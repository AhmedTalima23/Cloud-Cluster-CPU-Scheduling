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
├── README.md                          # Build/run guide, conventions, ownership [Student 1]
├── src/
│   ├── Main.java                      # Console CLI & interactive entry point [Student 1]
│   ├── core/                          # Shared framework [Student 1]
│   │   ├── Process.java
│   │   ├── Scheduler.java
│   │   ├── Snapshot.java
│   │   ├── SimulationResult.java
│   │   ├── MetricsCalculator.java
│   │   ├── MatrixPrinter.java
│   │   └── InputParser.java
│   ├── sjf/                           # Preemptive SJF [Student 2]
│   ├── experiments/                   # CS & Q sweep runner [Student 2]
│   ├── acs/                           # ACS engine & Tiers 1–3 [Students 3 & 4]
│   └── gui/                           # Swing GUI (bonus) [Student 5]
├── tests/ (or testcases/)             # Input test-case files [Students 2 & 3]
├── outputs/                           # Saved scheduling analysis tables [Students 2 & 3]
└── report/
    ├── Report.pdf                     # Final assembled report [Student 1]
    ├── graphs/                        # Sweep plots [Student 2]
    ├── data/                          # Sweep CSVs [Student 2]
    └── screenshots/                   # GUI screenshots [Student 5]
```

> **Note:** Early standalone ACS/Tier 2/Tier 3 prototypes (`ACSEngine.java`, `RoundRobinTier.java`, `Tier2Queue.java`, `Tier3Aging.java`, `Process.java`, `ACSTest.java`, `AgingTest.java`) currently sit in the repository root and are scheduled to be moved into `src/acs/` and adapted to `core.Scheduler`.

---

## Compile & Run

### 1. Core framework & Main (`src/`)

```bash
javac -d out src/core/*.java src/Main.java
```

List registered schedulers:

```bash
java -cp out Main --list
```

Run in CLI mode (once `sjf.PreemptiveSJF` and/or `acs.ACSScheduler` are integrated into `src/`):

```bash
java -cp out Main --scheduler acs --file tests/tc1.txt
java -cp out Main --scheduler acs --file tests/tc2.txt --q 3 --cs 1 --out outputs/acs_tc02_analysis.txt
java -cp out Main --scheduler sjf --file testcases/sjf_tc01.txt --cs 1
```

Or run in interactive menu mode:

```bash
java -cp out Main
```

### 2. Standalone ACS & Aging prototypes (root directory)

```bash
javac -d out Process.java RoundRobinTier.java Tier2Queue.java Tier3Aging.java ACSEngine.java ACSTest.java AgingTest.java
java -cp out AgingTest
java -cp out ACSTest
```

---

## Input File Format

`core.InputParser` accepts **both** header formats (blank lines and `#` comments are ignored):

**Format A:**
```text
3 0
P1 0 4 2
P2 1 7 3
P3 3 2 1
```

**Format B (used in `tests/*.txt`):**
```text
Q=3
CS=0
P1 0 4 2
P2 1 7 3
P3 3 2 1
```

Each process line is: `<id> <arrivalTime> <burstTime> <initialPriority>`.

---

## Agreed Simulation Conventions

- **Priority order:** Lower priority number = higher priority.
- **Context Switch (CS):** Charged whenever the CPU switches to a different process (including after a process finishes), but **not** at `t = 0`.
- **Exact quantum finish:** A process whose remaining burst reaches `0` at the end of its Tier 1 quantum finishes immediately and is **not** demoted to Tier 2.
- **Tier 1 precedence:** Tier 1 always has priority over Tier 2, except that a process already running in Tier 2 is **non-preemptive** and runs to completion.
- **Tier 3 Aging:** `effectivePriority = basePriority - floor(timeWaitedInTier2 / 4)` (`AGING_INTERVAL = 4`).
- **Metrics:**
  - `Turnaround Time (TAT) = Finish Time - Arrival Time`
  - `Waiting Time (WT) = TAT - Burst Time` (CS time counts as waiting)
  - `Response Time (RT) = First CPU Start Time - Arrival Time`
  - `CPU Utilization = (Total Busy Time / Total Simulation Time) * 100%`

---

## Team Ownership

| Student | Role | Owned Modules |
| :--- | :--- | :--- |
| Student 1 | Team Lead, Core & Integration | `src/core/*`, `src/Main.java`, `README.md`, final `Report.pdf` & ZIP |
| Student 2 | Preemptive SJF & Experiments | `src/sjf/PreemptiveSJF.java`, `src/experiments/SweepRunner.java`, SJF tests, CSVs & graphs |
| Student 3 | ACS Engine & Tier 1 | `src/acs/ACSScheduler.java`, `src/acs/Tier1RoundRobin.java`, ACS tests & analysis tables |
| Student 4 | ACS Tier 2, Tier 3 & Trade-offs | `src/acs/Tier2PriorityQueue.java`, `src/acs/AgingPolicy.java`, aging tests, Tier 2 trade-off analysis |
| Student 5 | GUI (Bonus) & Verification | `src/gui/*`, GUI screenshots, hand-verification notes |
