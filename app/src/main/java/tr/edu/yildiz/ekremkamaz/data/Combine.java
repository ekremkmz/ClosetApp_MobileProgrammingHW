package tr.edu.yildiz.ekremkamaz.data;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import tr.edu.yildiz.ekremkamaz.data.Clothes;

public class Combine implements Parcelable {
    private int id;
    private Clothes topOfHead;
    private Clothes face;
    private Clothes top;
    private Clothes lower;
    private Clothes foot;

    public Combine(int id, Clothes topOfHead, Clothes face, Clothes top, Clothes lower, Clothes foot) {
        this.id = id;
        this.topOfHead = topOfHead;
        this.face = face;
        this.top = top;
        this.lower = lower;
        this.foot = foot;
    }

    protected Combine(Parcel in) {
        id = in.readInt();
        topOfHead = in.readParcelable(Clothes.class.getClassLoader());
        face = in.readParcelable(Clothes.class.getClassLoader());
        top = in.readParcelable(Clothes.class.getClassLoader());
        lower = in.readParcelable(Clothes.class.getClassLoader());
        foot = in.readParcelable(Clothes.class.getClassLoader());
    }

    public static final Creator<Combine> CREATOR = new Creator<Combine>() {
        @Override
        public Combine createFromParcel(Parcel in) {
            return new Combine(in);
        }

        @Override
        public Combine[] newArray(int size) {
            return new Combine[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Clothes getTopOfHead() {
        return topOfHead;
    }

    public void setTopOfHead(Clothes topOfHead) {
        this.topOfHead = topOfHead;
    }

    public Clothes getFace() {
        return face;
    }

    public void setFace(Clothes face) {
        this.face = face;
    }

    public Clothes getTop() {
        return top;
    }

    public void setTop(Clothes top) {
        this.top = top;
    }

    public Clothes getLower() {
        return lower;
    }

    public void setLower(Clothes lower) {
        this.lower = lower;
    }

    public Clothes getFoot() {
        return foot;
    }

    public void setFoot(Clothes foot) {
        this.foot = foot;
    }

    public File toFile(Context context) throws IOException {
        File file = File.createTempFile("combine", ".txt", context.getExternalCacheDir());

        String data = new String();

        data = toString();

        FileOutputStream stream = new FileOutputStream(file);

        stream.write(data.getBytes());

        stream.close();

        return file;
    }

    public String toString() {
        return "{" + "topOfHead:" + String.valueOf(topOfHead.getId()) + ",\n"
                + "face:" + String.valueOf(face.getId()) + ",\n"
                + "top:" + String.valueOf(top.getId()) + ",\n"
                + "lower:" + String.valueOf(lower.getId()) + ",\n"
                + "foot:" + String.valueOf(foot.getId()) + "}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeParcelable(topOfHead, i);
        parcel.writeParcelable(face, i);
        parcel.writeParcelable(top, i);
        parcel.writeParcelable(lower, i);
        parcel.writeParcelable(foot, i);
    }
}
