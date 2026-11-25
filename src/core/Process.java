package core;

public class Process {

    private final String id;
    private final int arrivalTime;
    private final int burstTime;
    private final int initialPriority;

    private int remainingTime;
    private int effectivePriority;
    private int firstStartTime = -1;
    private int finishTime = -1;

    public Process(String id, int arrivalTime, int burstTime, int initialPriority) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Process id cannot be empty");
        }
        if (arrivalTime < 0) {
            throw new IllegalArgumentException("Arrival time cannot be negative: " + arrivalTime);
        }
        if (burstTime <= 0) {
            throw new IllegalArgumentException("Burst time must be positive: " + burstTime);
        }
        this.id = id.trim();
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.initialPriority = initialPriority;
        this.remainingTime = burstTime;
        this.effectivePriority = initialPriority;
    }

    public Process(Process other) {
        this.id = other.id;
        this.arrivalTime = other.arrivalTime;
        this.burstTime = other.burstTime;
        this.initialPriority = other.initialPriority;
        this.remainingTime = other.remainingTime;
        this.effectivePriority = other.effectivePriority;
        this.firstStartTime = other.firstStartTime;
        this.finishTime = other.finishTime;
    }

    public Process freshCopy() {
        return new Process(this.id, this.arrivalTime, this.burstTime, this.initialPriority);
    }

    public void reset() {
        this.remainingTime = this.burstTime;
        this.effectivePriority = this.initialPriority;
        this.firstStartTime = -1;
        this.finishTime = -1;
    }

    public String getId() {
        return id;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public int getInitialPriority() {
        return initialPriority;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        if (remainingTime < 0) {
            throw new IllegalArgumentException("Remaining time cannot be negative: " + remainingTime);
        }
        this.remainingTime = remainingTime;
    }

    public void executeFor(int duration) {
        if (duration < 0 || duration > remainingTime) {
            throw new IllegalArgumentException("Invalid execution duration: " + duration);
        }
        this.remainingTime -= duration;
    }

    public int getEffectivePriority() {
        return effectivePriority;
    }

    public void setEffectivePriority(int effectivePriority) {
        this.effectivePriority = effectivePriority;
    }

    public int getFirstStartTime() {
        return firstStartTime;
    }

    public void setFirstStartTime(int firstStartTime) {
        this.firstStartTime = firstStartTime;
    }

    public void recordStartIfUnset(int time) {
        if (this.firstStartTime == -1) {
            this.firstStartTime = time;
        }
    }

    public int getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(int finishTime) {
        this.finishTime = finishTime;
    }

    public boolean isFinished() {
        return remainingTime == 0 && finishTime != -1;
    }

    public int getTurnaroundTime() {
        if (finishTime == -1) {
            throw new IllegalStateException("Process " + id + " has not finished yet");
        }
        return finishTime - arrivalTime;
    }

    public int getWaitingTime() {
        return getTurnaroundTime() - burstTime;
    }

    public int getResponseTime() {
        if (firstStartTime == -1) {
            throw new IllegalStateException("Process " + id + " has not started yet");
        }
        return firstStartTime - arrivalTime;
    }

    @Override
    public String toString() {
        return String.format("%s (arrival=%d, burst=%d, priority=%d, remaining=%d)",
                id, arrivalTime, burstTime, initialPriority, remainingTime);
    }
}
