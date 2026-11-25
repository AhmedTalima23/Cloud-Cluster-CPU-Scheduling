package core;

import java.util.List;
import java.util.Locale;

public final class MatrixPrinter {

    private MatrixPrinter() {
    }

    public static void print(SimulationResult result) {
        System.out.print(formatAll(result));
    }

    public static String formatAll(SimulationResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("====================================================================\n");
        sb.append(String.format(Locale.US, "Scheduler: %s | Q = %d | CS = %d%n",
                result.getSchedulerName(), result.getQuantum(), result.getContextSwitch()));
        sb.append("====================================================================\n\n");
        sb.append(formatMatrix(result)).append("\n");
        if (!result.getTimeline().isEmpty()) {
            sb.append(formatTimeline(result)).append("\n");
        }
        sb.append(formatMetrics(result));
        return sb.toString();
    }

    public static String formatMatrix(SimulationResult result) {
        StringBuilder sb = new StringBuilder();
        String divider = "+------+---------+------------------------------------------------------------------+\n";
        sb.append("=== Heuristic Demotion Matrix ===\n");
        sb.append(divider);
        sb.append(String.format("| %-4s | %-7s | %-64s |%n", "Time", "Process", "Tier"));
        sb.append(divider);

        List<Snapshot> snapshots = result.getSnapshots();
        for (Snapshot snap : snapshots) {
            boolean hasNote = snap.getNote() != null && !snap.getNote().isEmpty();
            boolean showTier2 = !snap.getTier2().isEmpty() || snap.isDone() || hasAnyTier2(snapshots);

            if (hasNote) {
                sb.append(String.format("| %-4d | %-7s | %-64s |%n",
                        snap.getTime(), snap.getRunningProcess(), snap.getNote()));
                sb.append(String.format("| %-4s | %-7s | %-64s |%n", "", "", snap.formatTier1()));
                if (showTier2) {
                    sb.append(String.format("| %-4s | %-7s | %-64s |%n", "", "", snap.formatTier2()));
                }
            } else {
                sb.append(String.format("| %-4d | %-7s | %-64s |%n",
                        snap.getTime(), snap.getRunningProcess(), snap.formatTier1()));
                if (showTier2) {
                    sb.append(String.format("| %-4s | %-7s | %-64s |%n", "", "", snap.formatTier2()));
                }
            }
            sb.append(divider);
        }

        return sb.toString();
    }

    public static String formatTimeline(SimulationResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Execution Timeline ===\n");
        List<SimulationResult.TimelineSegment> segments = result.getTimeline();
        for (int i = 0; i < segments.size(); i++) {
            if (i > 0) {
                sb.append(" | ");
            }
            sb.append(segments.get(i).toString());
        }
        sb.append("\n");
        return sb.toString();
    }

    public static String formatMetrics(SimulationResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Scheduling Metrics ===\n");
        String divider = "+---------+---------+-------+----------+-------+--------+------------+---------+----------+\n";
        sb.append(divider);
        sb.append(String.format("| %-7s | %-7s | %-5s | %-8s | %-5s | %-6s | %-10s | %-7s | %-8s |%n",
                "Process", "Arrival", "Burst", "Priority", "Start", "Finish", "Turnaround", "Waiting", "Response"));
        sb.append(divider);

        for (Process p : result.getProcesses()) {
            sb.append(String.format("| %-7s | %-7d | %-5d | %-8d | %-5d | %-6d | %-10d | %-7d | %-8d |%n",
                    p.getId(),
                    p.getArrivalTime(),
                    p.getBurstTime(),
                    p.getInitialPriority(),
                    p.getFirstStartTime(),
                    p.getFinishTime(),
                    p.getTurnaroundTime(),
                    p.getWaitingTime(),
                    p.getResponseTime()));
        }
        sb.append(divider);
        sb.append(String.format(Locale.US, "Average Turnaround Time : %.2f time units%n",
                result.getAvgTurnaroundTime()));
        sb.append(String.format(Locale.US, "Average Waiting Time    : %.2f time units%n",
                result.getAvgWaitingTime()));
        sb.append(String.format(Locale.US, "Average Response Time   : %.2f time units%n",
                result.getAvgResponseTime()));
        sb.append(String.format(Locale.US, "CPU Utilization         : %.2f%% (%d/%d time units)%n",
                result.getCpuUtilization(), result.getTotalBusyTime(), result.getTotalTime()));
        sb.append(String.format(Locale.US, "Context Switches        : %d (overhead: %d time units)%n",
                result.getContextSwitchCount(), result.getTotalContextSwitchTime()));
        return sb.toString();
    }

    private static boolean hasAnyTier2(List<Snapshot> snapshots) {
        for (Snapshot s : snapshots) {
            if (!s.getTier2().isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
