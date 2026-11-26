package acs;

import core.Process;
import core.Snapshot;

import java.util.ArrayList;
import java.util.List;

public class Tier2Queue {

    private static class Entry {
        Process process;
        int enteredAt;

        Entry(Process process, int enteredAt) {
            this.process = process;
            this.enteredAt = enteredAt;
        }
    }

    private final ArrayList<Entry> queue = new ArrayList<>();

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
                return Tier3Aging.effectivePriority(entry.process.getInitialPriority(), waitingTime);
            }
        }
        throw new IllegalArgumentException("Process not found");
    }

    public Process pickNext(int currentTime) {
        if (queue.isEmpty()) {
            return null;
        }
        int bestIndex = 0;
        int bestPriority = effectivePriorityOf(queue.get(0).process, currentTime);

        for (int i = 1; i < queue.size(); i++) {
            Entry entry = queue.get(i);
            int priority = effectivePriorityOf(entry.process, currentTime);
            if (priority < bestPriority) {
                bestPriority = priority;
                bestIndex = i;
            }
        }

        Entry selected = queue.remove(bestIndex);
        selected.process.setEffectivePriority(bestPriority);
        return selected.process;
    }

    public List<Snapshot.QueueEntry> snapshotEntries(int currentTime) {
        List<Snapshot.QueueEntry> entries = new ArrayList<>(queue.size());
        for (Entry e : queue) {
            int effPrio = effectivePriorityOf(e.process, currentTime);
            e.process.setEffectivePriority(effPrio);
            entries.add(new Snapshot.QueueEntry(e.process.getId(), e.process.getRemainingTime(), effPrio));
        }
        return entries;
    }
}
