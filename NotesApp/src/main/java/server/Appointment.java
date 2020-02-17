package main.java.server;

import main.java.server.helper.Business;

import java.time.LocalDateTime;

/**
 * Represents an appointment at a given business at a given time.
 */
public class Appointment extends ToDo {
  Business place;

  public Appointment(String title, String description, LocalDateTime apptDate, Business place) {
    super(title, description, apptDate);
    this.place = place;
  }

  public Appointment(String title, LocalDateTime apptDate, String name, String contact, String addr1, String city) {
    this(title, null, apptDate, new Business(name, contact, addr1, city));
  }
}
