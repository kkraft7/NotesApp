package server;

import server.helper.Contact;

import java.time.LocalDateTime;

/**
** Represents an event or to-do item that takes place on or by a certain date and time.
** A location may also be specified.
*/
public class Event extends Note {
  LocalDateTime eventDate;
  Contact location;
  CompletionStatus status;

  enum CompletionStatus { COMPLETED, CANCELED, MISSED }

  protected Event() {
    super(null, null);
    this.eventDate = null;
    this.location = null;
  }

  public Event(String title, String description, LocalDateTime date, Contact place) {
    super(title, description);
    this.eventDate = date;
    this.location = place;
  }

  public Event(String title, String description, LocalDateTime date, String contactName, String contactNote,
               String phone, String addr1, String addr2, String city, String state, Integer code) {
    this(title, description, date, new Contact(contactName, contactNote, phone, addr1, addr2, city, state, code));
  }

  public Event(String title, LocalDateTime date, String phone, String contact, String addr1, String city) {
    this(title, null, date, new Contact(contact, null, phone, addr1, null, city, null, null));
  }

  public LocalDateTime getDate() { return eventDate; }
  public void setDate(LocalDateTime newDate) { this.eventDate = newDate; }

  public Contact getLocation() { return location; }
  public void setLocation(Contact place) { this.location = place; }

  public CompletionStatus getStatus() { return status; }
  public void setStatus(CompletionStatus newStatus) { this.status = newStatus; }
}
