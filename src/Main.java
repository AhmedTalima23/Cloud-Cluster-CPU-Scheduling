import core.InputParser;
import core.MatrixPrinter;
import core.Process;
import core.Scheduler;
import core.SimulationResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static final Map<String, Scheduler> SCHEDULERS = new LinkedHashMap<>();

    static {
        registerIfAvailable("sjf", "sjf.PreemptiveSJF");
        registerIfAvailable("acs", "acs.ACSScheduler");
    }

    private static void registerIfAvailable(String key, String className) {
        try {
            Class<?> clazz = Class.forName(className);
            Object instance = clazz.getDeclaredConstructor().newInstance();
            if (instance instanceof Scheduler) {
                SCHEDULERS.put(key, (Scheduler) instance);
            }
        } catch (ClassNotFoundException ignored) {
        } catch (Exception e) {
            System.err.println("Warning: could not instantiate " + className + ": " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            if (args.length == 0) {
                runInteractive();
            } else {
                runCli(args);
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void runCli(String[] args) throws IOException {
        String schedulerKey = null;
        String filePath = null;
        Integer overrideQ = null;
        Integer overrideCs = null;
        String outputPath = null;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--list":
                    printAvailableSchedulers();
                    return;
                case "--help":
                case "-h":
                    printUsage();
                    return;
                case "--scheduler":
                case "-s":
                    schedulerKey = requireArg(args, ++i, "--scheduler").toLowerCase();
                    break;
                case "--file":
                case "-f":
                    filePath = requireArg(args, ++i, "--file");
                    break;
                case "--q":
                case "--quantum":
                    overrideQ = Integer.parseInt(requireArg(args, ++i, "--q"));
                    break;
                case "--cs":
                    overrideCs = Integer.parseInt(requireArg(args, ++i, "--cs"));
                    break;
                case "--out":
                case "-o":
                    outputPath = requireArg(args, ++i, "--out");
                    break;
                default:
                    throw new IllegalArgumentException("Unknown argument: " + args[i] + " (use --help)");
            }
        }

        if (schedulerKey == null || filePath == null) {
            printUsage();
            throw new IllegalArgumentException("Both --scheduler and --file are required in CLI mode.");
        }

        Scheduler scheduler = SCHEDULERS.get(schedulerKey);
        if (scheduler == null) {
            printAvailableSchedulers();
            throw new IllegalArgumentException(
                    "Scheduler '" + schedulerKey + "' is not registered yet. "
                            + "Ensure it is in package sjf/acs and implements core.Scheduler.");
        }

        InputParser.TestCase tc = InputParser.parseFile(filePath);
        int q = overrideQ != null ? overrideQ : tc.getQuantum();
        int cs = overrideCs != null ? overrideCs : tc.getContextSwitch();

        SimulationResult result = scheduler.run(tc.getProcessesCopy(), q, cs);
        String formatted = MatrixPrinter.formatAll(result);
        System.out.print(formatted);

        if (outputPath != null) {
            Path out = Path.of(outputPath);
            if (out.getParent() != null) {
                Files.createDirectories(out.getParent());
            }
            Files.writeString(out, formatted);
            System.out.println("\nSaved output to: " + outputPath);
        }
    }

    private static void runInteractive() throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== Cloud-Cluster CPU Scheduling Simulator ===");

        if (SCHEDULERS.isEmpty()) {
            System.out.println("No schedulers implementing core.Scheduler are integrated yet.");
            System.out.println("Expected classes: sjf.PreemptiveSJF (Student 2), acs.ACSScheduler (Students 3 & 4).");
            return;
        }

        List<String> keys = new ArrayList<>(SCHEDULERS.keySet());
        System.out.println("\nSelect a scheduler:");
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            System.out.printf("  %d) %s (%s)%n", i + 1, SCHEDULERS.get(key).getName(), key);
        }
        System.out.print("Choice [1-" + keys.size() + "]: ");
        int choice = Integer.parseInt(sc.nextLine().trim());
        if (choice < 1 || choice > keys.size()) {
            throw new IllegalArgumentException("Invalid scheduler choice.");
        }
        Scheduler scheduler = SCHEDULERS.get(keys.get(choice - 1));

        System.out.println("\nInput source:");
        System.out.println("  1) Load from test-case file");
        System.out.println("  2) Enter processes manually");
        System.out.print("Choice [1-2]: ");
        int inputChoice = Integer.parseInt(sc.nextLine().trim());

        List<Process> processes;
        int q;
        int cs;

        if (inputChoice == 1) {
            System.out.print("Enter test-case file path (e.g., tests/tc1.txt): ");
            String path = sc.nextLine().trim();
            InputParser.TestCase tc = InputParser.parseFile(path);
            processes = tc.getProcessesCopy();
            q = tc.getQuantum();
            cs = tc.getContextSwitch();

            if (scheduler.usesQuantum()) {
                System.out.print("Quantum Q [Enter to keep " + q + "]: ");
                String qIn = sc.nextLine().trim();
                if (!qIn.isEmpty()) {
                    q = Integer.parseInt(qIn);
                }
            }
            System.out.print("Context Switch CS [Enter to keep " + cs + "]: ");
            String csIn = sc.nextLine().trim();
            if (!csIn.isEmpty()) {
                cs = Integer.parseInt(csIn);
            }
        } else if (inputChoice == 2) {
            q = 3;
            if (scheduler.usesQuantum()) {
                System.out.print("Quantum Q: ");
                q = Integer.parseInt(sc.nextLine().trim());
            }
            System.out.print("Context Switch CS: ");
            cs = Integer.parseInt(sc.nextLine().trim());
            System.out.print("Number of processes: ");
            int count = Integer.parseInt(sc.nextLine().trim());
            processes = new ArrayList<>(count);
            System.out.println("Enter each process as: <id> <arrival> <burst> <priority>");
            for (int i = 0; i < count; i++) {
                System.out.print("Process " + (i + 1) + ": ");
                String[] parts = sc.nextLine().trim().split("\\s+");
                if (parts.length != 4) {
                    throw new IllegalArgumentException("Expected 4 values: <id> <arrival> <burst> <priority>");
                }
                processes.add(new Process(
                        parts[0],
                        Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3])));
            }
        } else {
            throw new IllegalArgumentException("Invalid input source choice.");
        }

        System.out.println();
        SimulationResult result = scheduler.run(processes, q, cs);
        MatrixPrinter.print(result);
    }

    private static void printAvailableSchedulers() {
        System.out.println("Registered schedulers:");
        if (SCHEDULERS.isEmpty()) {
            System.out.println("  (none yet — expecting sjf.PreemptiveSJF and acs.ACSScheduler)");
            return;
        }
        for (Map.Entry<String, Scheduler> entry : SCHEDULERS.entrySet()) {
            System.out.printf("  %-6s -> %s%n", entry.getKey(), entry.getValue().getName());
        }
    }

    private static void printUsage() {
        System.out.println("Usage:");
        System.out.println("  java -cp out Main");
        System.out.println("  java -cp out Main --list");
        System.out.println("  java -cp out Main --scheduler <sjf|acs> --file <path> [--q <int>] [--cs <int>] [--out <path>]");
    }

    private static String requireArg(String[] args, int index, String flag) {
        if (index >= args.length) {
            throw new IllegalArgumentException("Missing value after " + flag);
        }
        return args[index];
    }
}
