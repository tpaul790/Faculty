package ro.mpp2025;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ro.mpp2025.interfaces.IConfigurationRepo;
import ro.mpp2025.interfaces.IPlayerRepo;

import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        Player player = new Player("paul");

        IPlayerRepo repo = new PlayerRepo();
        IConfigurationRepo configRepo = new ConfigurationRepo();
        Animal a1 = new Animal("oaie");
        Animal a2 = new Animal("gaina");
        Animal a3 = new Animal("porc");
        Animal a4 = new Animal("cal");
        Animal a5 = new Animal("rata");
        Animal a6 = new Animal("gasca");
        Animal a7 = new Animal("caine");
        Animal a8 = new Animal("pisica");

        Configuration configuration = new Configuration();
        configuration.addToAnimals(a1);
        configuration.addToAnimals(a2);
        configuration.addToAnimals(a3);
        configuration.addToAnimals(a4);
        configuration.addToAnimals(a5);
        configuration.addToAnimals(a1);
        configuration.addToAnimals(a2);
        configuration.addToAnimals(a3);
        configuration.addToAnimals(a4);
        configuration.addToAnimals(a5);

        configRepo.save(configuration);


//        repo.save(player);



        repo.findBy("paul");
    }
}