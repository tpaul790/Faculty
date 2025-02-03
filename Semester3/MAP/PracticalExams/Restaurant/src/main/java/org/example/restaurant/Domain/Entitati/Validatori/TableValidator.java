package org.example.restaurant.Domain.Entitati.Validatori;

import org.example.restaurant.Domain.Entitati.Order;
import org.example.restaurant.Domain.Entitati.Table;

public class TableValidator {
    private static TableValidator instance = new TableValidator();

    private TableValidator() {}

    public static TableValidator getInstance() {
        if(instance == null) {
            instance = new TableValidator();
        }
        return instance;
    }

    public boolean validate(Table tab) {
        return tab.getId() >= 1;
    }
}
