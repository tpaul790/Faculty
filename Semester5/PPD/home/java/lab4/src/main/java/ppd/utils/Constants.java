package ppd.utils;

public record Constants() {
    public static String HOME_DIR = System.getProperty("user.dir") + "/src/main/java/ppd";
//    public static String HOME_DIR = System.getProperty("user.dir");
    public static String INPUT_DIR = HOME_DIR + "/data/inputs";
    public static String OUTPUT_DIR = HOME_DIR + "/data/outputs";

    public static final int PROJECTS = 10;
    public static final int MIN_PARTICIPANTS = 80;
    public static final int MAX_PARTICIPANTS = 200;
}
