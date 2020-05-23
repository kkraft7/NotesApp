package server;

import java.time.LocalDateTime;
import java.util.ArrayList;
import javax.persistence.*;

// Need tags to indicate relevant search categories
/**
 * This is the base class for notes and to-do items.
 */
@Entity
@Table(name = "NOTES")
public class Note extends Base {
  @Id
  @Column(name="NOTE_ID")
  @GeneratedValue
  private Long noteId;
  private final LocalDateTime created;
  private ArrayList<String> notes;
  private ArrayList<Link> links;

  public Note(String title, String description) {
    super(title, description);
    created = LocalDateTime.now();
  }

  public Note(String title) { this(title, null); }

  public LocalDateTime getDateCreated() { return created; }

  public void addNote(String note) {
    if (notes == null) {
      notes = new ArrayList<>();
    }
    notes.add(note);
  }

  public ArrayList<String> getNotes() { return notes; }

  public void addLink(Link link) {
    if (links == null) {
      links = new ArrayList<>();
    }
    links.add(link);
  }

  public ArrayList<Link> getLinks() { return links; }
}
