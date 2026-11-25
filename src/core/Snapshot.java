package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Snapshot {

    public static class QueueEntry {
        private final String processId;
        private final int remainingTime;
        private final int priority;

        public QueueEntry(String processId, int remainingTime, int priority) {
            this.processId = processId;
            this.remainingTime = remainingTime;
            this.priority = priority;
        }

        public String getProcessId() {
            return processId;
        }

        public int getRemainingTime() {
            return remainingTime;
        }

        public int getPriority() {
            return priority;
        }

        @Override
        public String toString() {
            return processId + " (remaining: " + remainingTime + ", Priority: " + priority + ")";
        }
    }

    public static final String EVENT_CS = "CS";
    public static final String EVENT_IDLE = "IDLE";
    public static final String EVENT_DONE = "DONE";

    private final int time;
    private final String runningProcess;
    private final List<QueueEntry> tier1;
    private final List<QueueEntry> tier2;
    private final String note;

    public Snapshot(int time, String runningProcess, List<QueueEntry> tier1, List<QueueEntry> tier2) {
        this(time, runningProcess, tier1, tier2, "");
    }

    public Snapshot(int time, String runningProcess, List<QueueEntry> tier1, List<QueueEntry> tier2, String note) {
        this.time = time;
        this.runningProcess = runningProcess == null ? "" : runningProcess;
        this.tier1 = tier1 == null
                ? Collections.emptyList()
                : Collections.unmodifiableList(new ArrayList<>(tier1));
        this.tier2 = tier2 == null
                ? Collections.emptyList()
                : Collections.unmodifiableList(new ArrayList<>(tier2));
        this.note = note == null ? "" : note;
    }

    public int getTime() {
        return time;
    }

    public String getRunningProcess() {
        return runningProcess;
    }

    public boolean isContextSwitch() {
        return EVENT_CS.equalsIgnoreCase(runningProcess);
    }

    public boolean isIdle() {
        return EVENT_IDLE.equalsIgnoreCase(runningProcess);
    }

    public boolean isDone() {
        return EVENT_DONE.equalsIgnoreCase(runningProcess);
    }

    public List<QueueEntry> getTier1() {
        return tier1;
    }

    public List<QueueEntry> getTier2() {
        return tier2;
    }

    public String getNote() {
        return note;
    }

    public String formatTier1() {
        return formatQueue("Tier 1", tier1);
    }

    public String formatTier2() {
        return formatQueue("Tier 2", tier2);
    }

    public static String formatQueue(String label, List<QueueEntry> entries) {
        if (entries == null || entries.isEmpty()) {
            return label + ": [Empty]";
        }
        StringBuilder sb = new StringBuilder(label).append(": [");
        for (int i = 0; i < entries.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(entries.get(i).toString());
        }
        return sb.append("]").toString();
    }
}
