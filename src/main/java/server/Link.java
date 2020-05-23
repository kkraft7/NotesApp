package server;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Represents a web link.
 */
public class Link extends Base {
  URL link;

  public Link(String title, String description, String url) throws MalformedURLException {
    super(title, description);
    this.link = new URL(url);
  }

  public Link(String title, String url) throws MalformedURLException{ this(title, null, url); }
}
