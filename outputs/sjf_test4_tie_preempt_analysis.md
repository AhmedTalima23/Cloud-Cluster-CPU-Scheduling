#### sjf_test4_tie_preempt (Preemptive SJF, CS = 1)

| Process | Arrival | Burst |
|---|---|---|
| P1 | 0 | 6 |
| P2 | 2 | 4 |
| P3 | 3 | 1 |
| P4 | 3 | 4 |

| Time | CPU | Ready queue |
|---|---|---|
| 0-1 | P1 | [Empty] |
| 2 | P1 | [P2 (remaining: 4)] |
| 3 | CS: P1 -> P3 | [P1 (remaining: 3), P2 (remaining: 4), P4 (remaining: 4)] |
| 4 | P3 | [P1 (remaining: 3), P2 (remaining: 4), P4 (remaining: 4)] |
| 5 | CS: P3 -> P1 | [P2 (remaining: 4), P4 (remaining: 4)] |
| 6-8 | P1 | [P2 (remaining: 4), P4 (remaining: 4)] |
| 9 | CS: P1 -> P2 | [P4 (remaining: 4)] |
| 10-13 | P2 | [P4 (remaining: 4)] |
| 14 | CS: P2 -> P4 | [Empty] |
| 15-18 | P4 | [Empty] |

| Process | Arrival | Burst | Finish | TAT | WT | RT |
|---|---|---|---|---|---|---|
| P1 | 0 | 6 | 9 | 9 | 3 | 0 |
| P2 | 2 | 4 | 14 | 12 | 8 | 8 |
| P3 | 3 | 1 | 5 | 2 | 1 | 1 |
| P4 | 3 | 4 | 19 | 16 | 12 | 12 |

Average turnaround time = 9.75, average waiting time = 6.00, average response time = 5.25, CPU utilization = 78.9% (15/19), context switches = 4

