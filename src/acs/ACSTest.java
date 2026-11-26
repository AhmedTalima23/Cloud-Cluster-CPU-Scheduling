package acs;

import core.InputParser;
import core.MatrixPrinter;
import core.SimulationResult;

public class ACSTest {

    public static void runFile(String filePath) throws Exception {
        InputParser.TestCase tc = InputParser.parseFile(filePath);
        ACSScheduler scheduler = new ACSScheduler();
        SimulationResult result = scheduler.run(tc.getProcessesCopy(), tc.getQuantum(), tc.getContextSwitch());
        System.out.println("File: " + filePath);
        MatrixPrinter.print(result);
        System.out.println();
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 1) {
            runFile(args[0]);
            return;
        }

        String[] files = {
            "tests/tc1.txt",
            "tests/tc2.txt",
            "tests/testA.txt",
            "tests/testB.txt",
            "tests/testC.txt"
        };

        for (String file : files) {
            runFile(file);
        }
    }
}
