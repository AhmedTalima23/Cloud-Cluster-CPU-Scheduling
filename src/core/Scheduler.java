package core;

import java.util.List;

public interface Scheduler {

    String getName();

    default boolean usesQuantum() {
        return true;
    }

    SimulationResult run(List<Process> processes, int quantum, int contextSwitch);
}
