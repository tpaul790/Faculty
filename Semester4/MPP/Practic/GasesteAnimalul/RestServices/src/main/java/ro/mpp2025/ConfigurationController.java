package ro.mpp2025;

import org.springframework.web.bind.annotation.*;
import ro.mpp2025.interfaces.IConfigurationRepo;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/ro/mpp/configurations")
public class ConfigurationController {
    private IConfigurationRepo repo;

    public ConfigurationController(IConfigurationRepo repo) {
        this.repo = repo;
    }

    @RequestMapping(method = RequestMethod.POST)
    public void saveConfiguration(@RequestBody Configuration config) {
        repo.save(config);
    }
}
