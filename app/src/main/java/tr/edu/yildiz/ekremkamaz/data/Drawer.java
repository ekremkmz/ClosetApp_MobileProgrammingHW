package tr.edu.yildiz.ekremkamaz.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Drawer implements Parcelable {
    private int id;
    private String name;

    public Drawer(int id, String name) {
        this.id = id;
        this.name = name;
    }

    protected Drawer(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }

    public static final Creator<Drawer> CREATOR = new Creator<Drawer>() {
        @Override
        public Drawer createFromParcel(Parcel in) {
            return new Drawer(in);
        }

        @Override
        public Drawer[] newArray(int size) {
            return new Drawer[size];
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

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
    }
}
