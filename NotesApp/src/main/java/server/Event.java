package main.java.server;

import java.time.LocalDateTime;

/**
 * Represents an event that takes place on a certain date.
 */
public class Event extends Note {
  LocalDateTime eventDate;

  public Event(String title, String description, LocalDateTime eventDate) {
    super(title, description);
    this.eventDate = eventDate;
  }

  public Event(String title, LocalDateTime eventDate) { this(title, null, eventDate); }
}
