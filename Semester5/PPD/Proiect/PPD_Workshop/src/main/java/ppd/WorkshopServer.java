package ppd;

import ppd.config.TestScenarioConfig;
import ppd.config.WorkshopConfig;
import ppd.client.ClientHandler;
import ppd.data.Database;
import ppd.utils.PeriodicChecker;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WorkshopServer {
    public static void main(String[] args) {
        System.out.println("Initializing Workshop Server...");

        String testScenarioFile = "test_scenario2.properties";

        TestScenarioConfig.initialize(testScenarioFile, true);

        WorkshopConfig.initialize(TestScenarioConfig.WORKSHOPS_FILE, TestScenarioConfig.CAPACITIES_FILE);

        Database.cleanUpDatabase();

        try {
            new PrintWriter("verification_report.txt").close();
            System.out.println("Log file 'verification_report.txt' cleared.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ExecutorService requestPool = Executors.newFixedThreadPool(TestScenarioConfig.THREAD_POOL_SIZE);

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new PeriodicChecker(),
                TestScenarioConfig.CHECK_INTERVAL_SEC, TestScenarioConfig.CHECK_INTERVAL_SEC, TimeUnit.SECONDS);

        System.out.println("Server started on port " + TestScenarioConfig.PORT);
        System.out.println("Periodic check set to " + TestScenarioConfig.CHECK_INTERVAL_SEC + " seconds.");
        System.out.println("Connected to PostgreSQL database via Database class.");

        try (ServerSocket serverSocket = new ServerSocket(TestScenarioConfig.PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();

                requestPool.submit(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            requestPool.shutdown();
            scheduler.shutdown();
        }
    }
}