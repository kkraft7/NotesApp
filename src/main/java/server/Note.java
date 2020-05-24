package server;

import server.helper.Link;

import java.time.LocalDateTime;
import java.util.ArrayList;
import javax.persistence.*;

// Need tags to indicate relevant search categories
/**
 * This is the base class for notes and to-do items.
 */
@Entity
@Table(name = "NOTES")
public class Note extends Node<Note> {
  @Id
  @Column(name="NOTE_ID")
  @GeneratedValue
  private Long noteId;
  private String description;
  private final LocalDateTime created;
  private ArrayList<Link> links;

  protected Note() {
    super(null);
    created = LocalDateTime.now();
  }

  public Note(String title, String desc) {
    super(title);
    this.description = desc;
    created = LocalDateTime.now();
  }

  public Note(String title) { this(title, null); }

  public String getDescription() { return description; }
  public void setDescription(String newDescription) { description = newDescription; }
  public LocalDateTime getDateCreated() { return created; }

  public void addLink(Link link) {
    if (links == null) {
      links = new ArrayList<>();
    }
    links.add(link);
  }

  public ArrayList<Link> getLinks() { return links; }
}
