package org.example.template.Domain.Validator;

import org.example.template.Domain.Flight;

public class FlightValidator implements Validator<Flight> {
    @Override
    public boolean validate(Flight flight) {
        boolean ok = true;
        if(flight.getFlightId()<0)
            ok = false;
        if(flight.getFrom().isEmpty() || flight.getTo().isEmpty())
            ok = false;
        return ok;
    }
}
