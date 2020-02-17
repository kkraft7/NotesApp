package main.java.server.helper;

/**
 * Represents a business entity.
 */
public class Business {
  String name;
  String contact;
  Location address;

  public Business(String name, String contact, Location addr) {
    this.name = name;
    this.contact = contact;
    this.address = addr;
  }

  public Business(String name, String contact, String addr1, String city) {
    this(name, contact, new Location(addr1, city));
  }

  public Business(String name, String addr1, String city) { this(name, null, addr1, city); }
}
