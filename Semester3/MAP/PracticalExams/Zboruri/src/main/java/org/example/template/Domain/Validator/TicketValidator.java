package org.example.template.Domain.Validator;

import org.example.template.Domain.Ticket;

public class TicketValidator implements Validator<Ticket> {
    @Override
    public boolean validate(Ticket ticket) {
        boolean ok = true;
        if(ticket.getFlightId()<1)
            ok = false;
        if(ticket.getUsername().isEmpty())
            ok = false;
        return ok;
    }
}
