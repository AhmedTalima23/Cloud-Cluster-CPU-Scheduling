package gui;

import java.util.List;

public final class GuiModels {

    private GuiModels() {
    }

    public static class ProcessSpec {
        public final String id;
        public final int arrival;
        public final int burst;
        public final int priority;

        public ProcessSpec(String id, int arrival, int burst, int priority) {
            this.id = id;
            this.arrival = arrival;
            this.burst = burst;
            this.priority = priority;
        }
    }

    public static class TimelineBlock {
        public final String label;
        public final int startTime;
        public int endTime;
        public final boolean isContextSwitch;

        public TimelineBlock(String label, int startTime, int endTime, boolean isContextSwitch) {
            this.label = label;
            this.startTime = startTime;
            this.endTime = endTime;
            this.isContextSwitch = isContextSwitch;
        }
    }

    public static class StepSnapshot {
        public final int time;
        public final String activeLabel;
        public final String tier1Summary;
        public final String tier2Summary;

        public StepSnapshot(int time, String activeLabel, String tier1Summary, String tier2Summary) {
            this.time = time;
            this.activeLabel = activeLabel;
            this.tier1Summary = tier1Summary;
            this.tier2Summary = tier2Summary;
        }
    }

    public static class ProcessMetricRow {
        public final String processId;
        public final int arrival;
        public final int burst;
        public final int finish;
        public final int waitingTime;
        public final int turnaroundTime;
        public final int responseTime;

        public ProcessMetricRow(
                String processId,
                int arrival,
                int burst,
                int finish,
                int waitingTime,
                int turnaroundTime,
                int responseTime
        ) {
            this.processId = processId;
            this.arrival = arrival;
            this.burst = burst;
            this.finish = finish;
            this.waitingTime = waitingTime;
            this.turnaroundTime = turnaroundTime;
            this.responseTime = responseTime;
        }
    }

    public static class SimulationOutput {
        public final String schedulerLabel;
        public final List<TimelineBlock> blocks;
        public final List<StepSnapshot> snapshots;
        public final List<ProcessMetricRow> processRows;
        public final double avgWaitingTime;
        public final double avgTurnaroundTime;
        public final double cpuUtilization;

        public SimulationOutput(
                String schedulerLabel,
                List<TimelineBlock> blocks,
                List<StepSnapshot> snapshots,
                List<ProcessMetricRow> processRows,
                double avgWaitingTime,
                double avgTurnaroundTime,
                double cpuUtilization
        ) {
            this.schedulerLabel = schedulerLabel;
            this.blocks = blocks;
            this.snapshots = snapshots;
            this.processRows = processRows;
            this.avgWaitingTime = avgWaitingTime;
            this.avgTurnaroundTime = avgTurnaroundTime;
            this.cpuUtilization = cpuUtilization;
        }
    }
}
