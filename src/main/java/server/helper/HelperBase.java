package server.helper;

public class HelperBase {
    String name;
    String note;

    public HelperBase(String newName, String newNote) {
        this.name = newName;
        this.note = newNote;
    }

    public HelperBase(String newName) { this(newName, null); }

    public String getName() { return name; }
    public void setName(String newName) { this.name = newName; }
    public String getNote() { return note; }
    public void setNote(String newNote) { note = newNote; }
}
