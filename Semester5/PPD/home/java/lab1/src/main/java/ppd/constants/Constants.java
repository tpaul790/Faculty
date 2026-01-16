package ppd.constants;

public class Constants {
    public static String HOME_DIR = System.getProperty("user.dir");
    public static String INPUT_DIR = HOME_DIR + "/data/inputs/";
    public static String OUTPUT_DIR = HOME_DIR + "/data/outputs/";
    public static int[][] TEST_CASES = new int[][] {{10, 10, 3}, {1000, 1000, 5}, {10, 10000, 5}, {10000, 10, 5}, {10000, 10000, 5}};
    public static int[][] THREADS_COUNT = new int[][] {{4}, {2, 4, 8, 16}, {2, 4, 8, 16}, {2, 4, 8, 16}};
}