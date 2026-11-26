package sjf;

import core.MetricsCalculator;
import core.Process;
import core.Scheduler;
import core.SimulationResult;
import core.Snapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Preemptive Shortest-Job-First (SRTF) with context-switch accounting. One decision per time unit.
 *
 * Rules (match the agreed team conventions):
 *  - The ready process with the smallest remaining time runs.
 *  - Tie-break: keep the running process (no needless switch), then earlier arrival, then input order.
 *  - CS is charged whenever the CPU moves from a previous process to a DIFFERENT one
 *    (preemption or after a finish), not at t = 0 and not after an idle gap.
 *  - The process being loaded is committed when the CS starts and runs at least one unit after it,
 *    so back-to-back switches can never loop forever. Arrivals during a CS just wait in the queue.
 *
 * SJF has no quantum: Tier 1 of the snapshot holds the ready queue (shortest first), Tier 2 stays empty.
 */
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
    public SimulationResult run(List<Process> input, int quantum, int contextSwitch) {
        List<Process> ps = new ArrayList<>();
        for (Process p : input) {
            ps.add(p.freshCopy());
        }
        int n = ps.size();
        List<Snapshot> snapshots = new ArrayList<>();
        List<SimulationResult.TimelineSegment> timeline = new ArrayList<>();

        int t = 0, done = 0, csLeft = 0, csCount = 0;
        Process last = null, committed = null, loading = null;

        while (done < n) {
            if (csLeft > 0) {                                   // context switch in progress
                snapshots.add(new Snapshot(t, Snapshot.EVENT_CS, queue(ps, t, loading), null,
                        "Context switch saves " + last.getId() + " and loads " + loading.getId()));
                addSegment(timeline, "CS", t, true, false);
                t++;
                if (--csLeft == 0) {
                    committed = loading;
                    loading = null;
                }
                continue;
            }

            Process best;
            if (committed != null) {
                best = committed;
                committed = null;
            } else {
                best = pick(ps, t, last);
                if (best == null) {                             // nothing ready: idle until next arrival
                    int next = Integer.MAX_VALUE;
                    for (Process p : ps) {
                        if (p.getRemainingTime() > 0) {
                            next = Math.min(next, p.getArrivalTime());
                        }
                    }
                    for (; t < next; t++) {
                        snapshots.add(new Snapshot(t, Snapshot.EVENT_IDLE, null, null));
                        addSegment(timeline, "IDLE", t, false, true);
                    }
                    last = null;
                    continue;
                }
                if (last != null && best != last) {
                    // Always count a logical switch; only insert CS delay when cost > 0.
                    csCount++;
                    if (contextSwitch > 0) {
                        loading = best;
                        csLeft = contextSwitch;
                        continue;
                    }
                }
            }

            best.recordStartIfUnset(t);
            snapshots.add(new Snapshot(t, best.getId(), queue(ps, t, best), null));
            addSegment(timeline, best.getId(), t, false, false);
            best.executeFor(1);
            t++;
            last = best;
            if (best.getRemainingTime() == 0) {
                best.setFinishTime(t);
                done++;
            }
        }
        return MetricsCalculator.buildResult(getName(), quantum, contextSwitch, snapshots, timeline, ps, csCount);
    }

    private Process pick(List<Process> ps, int t, Process last) {
        Process best = null;
        for (Process p : ps) {
            if (p.getArrivalTime() > t || p.getRemainingTime() == 0) {
                continue;
            }
            if (best == null || better(ps, p, best, last)) {
                best = p;
            }
        }
        return best;
    }

    private boolean better(List<Process> ps, Process a, Process b, Process last) {
        if (a.getRemainingTime() != b.getRemainingTime()) {
            return a.getRemainingTime() < b.getRemainingTime();
        }
        if (a == last) return true;
        if (b == last) return false;
        if (a.getArrivalTime() != b.getArrivalTime()) {
            return a.getArrivalTime() < b.getArrivalTime();
        }
        return ps.indexOf(a) < ps.indexOf(b);
    }

    /** Ready queue: arrived, unfinished, not on (or being loaded onto) the CPU; shortest first. */
    private List<Snapshot.QueueEntry> queue(List<Process> ps, int t, Process onCpu) {
        List<Process> q = new ArrayList<>();
        for (Process p : ps) {
            if (p.getArrivalTime() <= t && p.getRemainingTime() > 0 && p != onCpu) {
                q.add(p);
            }
        }
        q.sort((a, b) -> {
            if (a.getRemainingTime() != b.getRemainingTime()) return a.getRemainingTime() - b.getRemainingTime();
            if (a.getArrivalTime() != b.getArrivalTime()) return a.getArrivalTime() - b.getArrivalTime();
            return ps.indexOf(a) - ps.indexOf(b);
        });
        List<Snapshot.QueueEntry> out = new ArrayList<>();
        for (Process p : q) {
            out.add(new Snapshot.QueueEntry(p.getId(), p.getRemainingTime(), p.getEffectivePriority()));
        }
        return out;
    }

    /** Extends the last timeline segment when it has the same label and ends at t, else starts a new one. */
    private void addSegment(List<SimulationResult.TimelineSegment> tl, String label, int t, boolean cs, boolean idle) {
        if (!tl.isEmpty()) {
            SimulationResult.TimelineSegment last = tl.get(tl.size() - 1);
            if (last.getLabel().equals(label) && last.getEndTime() == t) {
                tl.set(tl.size() - 1, new SimulationResult.TimelineSegment(label, last.getStartTime(), t + 1, cs, idle));
                return;
            }
        }
        tl.add(new SimulationResult.TimelineSegment(label, t, t + 1, cs, idle));
    }
}
