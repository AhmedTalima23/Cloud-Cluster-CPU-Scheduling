package acs;

import core.Process;
import core.Snapshot;

import java.util.ArrayList;
import java.util.List;

public class RoundRobinTier {

    private final ArrayList<Process> queue = new ArrayList<>();
    private final int quantum;

    public RoundRobinTier(int quantum) {
        this.quantum = quantum;
    }

    public void add(Process process) {
        queue.add(process);
    }

    public Process poll() {
        if (queue.isEmpty()) {
            return null;
        }
        return queue.remove(0);
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public int getQuantum() {
        return quantum;
    }

    public List<Snapshot.QueueEntry> snapshotEntries() {
        List<Snapshot.QueueEntry> entries = new ArrayList<>(queue.size());
        for (Process p : queue) {
            entries.add(new Snapshot.QueueEntry(p.getId(), p.getRemainingTime(), p.getInitialPriority()));
        }
        return entries;
    }
}
