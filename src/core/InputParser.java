package core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class InputParser {

    public static class TestCase {
        private final String sourceName;
        private final int quantum;
        private final int contextSwitch;
        private final List<Process> processes;

        public TestCase(String sourceName, int quantum, int contextSwitch, List<Process> processes) {
            this.sourceName = sourceName;
            this.quantum = quantum;
            this.contextSwitch = contextSwitch;
            this.processes = Collections.unmodifiableList(new ArrayList<>(processes));
        }

        public String getSourceName() {
            return sourceName;
        }

        public int getQuantum() {
            return quantum;
        }

        public int getContextSwitch() {
            return contextSwitch;
        }

        public List<Process> getProcessesCopy() {
            List<Process> copy = new ArrayList<>(processes.size());
            for (Process p : processes) {
                copy.add(p.freshCopy());
            }
            return copy;
        }

        public List<Process> getProcesses() {
            return processes;
        }
    }

    private InputParser() {
    }

    public static TestCase parseFile(String filePath) throws IOException {
        return parseFile(Path.of(filePath));
    }

    public static TestCase parseFile(Path path) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
            String raw;
            while ((raw = reader.readLine()) != null) {
                String line = stripComment(raw).trim();
                if (!line.isEmpty()) {
                    lines.add(line);
                }
            }
        }
        return parseLines(path.getFileName().toString(), lines);
    }

    public static TestCase parseLines(String sourceName, List<String> lines) {
        Integer quantum = null;
        Integer cs = null;
        List<Process> processes = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            String line = stripComment(lines.get(i)).trim();
            if (line.isEmpty()) {
                continue;
            }

            if (line.contains("=")) {
                String[] kv = line.split("=", 2);
                String key = kv[0].trim().toUpperCase();
                int val = Integer.parseInt(kv[1].trim());
                if ("Q".equals(key) || "QUANTUM".equals(key)) {
                    quantum = val;
                } else if ("CS".equals(key) || "CONTEXT_SWITCH".equals(key)) {
                    cs = val;
                } else {
                    throw new IllegalArgumentException("Unknown header key on line " + (i + 1) + ": " + key);
                }
                continue;
            }

            String[] tokens = line.split("\\s+");

            if (quantum == null && cs == null && tokens.length == 2) {
                quantum = Integer.parseInt(tokens[0]);
                cs = Integer.parseInt(tokens[1]);
                continue;
            }

            if (tokens.length == 4) {
                String id = tokens[0];
                int arrival = Integer.parseInt(tokens[1]);
                int burst = Integer.parseInt(tokens[2]);
                int priority = Integer.parseInt(tokens[3]);
                processes.add(new Process(id, arrival, burst, priority));
                continue;
            }

            throw new IllegalArgumentException("Malformed line in " + sourceName + ": \"" + line + "\"");
        }

        if (quantum == null || cs == null) {
            throw new IllegalArgumentException("Missing Q or CS header in " + sourceName);
        }
        if (quantum <= 0) {
            throw new IllegalArgumentException("Quantum must be positive: " + quantum);
        }
        if (cs < 0) {
            throw new IllegalArgumentException("Context switch cost cannot be negative: " + cs);
        }
        if (processes.isEmpty()) {
            throw new IllegalArgumentException("No processes found in " + sourceName);
        }

        return new TestCase(sourceName, quantum, cs, processes);
    }

    private static String stripComment(String line) {
        int hashIdx = line.indexOf('#');
        return hashIdx >= 0 ? line.substring(0, hashIdx) : line;
    }
}
