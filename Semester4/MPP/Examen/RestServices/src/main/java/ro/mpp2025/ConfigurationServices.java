package ro.mpp2025;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.mpp2025.interfaces.IConfigurationRepo;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/ro/mpp/configurations")
public class ConfigurationServices {
    private final IConfigurationRepo configurationRepo;

    public ConfigurationServices(IConfigurationRepo configurationRepo) {
        this.configurationRepo = configurationRepo;
    }

    // ConfigurationServices.java
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateConfiguration(@PathVariable Integer id, @RequestBody AnimalsDTO animalsDTO) {
        System.out.println(">>> Received id=" + id + " animals=" + animalsDTO.getAnimals());

        Optional<Configuration> op = configurationRepo.findOne(id);
        if(op.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Configuration config = op.get();
        for(int i = 0; i < config.getAnimals().size(); i++)
        {
            Animal animal = config.getAnimals().get(i);
            animal.setName(animalsDTO.getAnimals().get(i));
        }
        configurationRepo.update(config);
        return ResponseEntity.ok().build();
    }

}
