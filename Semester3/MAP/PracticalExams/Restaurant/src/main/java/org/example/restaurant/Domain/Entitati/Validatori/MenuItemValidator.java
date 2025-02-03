package org.example.restaurant.Domain.Entitati.Validatori;

import org.example.restaurant.Domain.Entitati.MenuItem;

public class MenuItemValidator {
    private static MenuItemValidator instance = new MenuItemValidator();

    private MenuItemValidator() {}

    public static MenuItemValidator getInstance() {
        if(instance == null) {
            instance = new MenuItemValidator();
        }
        return instance;
    }

    public boolean validate(MenuItem menuItem) {
        boolean valid = true;
        if(menuItem.getId() < 1)
            valid = false;
        if(menuItem.getItem().isEmpty())
            valid = false;
        if(menuItem.getCategory().isEmpty())
            valid = false;
        if(menuItem.getPrice() < 0)
            valid = false;
        if(menuItem.getCurrency().isEmpty())
            valid = false;
        return valid;
    }
}
