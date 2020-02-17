package main.java.server;

/**
 * Base class for note data.
 */
public class Base {
  String title;
  String description;

  public Base(String title, String description) {
    this.title = title;
    this.description = description;
  }

  public String getTitle() { return title;}
  public void setTitle(String newTitle) { title = newTitle; }
  public String getDescription() { return description; }
  public void setDescription(String newDescription) { description = newDescription; }
}
