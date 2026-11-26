#### testB (Preemptive SJF, CS = 0)

| Process | Arrival | Burst |
|---|---|---|
| P1 | 0 | 4 |
| P2 | 0 | 5 |
| P3 | 1 | 5 |
| P4 | 2 | 5 |
| P5 | 3 | 5 |

| Time | CPU | Ready queue |
|---|---|---|
| 0 | P1 | [P2 (remaining: 5)] |
| 1 | P1 | [P2 (remaining: 5), P3 (remaining: 5)] |
| 2 | P1 | [P2 (remaining: 5), P3 (remaining: 5), P4 (remaining: 5)] |
| 3 | P1 | [P2 (remaining: 5), P3 (remaining: 5), P4 (remaining: 5), P5 (remaining: 5)] |
| 4-8 | P2 | [P3 (remaining: 5), P4 (remaining: 5), P5 (remaining: 5)] |
| 9-13 | P3 | [P4 (remaining: 5), P5 (remaining: 5)] |
| 14-18 | P4 | [P5 (remaining: 5)] |
| 19-23 | P5 | [Empty] |

| Process | Arrival | Burst | Finish | TAT | WT | RT |
|---|---|---|---|---|---|---|
| P1 | 0 | 4 | 4 | 4 | 0 | 0 |
| P2 | 0 | 5 | 9 | 9 | 4 | 4 |
| P3 | 1 | 5 | 14 | 13 | 8 | 8 |
| P4 | 2 | 5 | 19 | 17 | 12 | 12 |
| P5 | 3 | 5 | 24 | 21 | 16 | 16 |

Average turnaround time = 12.80, average waiting time = 8.00, average response time = 8.00, CPU utilization = 100.0% (24/24), context switches = 4

