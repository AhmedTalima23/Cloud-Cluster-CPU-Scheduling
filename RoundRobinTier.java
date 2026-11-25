import java.util.ArrayList;

public class RoundRobinTier {

    private ArrayList<Process> queue = new ArrayList<>();
    private int quantum;

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

    public String snapshot() {
        if (queue.isEmpty()) {
            return "Tier 1: [Empty]";
        }
        StringBuilder sb = new StringBuilder("Tier 1: [");
        for (int i = 0; i < queue.size(); i++) {
            Process p = queue.get(i);
            if (i > 0) sb.append(", ");
            sb.append(p.name)
              .append(" (remaining: ").append(p.remaining)
              .append(", Priority: ").append(p.basePriority)
              .append(")");
        }
        return sb.append("]").toString();
    }
}
