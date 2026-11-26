#### sjf_test3 (Preemptive SJF, CS = 2)

| Process | Arrival | Burst |
|---|---|---|
| P1 | 0 | 3 |
| P2 | 0 | 3 |
| P3 | 0 | 2 |
| P4 | 15 | 4 |
| P5 | 15 | 1 |

| Time | CPU | Ready queue |
|---|---|---|
| 0-1 | P3 | [P1 (remaining: 3), P2 (remaining: 3)] |
| 2-3 | CS: P3 -> P1 | [P2 (remaining: 3)] |
| 4-6 | P1 | [P2 (remaining: 3)] |
| 7-8 | CS: P1 -> P2 | [Empty] |
| 9-11 | P2 | [Empty] |
| 12-14 | IDLE | [Empty] |
| 15 | P5 | [P4 (remaining: 4)] |
| 16-17 | CS: P5 -> P4 | [Empty] |
| 18-21 | P4 | [Empty] |

| Process | Arrival | Burst | Finish | TAT | WT | RT |
|---|---|---|---|---|---|---|
| P1 | 0 | 3 | 7 | 7 | 4 | 4 |
| P2 | 0 | 3 | 12 | 12 | 9 | 9 |
| P3 | 0 | 2 | 2 | 2 | 0 | 0 |
| P4 | 15 | 4 | 22 | 7 | 3 | 3 |
| P5 | 15 | 1 | 16 | 1 | 0 | 0 |

Average turnaround time = 5.80, average waiting time = 3.20, average response time = 3.20, CPU utilization = 59.1% (13/22), context switches = 3

