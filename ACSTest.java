import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class ACSTest {

    public static void runFile(String filePath) throws Exception {
        Scanner sc = new Scanner(new File(filePath));
        int quantum = Integer.parseInt(sc.nextLine().trim().split("=")[1].trim());
        int csCost = Integer.parseInt(sc.nextLine().trim().split("=")[1].trim());

        ArrayList<Process> processes = new ArrayList<>();
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty() || line.startsWith("#")) continue;
            String[] p = line.split("\\s+");
            processes.add(new Process(
                    p[0],
                    Integer.parseInt(p[1]),
                    Integer.parseInt(p[2]),
                    Integer.parseInt(p[3])
            ));
        }
        sc.close();

        System.out.println("==================================================");
        System.out.println("Running: " + filePath + " (Q=" + quantum + ", CS=" + csCost + ")");
        System.out.println("==================================================");

        ACSEngine engine = new ACSEngine(quantum, csCost);
        engine.run(processes);
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
