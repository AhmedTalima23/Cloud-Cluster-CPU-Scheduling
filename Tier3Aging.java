
public class Tier3Aging {

    public static final int AGING_INTERVAL = 4;

    public static int effectivePriority(int basePriority, int waitedTime) {

        if (waitedTime < 0) {
            throw new IllegalArgumentException("Waiting time cannot be negative");
        }

        return basePriority - waitedTime / AGING_INTERVAL;
    }
}
