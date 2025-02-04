package org.example.template.Domain.Validator;

import org.example.template.Domain.Client;

public class ClientValidator implements Validator<Client> {
    @Override
    public boolean validate(Client client) {
        boolean ok = true;
        if(client.getName().isEmpty())
            ok = false;
        if (client.getUsername().isEmpty())
            ok = false;
        return ok;
    }
}
