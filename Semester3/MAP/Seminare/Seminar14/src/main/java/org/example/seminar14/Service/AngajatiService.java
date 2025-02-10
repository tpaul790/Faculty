package org.example.seminar14.Service;

import org.example.seminar14.Domain.Angajat;
import org.example.seminar14.Repo.AngajatRepo;

import java.util.List;

public class AngajatiService {
    private AngajatRepo angajatRepo;

    public AngajatiService(AngajatRepo angajatRepo) {
        this.angajatRepo = angajatRepo;
    }

    public List<Angajat> getAllAngajati() {
        return angajatRepo.findAll();
    }

    public Iterable<Angajat> filtrare(){
        List<Angajat> angajati = angajatRepo.findAll();
        return null;
    }
}
