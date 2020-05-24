package server.helper;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Represents a web link.
 */
public class Link extends HelperBase {
  URL linkURL;

  protected Link() {
    super(null, null);
    this.linkURL = null;
  }

  public Link(String linkName, String linkNote, String url) throws MalformedURLException {
    super(linkName, linkNote);
    this.linkURL = new URL(url);
  }

  public Link(String name, String url) throws MalformedURLException{ this(name, null, url); }

  public URL getUrl() { return linkURL; }
}
