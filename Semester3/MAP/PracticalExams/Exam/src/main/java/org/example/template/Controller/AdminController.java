package org.example.template.Controller;

import javafx.event.ActionEvent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.example.template.Domain.Show;
import org.example.template.Service.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AdminController {
    private Service service;
    public TextField hourTxt;
    public TextField nameTxt;
    public TextField durationTxt;
    public TextField seatsTxt;
    public DatePicker startDate;

    public void setService(Service service) {
        this.service = service;
        afterService();
    }

    public void afterService() {

    }

    public void onSaveButtonClick(ActionEvent actionEvent) {
        String name = nameTxt.getText();
        String duration = durationTxt.getText();
        String seats = seatsTxt.getText();
        String hour = hourTxt.getText();
        LocalDate startDay = startDate.getValue();
        LocalDateTime start = LocalDateTime.of(startDay, LocalTime.of(0, 0));
        if(!hour.isEmpty()) {
            LocalTime time = LocalTime.parse(hour);
            start = LocalDateTime.of(startDay, time);
        }
        if(!name.isEmpty() && !duration.isEmpty() && !seats.isEmpty()) {
            int minutes = Integer.parseInt(duration);
            int seat = Integer.parseInt(seats);
            Show show = service.validate(start, minutes);
            if(show == null){
                service.saveShow(name,minutes,start,seat);
                nameTxt.clear();
                durationTxt.clear();
                seatsTxt.clear();
                hourTxt.clear();
            }else{
                MessageAlert.showSuccesMessage(null,"The show cannot be scheduled at the specified time as "+show.getName()+" will be playing at that time");
            }
        }
    }
}
