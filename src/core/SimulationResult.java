package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SimulationResult {

    public static class TimelineSegment {
        private final String label;
        private final int startTime;
        private final int endTime;
        private final boolean contextSwitch;
        private final boolean idle;

        public TimelineSegment(String label, int startTime, int endTime, boolean contextSwitch, boolean idle) {
            this.label = label;
            this.startTime = startTime;
            this.endTime = endTime;
            this.contextSwitch = contextSwitch;
            this.idle = idle;
        }

        public String getLabel() {
            return label;
        }

        public int getStartTime() {
            return startTime;
        }

        public int getEndTime() {
            return endTime;
        }

        public int getDuration() {
            return endTime - startTime;
        }

        public boolean isContextSwitch() {
            return contextSwitch;
        }

        public boolean isIdle() {
            return idle;
        }

        @Override
        public String toString() {
            return label + "[" + startTime + "-" + endTime + "]";
        }
    }

    private final String schedulerName;
    private final int quantum;
    private final int contextSwitch;
    private final List<Snapshot> snapshots;
    private final List<TimelineSegment> timeline;
    private final List<Process> processes;

    private final double avgWaitingTime;
    private final double avgTurnaroundTime;
    private final double avgResponseTime;
    private final double cpuUtilization;
    private final int totalTime;
    private final int totalBusyTime;
    private final int totalContextSwitchTime;
    private final int contextSwitchCount;

    public SimulationResult(
            String schedulerName,
            int quantum,
            int contextSwitch,
            List<Snapshot> snapshots,
            List<TimelineSegment> timeline,
            List<Process> processes,
            double avgWaitingTime,
            double avgTurnaroundTime,
            double avgResponseTime,
            double cpuUtilization,
            int totalTime,
            int totalBusyTime,
            int totalContextSwitchTime,
            int contextSwitchCount) {
        this.schedulerName = schedulerName;
        this.quantum = quantum;
        this.contextSwitch = contextSwitch;
        this.snapshots = snapshots == null
                ? Collections.emptyList()
                : Collections.unmodifiableList(new ArrayList<>(snapshots));
        this.timeline = timeline == null
                ? Collections.emptyList()
                : Collections.unmodifiableList(new ArrayList<>(timeline));
        this.processes = processes == null
                ? Collections.emptyList()
                : Collections.unmodifiableList(new ArrayList<>(processes));
        this.avgWaitingTime = avgWaitingTime;
        this.avgTurnaroundTime = avgTurnaroundTime;
        this.avgResponseTime = avgResponseTime;
        this.cpuUtilization = cpuUtilization;
        this.totalTime = totalTime;
        this.totalBusyTime = totalBusyTime;
        this.totalContextSwitchTime = totalContextSwitchTime;
        this.contextSwitchCount = contextSwitchCount;
    }

    public String getSchedulerName() {
        return schedulerName;
    }

    public int getQuantum() {
        return quantum;
    }

    public int getContextSwitch() {
        return contextSwitch;
    }

    public List<Snapshot> getSnapshots() {
        return snapshots;
    }

    public List<TimelineSegment> getTimeline() {
        return timeline;
    }

    public List<Process> getProcesses() {
        return processes;
    }

    public double getAvgWaitingTime() {
        return avgWaitingTime;
    }

    public double getAvgTurnaroundTime() {
        return avgTurnaroundTime;
    }

    public double getAvgResponseTime() {
        return avgResponseTime;
    }

    public double getCpuUtilization() {
        return cpuUtilization;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public int getTotalBusyTime() {
        return totalBusyTime;
    }

    public int getTotalContextSwitchTime() {
        return totalContextSwitchTime;
    }

    public int getContextSwitchCount() {
        return contextSwitchCount;
    }
}
