package ppd.utils;

public record Constants() {
    public static String HOME_DIR = System.getProperty("user.dir");
    public static String INPUT_DIR = HOME_DIR + "/data/inputs/";
    public static String OUTPUT_DIR = HOME_DIR + "/data/outputs/";
    public static int[][] TEST_CASES = new int[][] {{10, 10, 3}, {1000, 1000, 3}, {10000, 10000, 3}};
    public static int[][] THREADS_COUNT = new int[][] {{2}, {2, 4, 8, 16}, {2, 4, 8, 16}};
}
