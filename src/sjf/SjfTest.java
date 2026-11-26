package sjf;

import core.Process;
import core.SimulationResult;

import java.util.ArrayList;
import java.util.List;

public class SjfTest {
    static int failed = 0;

    static void check(String what, double actual, double expected) {
        boolean ok = Math.abs(actual - expected) < 1e-9;
        if (!ok) failed++;
        System.out.println((ok ? "PASS " : "FAIL ") + what + " = " + actual + (ok ? "" : " (expected " + expected + ")"));
    }

    static List<Process> list(int[][] d) {
        List<Process> l = new ArrayList<>();
        for (int i = 0; i < d.length; i++) l.add(new Process("P" + (i + 1), d[i][0], d[i][1], 1));
        return l;
    }

    public static void main(String[] a) {
        PreemptiveSJF s = new PreemptiveSJF();

        SimulationResult r = s.run(list(new int[][]{{0, 8}, {1, 4}, {2, 9}, {3, 5}}), 3, 0);
        check("classic avg WT", r.getAvgWaitingTime(), 6.5);
        check("classic avg TAT", r.getAvgTurnaroundTime(), 13.0);
        check("classic P2 finish", r.getProcesses().get(1).getFinishTime(), 5);

        r = s.run(list(new int[][]{{0, 6}, {2, 4}}), 3, 1);
        check("tie P1 finish (no preemption)", r.getProcesses().get(0).getFinishTime(), 6);
        check("tie P2 finish (CS after P1)", r.getProcesses().get(1).getFinishTime(), 11);

        r = s.run(list(new int[][]{{0, 2}, {10, 3}}), 3, 5);
        check("idle P2 start", r.getProcesses().get(1).getFirstStartTime(), 10);
        check("idle P2 finish", r.getProcesses().get(1).getFinishTime(), 13);

        r = s.run(list(new int[][]{{0, 4}, {1, 2}}), 3, 1);
        check("preempt P2 start", r.getProcesses().get(1).getFirstStartTime(), 2);
        check("preempt P2 finish", r.getProcesses().get(1).getFinishTime(), 4);
        check("preempt P1 finish", r.getProcesses().get(0).getFinishTime(), 8);
        check("preempt context switches", r.getContextSwitchCount(), 2);
        check("preempt CPU util %", Math.round(r.getCpuUtilization() * 10) / 10.0, 75.0);

        System.out.println(failed == 0 ? "ALL TESTS PASSED" : failed + " TEST(S) FAILED");
        if (failed > 0) System.exit(1);
    }
}
