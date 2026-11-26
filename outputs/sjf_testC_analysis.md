#### testC (Preemptive SJF, CS = 1)

| Process | Arrival | Burst |
|---|---|---|
| P1 | 0 | 7 |
| P2 | 3 | 3 |
| P3 | 6 | 2 |

| Time | CPU | Ready queue |
|---|---|---|
| 0-2 | P1 | [Empty] |
| 3 | CS: P1 -> P2 | [P1 (remaining: 4)] |
| 4-5 | P2 | [P1 (remaining: 4)] |
| 6 | P2 | [P3 (remaining: 2), P1 (remaining: 4)] |
| 7 | CS: P2 -> P3 | [P1 (remaining: 4)] |
| 8-9 | P3 | [P1 (remaining: 4)] |
| 10 | CS: P3 -> P1 | [Empty] |
| 11-14 | P1 | [Empty] |

| Process | Arrival | Burst | Finish | TAT | WT | RT |
|---|---|---|---|---|---|---|
| P1 | 0 | 7 | 15 | 15 | 8 | 0 |
| P2 | 3 | 3 | 7 | 4 | 1 | 1 |
| P3 | 6 | 2 | 10 | 4 | 2 | 2 |

Average turnaround time = 7.67, average waiting time = 3.67, average response time = 1.00, CPU utilization = 80.0% (12/15), context switches = 3

