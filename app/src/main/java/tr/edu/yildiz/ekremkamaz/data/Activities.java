package tr.edu.yildiz.ekremkamaz.data;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class Activities {
    private int id;
    private String name;
    private String type;
    private String date;
    private LatLng location;
    private Combine combine;

    public Activities(int id, String name, String type, String date, LatLng location, Combine combine) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.date = date;
        this.location = location;
        this.combine = combine;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public Combine getCombine() {
        return combine;
    }

    public void setCombine(Combine combine) {
        this.combine = combine;
    }

}
