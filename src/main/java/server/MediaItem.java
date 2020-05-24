package server;

/**
** Represents an artwork in a specified medium.
*/
public class MediaItem extends Note {
  String creator;
  Category mediaType;

  enum Category { RECORD, SONG, BOOK, FILM }

  protected MediaItem() {
    super(null, null);
    this.creator = null;
    this.mediaType = null;
  }

  public MediaItem(String title, String description, String creator, Category type) {
    super(title, description);
    this.creator = creator;
    this.mediaType = type;
  }

  public MediaItem(String title, String creator, Category type) { this(title, null, creator, type); }

  public String getCreator() { return creator; }
  public void setCreator(String newCreator) { this.creator = newCreator; }

  public Category getMediaType() { return mediaType; }   // Not sure if we need a setter for this
}
