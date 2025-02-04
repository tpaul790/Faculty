package org.example.template.Service;

import org.example.template.Repository.NevoiRepository;
import org.example.template.Repository.PersoaneRepository;

public class Service {
    private PersoaneRepository persoaneRepository;
    private NevoiRepository nevoiRepository;

    public Service(PersoaneRepository persoaneRepository, NevoiRepository nevoiRepository) {
        this.persoaneRepository = persoaneRepository;
        this.nevoiRepository = nevoiRepository;
    }


}
