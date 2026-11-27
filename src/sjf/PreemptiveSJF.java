package sjf;

import core.MetricsCalculator;
import core.Process;
import core.Scheduler;
import core.SimulationResult;
import core.Snapshot;
import java.util.ArrayList;
import java.util.List;

public class PreemptiveSJF implements Scheduler {

    @Override
    public String getName() {
        return "Preemptive SJF";
    }

    @Override
    public boolean usesQuantum() {
        return false;
    }

    @Override
    public SimulationResult run(
            List<Process> input,
            int quantum,
            int contextSwitch) {

        if (input == null) {
            throw new IllegalArgumentException(
                    "Input cannot be null"
            );
        }

        if (contextSwitch < 0) {
            throw new IllegalArgumentException(
                    "Context switch cannot be negative"
            );
        }

        List<Process> ps = new ArrayList<>();

        for (Process p : input) {

            if (p == null) {
                throw new IllegalArgumentException(
                        "Process list cannot contain null"
                );
            }

            ps.add(p.freshCopy());
        }

        int n = ps.size();

        List<Snapshot> snapshots = new ArrayList<>();

        List<SimulationResult.TimelineSegment> timeline =
                new ArrayList<>();

        int t = 0;
        int done = 0;
        int csLeft = 0;
        int csCount = 0;

        Process last = null;

        // Indicates that a context switch has just finished.
        boolean afterContextSwitch = false;

        while (done < n) {

            // Handle Context Switching
            if (csLeft > 0) {

                snapshots.add(
                        new Snapshot(
                                t,
                                Snapshot.EVENT_CS,
                                queue(ps, t, null),
                                null,
                                "Context switch from " + last.getId()
                        )
                );

                addSegment(
                        timeline,
                        "CS",
                        t,
                        true,
                        false
                );

                t++;
                csLeft--;

                if (csLeft == 0) {
                    afterContextSwitch = true;
                }

                continue;
            }

            // Select the process with the shortest remaining time
            Process best = pick(ps, t, last);

            // Handle CPU Idle Time
            if (best == null) {

                int next = Integer.MAX_VALUE;

                for (Process p : ps) {

                    if (p.getRemainingTime() > 0) {

                        next = Math.min(
                                next,
                                p.getArrivalTime()
                        );
                    }
                }

                if (next == Integer.MAX_VALUE) {
                    break;
                }

                while (t < next) {

                    snapshots.add(
                            new Snapshot(
                                    t,
                                    Snapshot.EVENT_IDLE,
                                    null,
                                    null
                            )
                    );

                    addSegment(
                            timeline,
                            "IDLE",
                            t,
                            false,
                            true
                    );

                    t++;
                }

                last = null;
                afterContextSwitch = false;

                continue;
            }

            // Handle Process Switching
            if (last != null
                    && best != last
                    && !afterContextSwitch) {

                csCount++;

                if (contextSwitch > 0) {

                    csLeft = contextSwitch;

                    continue;
                }
            }

            // Context switch is completed.
            // The process is selected again after the CS.
            afterContextSwitch = false;

            // Execute the selected process
            best.recordStartIfUnset(t);

            snapshots.add(
                    new Snapshot(
                            t,
                            best.getId(),
                            queue(ps, t, best),
                            null
                    )
            );

            addSegment(
                    timeline,
                    best.getId(),
                    t,
                    false,
                    false
            );

            best.executeFor(1);

            t++;

            last = best;

            if (best.getRemainingTime() == 0) {

                best.setFinishTime(t);

                done++;
            }
        }

        return MetricsCalculator.buildResult(
                getName(),
                quantum,
                contextSwitch,
                snapshots,
                timeline,
                ps,
                csCount
        );
    }

    // Select the best ready process
    private Process pick(
            List<Process> ps,
            int t,
            Process last) {

        Process best = null;

        for (Process p : ps) {

            if (p.getArrivalTime() > t
                    || p.getRemainingTime() == 0) {

                continue;
            }

            if (best == null || better(ps, p, best, last)) {
                best = p;
            }
        }

        return best;
    }

    // Compare two processes
    private boolean better(
            List<Process> ps,
            Process a,
            Process b,
            Process last) {

        // First: Shortest Remaining Time
        if (a.getRemainingTime() != b.getRemainingTime()) {

            return a.getRemainingTime()
                    < b.getRemainingTime();
        }

        // Tie: Prefer the currently running process
        if (a == last) {
            return true;
        }

        if (b == last) {
            return false;
        }

        // Then: Earlier arrival time
        if (a.getArrivalTime() != b.getArrivalTime()) {

            return a.getArrivalTime()
                    < b.getArrivalTime();
        }

        // Finally: Original input order
        return ps.indexOf(a) < ps.indexOf(b);
    }

    // Build the ready queue snapshot
    private List<Snapshot.QueueEntry> queue(
            List<Process> ps,
            int t,
            Process onCpu) {

        List<Process> q = new ArrayList<>();

        for (Process p : ps) {

            if (p.getArrivalTime() <= t
                    && p.getRemainingTime() > 0
                    && p != onCpu) {

                q.add(p);
            }
        }

        q.sort((a, b) -> {

            int remainingCompare = Integer.compare(
                    a.getRemainingTime(),
                    b.getRemainingTime()
            );

            if (remainingCompare != 0) {
                return remainingCompare;
            }

            int arrivalCompare = Integer.compare(
                    a.getArrivalTime(),
                    b.getArrivalTime()
            );

            if (arrivalCompare != 0) {
                return arrivalCompare;
            }

            return Integer.compare(
                    ps.indexOf(a),
                    ps.indexOf(b)
            );
        });

        List<Snapshot.QueueEntry> out = new ArrayList<>();

        for (Process p : q) {

            out.add(
                    new Snapshot.QueueEntry(
                            p.getId(),
                            p.getRemainingTime(),
                            p.getEffectivePriority()
                    )
            );
        }

        return out;
    }

    // Add or merge timeline segments
    private void addSegment(
            List<SimulationResult.TimelineSegment> tl,
            String label,
            int t,
            boolean cs,
            boolean idle) {

        if (!tl.isEmpty()) {

            SimulationResult.TimelineSegment last =
                    tl.get(tl.size() - 1);

            if (last.getLabel().equals(label)
                    && last.getEndTime() == t) {

                tl.set(
                        tl.size() - 1,
                        new SimulationResult.TimelineSegment(
                                label,
                                last.getStartTime(),
                                t + 1,
                                cs,
                                idle
                        )
                );

                return;
            }
        }

        tl.add(
                new SimulationResult.TimelineSegment(
                        label,
                        t,
                        t + 1,
                        cs,
                        idle
                )
        );
    }
}