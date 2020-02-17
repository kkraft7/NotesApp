package main.java.server;

/**
 * Represents an artwork in a specific medium.
 */
public class MediaItem extends Note {
  String creator;
  Category mediaType;

  enum Category { RECORD, SONG, BOOK, FILM }

  public MediaItem(String title, String description, String creator, Category type) {
    super(title, description);
    this.creator = creator;
    this.mediaType = type;
  }

  public MediaItem(String title, String creator, Category type) { this(title, null, creator, type); }
}
