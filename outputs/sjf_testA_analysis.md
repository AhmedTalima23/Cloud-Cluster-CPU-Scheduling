#### testA (Preemptive SJF, CS = 1)

| Process | Arrival | Burst |
|---|---|---|
| P1 | 0 | 9 |
| P2 | 1 | 2 |
| P3 | 2 | 1 |
| P4 | 4 | 6 |

| Time | CPU | Ready queue |
|---|---|---|
| 0 | P1 | [Empty] |
| 1 | CS: P1 -> P2 | [P1 (remaining: 8)] |
| 2-3 | P2 | [P3 (remaining: 1), P1 (remaining: 8)] |
| 4 | CS: P2 -> P3 | [P4 (remaining: 6), P1 (remaining: 8)] |
| 5 | P3 | [P4 (remaining: 6), P1 (remaining: 8)] |
| 6 | CS: P3 -> P4 | [P1 (remaining: 8)] |
| 7-12 | P4 | [P1 (remaining: 8)] |
| 13 | CS: P4 -> P1 | [Empty] |
| 14-21 | P1 | [Empty] |

| Process | Arrival | Burst | Finish | TAT | WT | RT |
|---|---|---|---|---|---|---|
| P1 | 0 | 9 | 22 | 22 | 13 | 0 |
| P2 | 1 | 2 | 4 | 3 | 1 | 1 |
| P3 | 2 | 1 | 6 | 4 | 3 | 3 |
| P4 | 4 | 6 | 13 | 9 | 3 | 3 |

Average turnaround time = 9.50, average waiting time = 5.00, average response time = 1.75, CPU utilization = 81.8% (18/22), context switches = 4

