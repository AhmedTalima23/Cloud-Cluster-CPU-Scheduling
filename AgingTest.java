public class AgingTest {

    static void check(String what, Object expected, Object actual) {
        System.out.println((expected.equals(actual) ? "PASS  " : "FAIL  ")
            + what + " (expected " + expected + ", got " + actual + ")");
    }

    public static void main(String[] args) {

        Tier2Queue queue = new Tier2Queue();

        Process p1 = new Process("P1", 0, 5, 4);
        Process p2 = new Process("P2", 1, 6, 2);
        Process p3 = new Process("P3", 4, 4, 3);

        queue.add(p1, 3);
        queue.add(p2, 7);
        queue.add(p3, 11);

        check("t=11 P1 priority", 2, queue.effectivePriorityOf(p1, 11));
        check("t=11 P2 priority", 1, queue.effectivePriorityOf(p2, 11));
        check("t=11 P3 priority", 3, queue.effectivePriorityOf(p3, 11));
        check("t=11 selected", "P2", queue.pickNext(11).name);

        check("t=15 P1 priority", 1, queue.effectivePriorityOf(p1, 15));
        check("t=15 P3 priority", 2, queue.effectivePriorityOf(p3, 15));
        check("t=15 selected", "P1", queue.pickNext(15).name);

        check("t=18 selected", "P3", queue.pickNext(18).name);
        check("queue is empty", true, queue.isEmpty());
    }
}