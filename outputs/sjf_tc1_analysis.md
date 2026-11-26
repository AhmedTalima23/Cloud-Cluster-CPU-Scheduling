#### tc1 (Preemptive SJF, CS = 0)

| Process | Arrival | Burst |
|---|---|---|
| P1 | 0 | 4 |
| P2 | 1 | 7 |
| P3 | 3 | 2 |

| Time | CPU | Ready queue |
|---|---|---|
| 0 | P1 | [Empty] |
| 1-2 | P1 | [P2 (remaining: 7)] |
| 3 | P1 | [P3 (remaining: 2), P2 (remaining: 7)] |
| 4-5 | P3 | [P2 (remaining: 7)] |
| 6-12 | P2 | [Empty] |

| Process | Arrival | Burst | Finish | TAT | WT | RT |
|---|---|---|---|---|---|---|
| P1 | 0 | 4 | 4 | 4 | 0 | 0 |
| P2 | 1 | 7 | 13 | 12 | 5 | 5 |
| P3 | 3 | 2 | 6 | 3 | 1 | 1 |

Average turnaround time = 6.33, average waiting time = 2.00, average response time = 2.00, CPU utilization = 100.0% (13/13), context switches = 2

