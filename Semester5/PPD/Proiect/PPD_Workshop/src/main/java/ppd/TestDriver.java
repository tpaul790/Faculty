package ppd;

import ppd.config.TestScenarioConfig;
import ppd.config.WorkshopConfig;
import ppd.client.WorkshopClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestDriver {
    public static void main(String[] args) {
        System.out.println("Starting Test Driver...");

        String testScenarioFile = "test_scenario3.properties";

        TestScenarioConfig.initialize(testScenarioFile, false);

        WorkshopConfig.initialize(TestScenarioConfig.WORKSHOPS_FILE, TestScenarioConfig.CAPACITIES_FILE);


        ExecutorService clientPool = Executors.newFixedThreadPool(TestScenarioConfig.CLIENTS_COUNT);

        long startTime = System.currentTimeMillis();
        long duration = TestScenarioConfig.TEST_DURATION_SEC * 1000L;

        int clientCount = 1;

        while (System.currentTimeMillis() - startTime < duration) {

            // Throw a clients batch
            for(int i = 0; i < TestScenarioConfig.BATCH_SIZE; i++) {
                String name = "Client_" + clientCount;
                String cnp = "CNP" + (10000 + clientCount);
                clientCount++;

                clientPool.submit(new WorkshopClient(name, cnp));
            }

            try {
                // Every CLIENT_DELAY_MS s, a client will send a request
                Thread.sleep(TestScenarioConfig.CLIENT_DELAY_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        clientPool.shutdown();
        System.out.println("Test Driver finished sending requests after " + TestScenarioConfig.TEST_DURATION_SEC + " seconds.");
    }
}