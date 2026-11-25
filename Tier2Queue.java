
import java.util.ArrayList;

public class Tier2Queue {

    private static class Entry {

        Process process;
        int enteredAt;

        Entry(Process process, int enteredAt) {
            this.process = process;
            this.enteredAt = enteredAt;
        }
    }

    private ArrayList<Entry> queue = new ArrayList<>();

    public void add(Process process, int currentTime) {

        if (process == null) {
            throw new IllegalArgumentException("Process cannot be null");
        }

        queue.add(new Entry(process, currentTime));
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public int size() {
        return queue.size();
    }

    public int effectivePriorityOf(Process process, int currentTime) {

        for (Entry entry : queue) {

            if (entry.process == process) {

                int waitingTime = currentTime - entry.enteredAt;

                return Tier3Aging.effectivePriority(
                    entry.process.basePriority,
                    waitingTime
                );
            }
        }

        throw new IllegalArgumentException("Process not found");
    }

    public Process pickNext(int currentTime) {

        if (queue.isEmpty()) {
            return null;
        }

        int bestIndex = 0;

        int bestPriority = effectivePriorityOf(
            queue.get(0).process,
            currentTime
        );

        for (int i = 1; i < queue.size(); i++) {

            Entry entry = queue.get(i);

            int priority = effectivePriorityOf(
                entry.process,
                currentTime
            );

            if (priority < bestPriority) {
                bestPriority = priority;
                bestIndex = i;
            }
        }

        Entry selected = queue.remove(bestIndex);

        return selected.process;
    }
    
    public String snapshot(int currentTime) {
        if (queue.isEmpty()) {
            return "Tier 2: [Empty]";
        }
        StringBuilder sb = new StringBuilder("Tier 2: [");
        for (int i = 0; i < queue.size(); i++) {
            Entry e = queue.get(i);
            if (i > 0) sb.append(", ");
            sb.append(e.process.name)
              .append(" (remaining: ").append(e.process.remaining)
              .append(", Priority: ")
              .append(effectivePriorityOf(e.process, currentTime))
              .append(")");
        }
        return sb.append("]").toString();
    }
}
