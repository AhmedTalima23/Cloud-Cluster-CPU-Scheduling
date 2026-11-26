package experiments;

import core.InputParser;
import core.Process;
import core.SimulationResult;
import core.Snapshot;
import sjf.PreemptiveSJF;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

/**
 * Runs SJF on every test file (with the file's own Q and CS) and writes the scheduling analysis tables as
 * Markdown: time / CPU / ready queue, then WT / TAT / RT per process with the averages.
 * Usage: java -cp out experiments.AnalysisWriter [testsDir] [outDir]   (defaults: tests  outputs)
 */
public class AnalysisWriter {
    public static void main(String[] args) throws Exception {
        Path testsDir = Paths.get(args.length > 0 ? args[0] : "tests");
        Path outDir = Paths.get(args.length > 1 ? args[1] : "outputs");
        Files.createDirectories(outDir);
        List<Path> files = new ArrayList<>();
        try (Stream<Path> s = Files.list(testsDir)) {
            s.filter(p -> p.toString().endsWith(".txt")).sorted().forEach(files::add);
        }
        for (Path f : files) {
            InputParser.TestCase tc = InputParser.parseFile(f);
            SimulationResult r = new PreemptiveSJF().run(tc.getProcessesCopy(), tc.getQuantum(), tc.getContextSwitch());
            String name = f.getFileName().toString().replaceFirst("\\.txt$", "");
            String md = toMarkdown(name, tc, r);
            Files.writeString(outDir.resolve("sjf_" + name + "_analysis.md"), md);
            System.out.println(md);
        }
    }

    static String toMarkdown(String name, InputParser.TestCase tc, SimulationResult r) {
        StringBuilder sb = new StringBuilder();
        sb.append("#### ").append(name).append(" (").append(r.getSchedulerName())
                .append(", CS = ").append(r.getContextSwitch()).append(")\n\n");
        sb.append("| Process | Arrival | Burst |\n|---|---|---|\n");
        for (Process p : tc.getProcesses()) {
            sb.append(String.format("| %s | %d | %d |%n", p.getId(), p.getArrivalTime(), p.getBurstTime()));
        }
        sb.append("\n| Time | CPU | Ready queue |\n|---|---|---|\n");
        int from = -1, to = -1;
        String cpu = null, q = null;
        for (Snapshot s : r.getSnapshots()) {
            String c = s.isContextSwitch() ? "CS: " + s.getNote().replace("Context switch saves ", "")
                    .replace(" and loads ", " -> ") : s.getRunningProcess();
            String qs = s.formatTier1().replaceFirst("^Tier 1: ", "").replace(", Priority: " , "\u0000");
            qs = qs.replaceAll("\u0000-?\\d+\\)", ")");
            if (cpu != null && c.equals(cpu) && qs.equals(q) && s.getTime() == to + 1) {
                to = s.getTime();
                continue;
            }
            if (cpu != null) row(sb, from, to, cpu, q);
            from = to = s.getTime();
            cpu = c;
            q = qs;
        }
        if (cpu != null) row(sb, from, to, cpu, q);
        sb.append("\n| Process | Arrival | Burst | Finish | TAT | WT | RT |\n|---|---|---|---|---|---|---|\n");
        for (Process p : r.getProcesses()) {
            sb.append(String.format("| %s | %d | %d | %d | %d | %d | %d |%n", p.getId(), p.getArrivalTime(),
                    p.getBurstTime(), p.getFinishTime(), p.getTurnaroundTime(), p.getWaitingTime(), p.getResponseTime()));
        }
        sb.append(String.format(Locale.US, "%nAverage turnaround time = %.2f, average waiting time = %.2f, average response time = %.2f, "
                        + "CPU utilization = %.1f%% (%d/%d), context switches = %d%n%n",
                r.getAvgTurnaroundTime(), r.getAvgWaitingTime(), r.getAvgResponseTime(), r.getCpuUtilization(),
                r.getTotalBusyTime(), r.getTotalTime(), r.getContextSwitchCount()));
        return sb.toString();
    }

    private static void row(StringBuilder sb, int from, int to, String cpu, String queue) {
        sb.append(String.format("| %s | %s | %s |%n", from == to ? "" + from : from + "-" + to, cpu, queue));
    }
}
