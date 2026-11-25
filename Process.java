public class Process {

    public String name;
    public int arrival;
    public int burst;
    public int basePriority;
    public int remaining;
    public int firstStart = -1;
    public int finish = -1;

    public Process(String name, int arrival, int burst, int basePriority) {
        this.name = name;
        this.arrival = arrival;
        this.burst = burst;
        this.basePriority = basePriority;
        this.remaining = burst;
    }

    public int turnaroundTime() {
        return finish - arrival;
    }

    public int waitingTime() {
        return turnaroundTime() - burst;
    }

}
