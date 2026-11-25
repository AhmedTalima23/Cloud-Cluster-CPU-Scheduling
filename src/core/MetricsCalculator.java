package core;

import java.util.List;

public final class MetricsCalculator {

    private MetricsCalculator() {
    }

    public static int turnaroundTime(Process p) {
        return p.getTurnaroundTime();
    }

    public static int waitingTime(Process p) {
        return p.getWaitingTime();
    }

    public static int responseTime(Process p) {
        return p.getResponseTime();
    }

    public static double averageTurnaroundTime(List<Process> processes) {
        if (processes == null || processes.isEmpty()) {
            return 0.0;
        }
        double sum = 0.0;
        for (Process p : processes) {
            sum += p.getTurnaroundTime();
        }
        return sum / processes.size();
    }

    public static double averageWaitingTime(List<Process> processes) {
        if (processes == null || processes.isEmpty()) {
            return 0.0;
        }
        double sum = 0.0;
        for (Process p : processes) {
            sum += p.getWaitingTime();
        }
        return sum / processes.size();
    }

    public static double averageResponseTime(List<Process> processes) {
        if (processes == null || processes.isEmpty()) {
            return 0.0;
        }
        double sum = 0.0;
        for (Process p : processes) {
            sum += p.getResponseTime();
        }
        return sum / processes.size();
    }

    public static double cpuUtilization(int totalBusyTime, int totalTime) {
        if (totalTime <= 0) {
            return 0.0;
        }
        return (100.0 * totalBusyTime) / totalTime;
    }

    public static SimulationResult buildResult(
            String schedulerName,
            int quantum,
            int contextSwitch,
            List<Snapshot> snapshots,
            List<SimulationResult.TimelineSegment> timeline,
            List<Process> processes,
            int contextSwitchCount) {

        int totalBusyTime = 0;
        int maxFinishTime = 0;

        if (processes != null) {
            for (Process p : processes) {
                totalBusyTime += p.getBurstTime();
                if (p.getFinishTime() > maxFinishTime) {
                    maxFinishTime = p.getFinishTime();
                }
            }
        }

        int totalCsTime = 0;
        int timelineEnd = 0;
        if (timeline != null) {
            for (SimulationResult.TimelineSegment seg : timeline) {
                if (seg.isContextSwitch()) {
                    totalCsTime += seg.getDuration();
                }
                if (seg.getEndTime() > timelineEnd) {
                    timelineEnd = seg.getEndTime();
                }
            }
        } else {
            totalCsTime = contextSwitchCount * Math.max(0, contextSwitch);
        }

        int totalTime = Math.max(maxFinishTime, timelineEnd);
        double avgWt = averageWaitingTime(processes);
        double avgTat = averageTurnaroundTime(processes);
        double avgRt = averageResponseTime(processes);
        double utilization = cpuUtilization(totalBusyTime, totalTime);

        return new SimulationResult(
                schedulerName,
                quantum,
                contextSwitch,
                snapshots,
                timeline,
                processes,
                avgWt,
                avgTat,
                avgRt,
                utilization,
                totalTime,
                totalBusyTime,
                totalCsTime,
                contextSwitchCount
        );
    }
}
