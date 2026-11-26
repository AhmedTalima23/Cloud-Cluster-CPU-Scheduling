package acs;

import core.MetricsCalculator;
import core.Process;
import core.Scheduler;
import core.SimulationResult;
import core.Snapshot;

import java.util.ArrayList;
import java.util.List;

public class ACSScheduler implements Scheduler {

    @Override
    public String getName() {
        return "ACS";
    }

    @Override
    public boolean usesQuantum() {
        return true;
    }

    @Override
    public SimulationResult run(List<Process> input, int quantum, int contextSwitch) {
        List<Process> processes = new ArrayList<>(input.size());
        for (Process p : input) {
            processes.add(p.freshCopy());
        }

        RoundRobinTier tier1 = new RoundRobinTier(quantum);
        Tier2Queue tier2 = new Tier2Queue();

        int n = processes.size();
        boolean[] arrived = new boolean[n];
        List<Snapshot> snapshots = new ArrayList<>();
        List<SimulationResult.TimelineSegment> timeline = new ArrayList<>();

        int now = 0;
        int completed = 0;
        int csCount = 0;
        Process lastRan = null;

        while (completed < n) {
            admitArrivals(processes, arrived, tier1, now);

            if (tier1.isEmpty() && tier2.isEmpty()) {
                int nextArr = nextArrivalTime(processes, arrived);
                snapshots.add(new Snapshot(now, Snapshot.EVENT_IDLE, tier1.snapshotEntries(), tier2.snapshotEntries(now)));
                timeline.add(new SimulationResult.TimelineSegment("IDLE", now, nextArr, false, true));
                now = nextArr;
                continue;
            }

            List<Snapshot.QueueEntry> t1Before = tier1.snapshotEntries();
            List<Snapshot.QueueEntry> t2Before = tier2.snapshotEntries(now);

            boolean fromTier1 = !tier1.isEmpty();
            Process current = fromTier1 ? tier1.poll() : tier2.pickNext(now);

            boolean switched = (lastRan != null && lastRan != current);
            if (switched) {
                csCount++;
                if (contextSwitch > 0) {
                    String note = "Context switch saves " + lastRan.getId() + " and loads " + current.getId();
                    snapshots.add(new Snapshot(now, Snapshot.EVENT_CS, t1Before, t2Before, note));
                    int csEnd = now + contextSwitch;
                    timeline.add(new SimulationResult.TimelineSegment("CS", now, csEnd, true, false));
                    while (true) {
                        int nextArr = nextArrivalBefore(processes, arrived, csEnd);
                        if (nextArr == -1) {
                            break;
                        }
                        now = nextArr;
                        admitArrivals(processes, arrived, tier1, now);
                        snapshots.add(new Snapshot(now, Snapshot.EVENT_CS, tier1.snapshotEntries(), tier2.snapshotEntries(now)));
                    }
                    now = csEnd;
                    admitArrivals(processes, arrived, tier1, now);
                    snapshots.add(new Snapshot(now, current.getId(), tier1.snapshotEntries(), tier2.snapshotEntries(now)));
                } else {
                    snapshots.add(new Snapshot(now, current.getId(), t1Before, t2Before));
                }
            } else {
                snapshots.add(new Snapshot(now, current.getId(), t1Before, t2Before));
            }

            current.recordStartIfUnset(now);

            int runTime = fromTier1 ? Math.min(tier1.getQuantum(), current.getRemainingTime()) : current.getRemainingTime();
            int execEnd = now + runTime;
            timeline.add(new SimulationResult.TimelineSegment(current.getId(), now, execEnd, false, false));

            while (true) {
                int nextArr = nextArrivalBefore(processes, arrived, execEnd);
                if (nextArr == -1) {
                    break;
                }
                current.executeFor(nextArr - now);
                now = nextArr;
                admitArrivals(processes, arrived, tier1, now);
                snapshots.add(new Snapshot(now, current.getId(), tier1.snapshotEntries(), tier2.snapshotEntries(now)));
            }

            current.executeFor(execEnd - now);
            now = execEnd;
            lastRan = current;

            if (current.getRemainingTime() == 0) {
                current.setFinishTime(now);
                completed++;
            } else {
                tier2.add(current, now);
            }
        }

        snapshots.add(new Snapshot(now, Snapshot.EVENT_DONE, tier1.snapshotEntries(), tier2.snapshotEntries(now)));

        return MetricsCalculator.buildResult(getName(), quantum, contextSwitch, snapshots, timeline, processes, csCount);
    }

    private boolean admitArrivals(List<Process> processes, boolean[] arrived, RoundRobinTier tier1, int time) {
        boolean added = false;
        for (int i = 0; i < processes.size(); i++) {
            if (!arrived[i] && processes.get(i).getArrivalTime() <= time) {
                tier1.add(processes.get(i));
                arrived[i] = true;
                added = true;
            }
        }
        return added;
    }

    private int nextArrivalTime(List<Process> processes, boolean[] arrived) {
        int earliest = Integer.MAX_VALUE;
        for (int i = 0; i < processes.size(); i++) {
            if (!arrived[i] && processes.get(i).getArrivalTime() < earliest) {
                earliest = processes.get(i).getArrivalTime();
            }
        }
        return earliest;
    }

    private int nextArrivalBefore(List<Process> processes, boolean[] arrived, int limit) {
        int earliest = Integer.MAX_VALUE;
        for (int i = 0; i < processes.size(); i++) {
            int arr = processes.get(i).getArrivalTime();
            if (!arrived[i] && arr < limit && arr < earliest) {
                earliest = arr;
            }
        }
        return earliest == Integer.MAX_VALUE ? -1 : earliest;
    }
}
