#### sjf_test2_starvation (Preemptive SJF, CS = 1)

| Process | Arrival | Burst |
|---|---|---|
| P1 | 0 | 12 |
| P2 | 1 | 2 |
| P3 | 4 | 2 |
| P4 | 7 | 2 |
| P5 | 10 | 2 |
| P6 | 13 | 3 |

| Time | CPU | Ready queue |
|---|---|---|
| 0 | P1 | [Empty] |
| 1 | CS: P1 -> P2 | [P1 (remaining: 11)] |
| 2-3 | P2 | [P1 (remaining: 11)] |
| 4 | CS: P2 -> P3 | [P1 (remaining: 11)] |
| 5-6 | P3 | [P1 (remaining: 11)] |
| 7 | CS: P3 -> P4 | [P1 (remaining: 11)] |
| 8-9 | P4 | [P1 (remaining: 11)] |
| 10 | CS: P4 -> P5 | [P1 (remaining: 11)] |
| 11-12 | P5 | [P1 (remaining: 11)] |
| 13 | CS: P5 -> P6 | [P1 (remaining: 11)] |
| 14-16 | P6 | [P1 (remaining: 11)] |
| 17 | CS: P6 -> P1 | [Empty] |
| 18-28 | P1 | [Empty] |

| Process | Arrival | Burst | Finish | TAT | WT | RT |
|---|---|---|---|---|---|---|
| P1 | 0 | 12 | 29 | 29 | 17 | 0 |
| P2 | 1 | 2 | 4 | 3 | 1 | 1 |
| P3 | 4 | 2 | 7 | 3 | 1 | 1 |
| P4 | 7 | 2 | 10 | 3 | 1 | 1 |
| P5 | 10 | 2 | 13 | 3 | 1 | 1 |
| P6 | 13 | 3 | 17 | 4 | 1 | 1 |

Average turnaround time = 7.50, average waiting time = 3.67, average response time = 0.83, CPU utilization = 79.3% (23/29), context switches = 6

