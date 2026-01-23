package ppd.config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class WorkshopConfig {
    public static int NUM_CENTRES = 0;
    public static int NUM_WORKSHOPS = 0;

    //Map<Center, Map<Workshop, Capacity>>
    private static final Map<Integer, Map<Integer, Integer>> capacities = new HashMap<>();
    //Map<Workshop, Cost>
    private static final Map<Integer, Double> costs = new HashMap<>();
    //Map<Workshop, Duration>
    private static final Map<Integer, Integer> durations = new HashMap<>();
    //Map<Workshop, Workshop Name>
    private static final Map<Integer, String> names = new HashMap<>();

    public static void initialize(String workshopsFile, String capacitiesFile) {
        System.out.println("Loading config...");
        capacities.clear();
        costs.clear();
        durations.clear();
        names.clear();
        NUM_CENTRES = 0;
        NUM_WORKSHOPS = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(workshopsFile))) {
            String line;
            boolean header = true;
            while ((line = br.readLine()) != null) {
                if (header) { header = false; continue; }
                if (line.trim().isEmpty()) continue;

                String[] p = line.split(",");
                int id = Integer.parseInt(p[0].trim());
                String nume = p[1].trim();
                double cost = Double.parseDouble(p[2].trim());
                int durata = Integer.parseInt(p[3].trim());

                costs.put(id, cost);
                durations.put(id, durata);
                names.put(id, nume);

                if (id > NUM_WORKSHOPS) NUM_WORKSHOPS = id;
            }
        } catch (Exception e) {
            throw new RuntimeException("Eroare la citire workshops.csv", e);
        }

        try (BufferedReader br = new BufferedReader(new FileReader(capacitiesFile))) {
            String line;
            boolean header = true;
            while ((line = br.readLine()) != null) {
                if (header) { header = false; continue; }
                if (line.trim().isEmpty()) continue;

                String[] p = line.split(",");
                int cid = Integer.parseInt(p[0].trim());
                int wid = Integer.parseInt(p[1].trim());
                int cap = Integer.parseInt(p[2].trim());

                capacities.putIfAbsent(cid, new HashMap<>());
                capacities.get(cid).put(wid, cap);

                if (cid > NUM_CENTRES) NUM_CENTRES = cid;
            }
        } catch (Exception e) {
            throw new RuntimeException("Eroare la citire capacities.csv", e);
        }

        System.out.println("Config loaded: " + NUM_CENTRES + " centers, " + NUM_WORKSHOPS + " workshops.");
    }

    public static int getCapacity(int centerId, int workshopId) {
        if (capacities.containsKey(centerId) && capacities.get(centerId).containsKey(workshopId)) {
            return capacities.get(centerId).get(workshopId);
        }
        return 0;
    }

    public static double getCost(int workshopId) {
        return costs.getOrDefault(workshopId, 0.0);
    }

    public static int getDurationMinutes(int workshopId) {
        return durations.getOrDefault(workshopId, 60); // Default 60 min
    }
}