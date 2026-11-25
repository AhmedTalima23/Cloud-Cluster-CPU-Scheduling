import java.util.ArrayList;
import java.util.Locale;

public class ACSEngine {

    private RoundRobinTier tier1;
    private Tier2Queue tier2;
    private int csCost;
    private int csCount;

    public ACSEngine(int quantum, int csCost) {
        this.tier1 = new RoundRobinTier(quantum);
        this.tier2 = new Tier2Queue();
        this.csCost = csCost;
    }


    public void run(ArrayList<Process> processes) {
        int n = processes.size();
        boolean[] arrived = new boolean[n];
        ArrayList<String> timeline = new ArrayList<>();

        int now = 0;
        int completed = 0;
        csCount = 0;
        Process lastRan = null;

        System.out.println("=== Per-Step Snapshot Table ===");
        System.out.printf("%-6s %-8s %s%n", "Time", "Running", "Queues");

        while (completed < n) {
            admitArrivals(processes, arrived, now);

            if (tier1.isEmpty() && tier2.isEmpty()) {
                int nextArr = nextArrivalTime(processes, arrived);
                timeline.add("IDLE[" + now + "-" + nextArr + "]");
                now = nextArr;
                continue;
            }

            String snapBefore = tier1.snapshot() + " | " + tier2.snapshot(now);

            boolean fromTier1 = !tier1.isEmpty();
            Process current = fromTier1 ? tier1.poll() : tier2.pickNext(now);

            boolean switched = (lastRan != null && lastRan != current);
            if (switched) {
                csCount++;
                if (csCost > 0) {
                    printRow(now, "CS", snapBefore);
                    int csEnd = now + csCost;
                    timeline.add("CS[" + now + "-" + csEnd + "]");
                    while (true) {
                        int nextArr = nextArrivalBefore(processes, arrived, csEnd);
                        if (nextArr == -1) break;
                        now = nextArr;
                        admitArrivals(processes, arrived, now);
                        printRow(now, "CS", tier1.snapshot() + " | " + tier2.snapshot(now));
                    }
                    now = csEnd;
                } else {
                    printRow(now, current.name, snapBefore);
                }
            } else {
                printRow(now, current.name, snapBefore);
            }

            if (switched && csCost > 0 && admitArrivals(processes, arrived, now)) {
                printRow(now, current.name, tier1.snapshot() + " | " + tier2.snapshot(now));
            }

            if (current.firstStart == -1) {
                current.firstStart = now;
            }

            int runTime = fromTier1 ? Math.min(tier1.getQuantum(), current.remaining) : current.remaining;
            int execEnd = now + runTime;
            timeline.add(current.name + "[" + now + "-" + execEnd + "]");

            while (true) {
                int nextArr = nextArrivalBefore(processes, arrived, execEnd);
                if (nextArr == -1) break;
                current.remaining -= (nextArr - now);
                now = nextArr;
                admitArrivals(processes, arrived, now);
                printRow(now, current.name, tier1.snapshot() + " | " + tier2.snapshot(now));
            }

            current.remaining -= (execEnd - now);
            now = execEnd;
            lastRan = current;

            if (current.remaining == 0) {
                current.finish = now;
                completed++;
            } else {
                tier2.add(current, now);
            }
        }

        printRow(now, "DONE", tier1.snapshot() + " | " + tier2.snapshot(now));

        System.out.println("\n=== Timeline ===");
        System.out.println(String.join(" | ", timeline));

        printResults(processes);
    }

    private boolean admitArrivals(ArrayList<Process> processes, boolean[] arrived, int time) {
        boolean added = false;
        for (int i = 0; i < processes.size(); i++) {
            if (!arrived[i] && processes.get(i).arrival <= time) {
                tier1.add(processes.get(i));
                arrived[i] = true;
                added = true;
            }
        }
        return added;
    }

    private int nextArrivalTime(ArrayList<Process> processes, boolean[] arrived) {
        int earliest = Integer.MAX_VALUE;
        for (int i = 0; i < processes.size(); i++) {
            if (!arrived[i] && processes.get(i).arrival < earliest) {
                earliest = processes.get(i).arrival;
            }
        }
        return earliest;
    }

    private int nextArrivalBefore(ArrayList<Process> processes, boolean[] arrived, int limit) {
        int earliest = Integer.MAX_VALUE;
        for (int i = 0; i < processes.size(); i++) {
            int arr = processes.get(i).arrival;
            if (!arrived[i] && arr < limit && arr < earliest) {
                earliest = arr;
            }
        }
        return earliest == Integer.MAX_VALUE ? -1 : earliest;
    }

    private void printRow(int time, String running, String queues) {
        System.out.printf("%-6d %-8s %s%n", time, running, queues);
    }

    private void printResults(ArrayList<Process> processes) {
        System.out.println("\n=== Results ===");
        System.out.printf("%-8s %-8s %-8s %-8s %-11s %-8s%n",
                "Process", "Arrival", "Burst", "Finish", "Turnaround", "Waiting");

        double sumTat = 0, sumWt = 0;

        for (Process p : processes) {
            System.out.printf("%-8s %-8d %-8d %-8d %-11d %-8d%n",
                    p.name, p.arrival, p.burst, p.finish,
                    p.turnaroundTime(), p.waitingTime());
            sumTat += p.turnaroundTime();
            sumWt += p.waitingTime();
        }

        int n = processes.size();
        System.out.printf(Locale.US, "Avg Turnaround: %.2f | Avg Waiting: %.2f%n%n",
                sumTat / n, sumWt / n);
    }
}
