package experiments;

import core.InputParser;
import core.Process;
import core.Scheduler;
import core.SimulationResult;
import sjf.PreemptiveSJF;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class SweepRunner {
    static final int[] CS_VALUES = {0, 1, 2, 3};
    static final int[] Q_VALUES = {2, 3, 4, 5, 6};

    record Named(String name, List<Process> processes, int q) {}

    public static void main(String[] args) throws Exception {
        Path testsDir = Paths.get(args.length > 0 ? args[0] : "tests");
        Path outDir = Paths.get(args.length > 1 ? args[1] : "report");
        Path dataDir = outDir.resolve("data"), graphDir = outDir.resolve("graphs");
        Files.createDirectories(dataDir);
        Files.createDirectories(graphDir);

        List<Scheduler> schedulers = new ArrayList<>();
        schedulers.add(new PreemptiveSJF());
        try {
            schedulers.add((Scheduler) Class.forName("acs.ACSScheduler").getDeclaredConstructor().newInstance());
        } catch (ReflectiveOperationException ignored) {
        }

        List<Named> tests = new ArrayList<>();
        List<Path> files = new ArrayList<>();
        try (Stream<Path> s = Files.list(testsDir)) {
            s.filter(p -> p.toString().endsWith(".txt")).sorted().forEach(files::add);
        }
        for (Path f : files) {
            InputParser.TestCase tc = InputParser.parseFile(f);
            String fn = f.getFileName().toString();
            tests.add(new Named(fn.substring(0, fn.lastIndexOf('.')), tc.getProcesses(), tc.getQuantum()));
        }

        try (PrintWriter csv = new PrintWriter(dataDir.resolve("sweep.csv").toFile())) {
            csv.println("algorithm,testcase,CS,Q,avgWT,avgTAT,avgRT,cpuUtilization");
            for (Scheduler s : schedulers) {
                for (Named tc : tests) {
                    for (int cs : CS_VALUES) {
                        for (int q : s.usesQuantum() ? Q_VALUES : new int[]{tc.q()}) {
                            SimulationResult r = s.run(fresh(tc.processes()), q, cs);
                            csv.printf(Locale.US, "%s,%s,%d,%s,%.2f,%.2f,%.2f,%.2f%n", s.getName(), tc.name(), cs,
                                    s.usesQuantum() ? String.valueOf(q) : "-", r.getAvgWaitingTime(),
                                    r.getAvgTurnaroundTime(), r.getAvgResponseTime(), r.getCpuUtilization());
                        }
                    }
                }
            }
        }

        double[] csX = Arrays.stream(CS_VALUES).mapToDouble(v -> v).toArray();
        double[] qX = Arrays.stream(Q_VALUES).mapToDouble(v -> v).toArray();
        for (Scheduler s : schedulers) {
            String tag = s.getName().replaceAll("[^A-Za-z0-9]+", "_");
            LinkedHashMap<String, double[]> allWT = new LinkedHashMap<>(), allTAT = new LinkedHashMap<>();
            for (Named tc : tests) {
                if (!s.usesQuantum()) {
                    double[] wt = new double[CS_VALUES.length], tat = new double[CS_VALUES.length];
                    for (int i = 0; i < CS_VALUES.length; i++) {
                        SimulationResult r = s.run(fresh(tc.processes()), tc.q(), CS_VALUES[i]);
                        wt[i] = r.getAvgWaitingTime();
                        tat[i] = r.getAvgTurnaroundTime();
                    }
                    LinkedHashMap<String, double[]> ser = new LinkedHashMap<>();
                    ser.put("Avg waiting time", wt);
                    ser.put("Avg turnaround time", tat);
                    ChartWriter.line(graphDir.resolve(tag + "_" + tc.name() + "_vs_CS.png").toFile(),
                            s.getName() + " - " + tc.name(), "Context switch time (CS)", "Time units", csX, ser);
                    allWT.put(tc.name(), wt);
                    allTAT.put(tc.name(), tat);
                } else {
                    LinkedHashMap<String, double[]> serWT = new LinkedHashMap<>(), serTAT = new LinkedHashMap<>();
                    for (int cs : CS_VALUES) {
                        double[] wt = new double[Q_VALUES.length], tat = new double[Q_VALUES.length];
                        for (int j = 0; j < Q_VALUES.length; j++) {
                            SimulationResult r = s.run(fresh(tc.processes()), Q_VALUES[j], cs);
                            wt[j] = r.getAvgWaitingTime();
                            tat[j] = r.getAvgTurnaroundTime();
                        }
                        serWT.put("CS=" + cs, wt);
                        serTAT.put("CS=" + cs, tat);
                    }
                    ChartWriter.line(graphDir.resolve(tag + "_" + tc.name() + "_WT_vs_Q.png").toFile(),
                            s.getName() + " - avg waiting time - " + tc.name(), "Time quantum (Q)", "Avg waiting time", qX, serWT);
                    ChartWriter.line(graphDir.resolve(tag + "_" + tc.name() + "_TAT_vs_Q.png").toFile(),
                            s.getName() + " - avg turnaround time - " + tc.name(), "Time quantum (Q)", "Avg turnaround time", qX, serTAT);
                }
            }
            if (!s.usesQuantum()) {
                ChartWriter.line(graphDir.resolve(tag + "_ALL_WT_vs_CS.png").toFile(), s.getName()
                        + " - average waiting time, all test cases", "Context switch time (CS)", "Avg waiting time", csX, allWT);
                ChartWriter.line(graphDir.resolve(tag + "_ALL_TAT_vs_CS.png").toFile(), s.getName()
                        + " - average turnaround time, all test cases", "Context switch time (CS)", "Avg turnaround time", csX, allTAT);
            }
        }
        System.out.println("Done. See " + outDir.toAbsolutePath());
    }

    private static List<Process> fresh(List<Process> in) {
        List<Process> out = new ArrayList<>();
        for (Process p : in) {
            out.add(p.freshCopy());
        }
        return out;
    }
}
