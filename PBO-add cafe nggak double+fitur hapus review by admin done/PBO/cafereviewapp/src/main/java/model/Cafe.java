package model;

import javafx.beans.property.*;

public class Cafe {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty location = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();

    public Cafe() {}

    public Cafe(int id, String name, String location, String description) {
        setId(id);
        setName(name);
        setLocation(location);
        setDescription(description);
    }

    public IntegerProperty idProperty() { return id; }
    public StringProperty nameProperty() { return name; }
    public StringProperty locationProperty() { return location; }
    public StringProperty descriptionProperty() { return description; }

    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }

    public String getName() { return name.get(); }
    public void setName(String name) { this.name.set(name); }

    public String getLocation() { return location.get(); }
    public void setLocation(String location) { this.location.set(location); }

    public String getDescription() { return description.get(); }
    public void setDescription(String description) { this.description.set(description); }
    
    @Override
public String toString() {
    return getName(); // supaya comboBox menampilkan nama cafe, bukan object reference
}

}