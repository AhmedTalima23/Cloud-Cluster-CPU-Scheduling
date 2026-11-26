#### tc2 (Preemptive SJF, CS = 1)

| Process | Arrival | Burst |
|---|---|---|
| P1 | 0 | 5 |
| P2 | 1 | 6 |
| P3 | 4 | 4 |

| Time | CPU | Ready queue |
|---|---|---|
| 0 | P1 | [Empty] |
| 1-3 | P1 | [P2 (remaining: 6)] |
| 4 | P1 | [P3 (remaining: 4), P2 (remaining: 6)] |
| 5 | CS: P1 -> P3 | [P2 (remaining: 6)] |
| 6-9 | P3 | [P2 (remaining: 6)] |
| 10 | CS: P3 -> P2 | [Empty] |
| 11-16 | P2 | [Empty] |

| Process | Arrival | Burst | Finish | TAT | WT | RT |
|---|---|---|---|---|---|---|
| P1 | 0 | 5 | 5 | 5 | 0 | 0 |
| P2 | 1 | 6 | 17 | 16 | 10 | 10 |
| P3 | 4 | 4 | 10 | 6 | 2 | 2 |

Average turnaround time = 9.00, average waiting time = 4.00, average response time = 4.00, CPU utilization = 88.2% (15/17), context switches = 2

