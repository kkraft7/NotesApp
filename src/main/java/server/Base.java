package server;

import javax.persistence.MappedSuperclass;

/**
 * Base class for note data.
 */
@MappedSuperclass
public class Base {
  private String title;
  private String description;

  public Base(String title, String description) {
    this.title = title;
    this.description = description;
  }

  public String getTitle() { return title;}
  public void setTitle(String newTitle) { title = newTitle; }
  public String getDescription() { return description; }
  public void setDescription(String newDescription) { description = newDescription; }
}
