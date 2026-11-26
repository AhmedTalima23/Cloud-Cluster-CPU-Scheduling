# Cloud-Cluster CPU Scheduling Simulator

CS241: Operating Systems – 1 (Fall 2026) — Assignment 2  
Faculty of Computers and Artificial Intelligence, Cairo University

Java simulator for **Preemptive Shortest-Job First (SJF)** and the **Adaptive Container Scheduler (ACS)** (Tier 1 Round Robin, Tier 2 Non-Preemptive Priority, Tier 3 Aging), with context-switch accounting, per-step heuristic demotion matrices, metrics calculation, parameter sweeps, and a Swing GUI.

---

## Requirements

- **Java**: JDK 17+ (standard JDK only; no external libraries required)

---

## Project Structure

```text
.
├── README.md
├── src/
│   ├── Main.java
│   ├── core/
│   │   ├── Process.java
│   │   ├── Scheduler.java
│   │   ├── Snapshot.java
│   │   ├── SimulationResult.java
│   │   ├── MetricsCalculator.java
│   │   ├── MatrixPrinter.java
│   │   └── InputParser.java
│   ├── acs/
│   │   ├── ACSScheduler.java
│   │   ├── RoundRobinTier.java
│   │   ├── Tier2Queue.java
│   │   ├── Tier3Aging.java
│   │   ├── ACSTest.java
│   │   └── AgingTest.java
│   ├── sjf/
│   │   ├── PreemptiveSJF.java
│   │   └── SjfTest.java
│   ├── experiments/
│   │   ├── SweepRunner.java
│   │   ├── AnalysisWriter.java
│   │   └── ChartWriter.java
│   └── gui/
│       ├── SimulatorGUI.java
│       ├── GanttPanel.java
│       ├── TierMatrixPanel.java
│       ├── MetricsPanel.java
│       └── GuiModels.java
├── tests/
├── outputs/
└── report/
    ├── Report.pdf
    ├── data/sweep.csv
    ├── graphs/
    └── screenshots/
```

---

## Compile & Run

### 1. Compile all source files

```bash
javac -d out src/core/*.java src/acs/*.java src/sjf/*.java src/experiments/*.java src/gui/*.java src/Main.java
```

### 2. Run via CLI or Interactive Menu

```bash
java -cp out Main
java -cp out Main --list
java -cp out Main --scheduler acs --file tests/tc1.txt
java -cp out Main --scheduler acs --file tests/tc2.txt --q 3 --cs 1
java -cp out Main --scheduler sjf --file tests/tc1.txt
```

### 3. Launch the Swing GUI (Bonus)

```bash
java -cp out Main --gui
```
or:
```bash
java -cp out gui.SimulatorGUI
```

### 4. Run Tests, Sweeps & Analysis Generators

```bash
java -cp out acs.AgingTest
java -cp out acs.ACSTest
java -cp out sjf.SjfTest
java -cp out experiments.SweepRunner
java -cp out experiments.AnalysisWriter
```

---

## Input File Format

`core.InputParser` accepts both header formats (blank lines and `#` comments are ignored):

```text
Q=3
CS=1
P1 0 5 4
P2 1 6 2
P3 4 4 3
```
or:
```text
3 1
P1 0 5 4
P2 1 6 2
P3 4 4 3
```

Each process line is: `<id> <arrivalTime> <burstTime> <initialPriority>`.
