package ppd.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestScenarioConfig {
    public static int CLIENTS_COUNT = 20;
    public static int TEST_DURATION_SEC = 120;
    public static int CLIENT_DELAY_MS = 2000;
    public static int BATCH_SIZE = 5;
    public static String WORKSHOPS_FILE = "src/main/resources/workshops.csv";
    public static String CAPACITIES_FILE = "src/main/resources/capacities.csv";
    public static int PORT = 9000;
    public static int THREAD_POOL_SIZE = 8;
    public static int CHECK_INTERVAL_SEC = 10;

    public static void initialize(String filename, Boolean onServer) {
        System.out.println("Loading test scenario from: " + filename);

        try (InputStream input = TestScenarioConfig.class.getClassLoader().getResourceAsStream(filename)) {
            if (input == null) {
                System.out.println("Sorry, unable to find " + filename + ". Using defaults.");
                return;
            }

            Properties prop = new Properties();
            prop.load(input);

            CLIENTS_COUNT = Integer.parseInt(prop.getProperty("clients_count", "20"));
            TEST_DURATION_SEC = Integer.parseInt(prop.getProperty("test_duration_sec", "120"));
            CLIENT_DELAY_MS = Integer.parseInt(prop.getProperty("client_delay_ms", "2000"));
            BATCH_SIZE = Integer.parseInt(prop.getProperty("batch_size", "5"));
            WORKSHOPS_FILE = prop.getProperty("workshops_file", "src/main/resources/workshops.csv");
            CAPACITIES_FILE = prop.getProperty("capacities_file", "src/main/resources/capacities.csv");
            PORT = Integer.parseInt(prop.getProperty("port", "9000"));
            THREAD_POOL_SIZE = Integer.parseInt(prop.getProperty("thread_pool_size", "8"));
            CHECK_INTERVAL_SEC = Integer.parseInt(prop.getProperty("check_interval_sec", "10"));

            if(!onServer) {
                System.out.println("Scenario Loaded: " + CLIENTS_COUNT + " clients, "
                        + TEST_DURATION_SEC + "s duration, "
                        + CLIENT_DELAY_MS + "ms delay.");
            }

        } catch (IOException | NumberFormatException ex) {
            ex.printStackTrace();
        }
    }
}
