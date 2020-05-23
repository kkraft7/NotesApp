package server.helper;

/**
 * Represents an American address (not internationalized).
 */
public class Location {
  String address1;
  String address2;
  String city;
  String state;
  Integer zipCode;

  public Location(String addr1, String addr2, String state, String city, Integer zip) {
    this.address1 = addr1;
    this.address2 = addr2;
    this.city = city;
    this.state = state;
    this.zipCode = zip;
  }

  public Location(String addr1, String state, String city) { this(addr1, null, state, city, null); }
  public Location(String addr1, String city) { this(addr1, null, city); }
}
