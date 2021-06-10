package tr.edu.yildiz.ekremkamaz.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Clothes implements Parcelable {
    private int id;
    private String name;
    private String type;
    private String color;
    private String pattern;
    private String date;
    private int price;
    private String photo;
    private int drawer_id;

    public Clothes(int id, String name, String type, String color, String pattern, String date, int price, String photo, int drawer_id) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.color = color;
        this.pattern = pattern;
        this.date = date;
        this.price = price;
        this.photo = photo;
        this.drawer_id = drawer_id;
    }

    protected Clothes(Parcel in) {
        id = in.readInt();
        name = in.readString();
        type = in.readString();
        color = in.readString();
        pattern = in.readString();
        date = in.readString();
        price = in.readInt();
        photo = in.readString();
        drawer_id = in.readInt();
    }

    public static final Creator<Clothes> CREATOR = new Creator<Clothes>() {
        @Override
        public Clothes createFromParcel(Parcel in) {
            return new Clothes(in);
        }

        @Override
        public Clothes[] newArray(int size) {
            return new Clothes[size];
        }
    };

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String type) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(type);
        parcel.writeString(color);
        parcel.writeString(pattern);
        parcel.writeString(date);
        parcel.writeInt(price);
        parcel.writeString(photo);
        parcel.writeInt(drawer_id);
    }
}
