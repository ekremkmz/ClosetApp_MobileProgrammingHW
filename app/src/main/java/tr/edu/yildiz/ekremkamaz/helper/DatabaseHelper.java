package tr.edu.yildiz.ekremkamaz.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import tr.edu.yildiz.ekremkamaz.data.Activities;
import tr.edu.yildiz.ekremkamaz.data.Clothes;
import tr.edu.yildiz.ekremkamaz.data.Combine;
import tr.edu.yildiz.ekremkamaz.data.Drawer;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper DBHelper = null;
    private static SQLiteDatabase DB = null;
    private Context context;

    private DatabaseHelper(Context context) {
        super(context, "database", null, 1);
        this.context = context;
        openDatabase();
    }

    public static DatabaseHelper getInstance(Context context) {
        if (DBHelper == null) {
            DBHelper = new DatabaseHelper(context);
        }
        return DBHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE drawers (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    name varchar(10)\n" +
                ");");

        db.execSQL("CREATE TABLE clothes (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    name varchar(20),\n" +
                "    type varchar(20),\n" +
                "    color varchar(20),\n" +
                "    pattern varchar(20),\n" +
                "    date varchar(20),\n" +
                "    price INTEGER,\n" +
                "    photo varchar(64),\n" +
                "    drawer_id INTEGER\n" +
                ");");
        db.execSQL("CREATE TABLE combines (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    topOfHead INTEGER,\n" +
                "    face INTEGER,\n" +
                "    top INTEGER,\n" +
                "    lower INTEGER,\n" +
                "    foot INTEGER\n" +
                ");");
        db.execSQL("CREATE TABLE activities (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    name varchar(20),\n" +
                "    type varchar(20),\n" +
                "    date varchar(20),\n" +
                "    latitude INTEGER,\n" +
                "    longitude INTEGER,\n" +
                "    combine INTEGER\n" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    private void openDatabase() {
        if (DB == null) {
            DB = getWritableDatabase();
        }
    }

    public ArrayList<Drawer> getDrawers() {
        openDatabase();
        String[] columns = {"id", "name"};

        Cursor c = DB.query("drawers", columns, null, null, null, null, null);
        ArrayList<Drawer> drawerArrayList = new ArrayList<>();
        while (c.moveToNext()) {
            drawerArrayList.add(new Drawer(c.getInt(0), c.getString(1)));
        }
        return drawerArrayList;
    }

    public boolean deleteDrawer(Drawer d) {
        openDatabase();
        int result = DB.delete("drawers", "id=?", new String[]{String.valueOf(d.getId())});
        if (result > -1)
            return true;
        else
            return false;
    }

    public boolean addDrawer(Drawer d) {
        openDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", d.getName());
        long result = DB.insert("drawers", null, cv);

        if (result < 0) return false;

        String[] columns = {"id"};
        Cursor cur = DB.query("drawers", columns, null, null, null, null, "id desc");
        if (cur.moveToNext()) {
            d.setId(cur.getInt(0));
            return true;
        }
        return false;
    }

    public ArrayList<Clothes> getClothesFromDrawer(int drawer_id) {
        openDatabase();
        String[] columns = {"id", "name", "type", "color", "pattern", "date", "price", "photo"};

        Cursor c = DB.query("clothes", columns, "drawer_id=?", new String[]{String.valueOf(drawer_id)}, null, null, null);
        ArrayList<Clothes> clothesArrayList = new ArrayList<>();
        while (c.moveToNext()) {
            clothesArrayList.add(new Clothes(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5), c.getInt(6), c.getString(7), drawer_id));
        }
        return clothesArrayList;
    }

    public boolean addClothes(Clothes c, Uri uri, int drawer_id) {
        openDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", c.getName());
        cv.put("type", c.getType());
        cv.put("color", c.getColor());
        cv.put("pattern", c.getPattern());
        cv.put("date", c.getDate());
        cv.put("price", c.getPrice());
        cv.put("photo", "");
        cv.put("drawer_id", drawer_id);
        long result = DB.insert("clothes", null, cv);

        if (result < 0) return false;
        cv.clear();

        String[] columns = {"id"};
        Cursor cur = DB.query("clothes", columns, null, null, null, null, "id desc");

        cur.moveToNext();
        c.setId(cur.getInt(0));

        InputStream in;

        try {
            in = context.getContentResolver().openInputStream(uri);
        } catch (IOException e) {
            deleteClothes(c);
            return false;
        }

        File file = new File(context.getFilesDir() + "/" + c.getId());

        OutputStream out;
        try {
            out = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            deleteClothes(c);
            return false;
        }

        try {
            IOUtils.copy(in, out);
        } catch (IOException e) {
            deleteClothes(c);
            return false;
        }

        cv.put("photo", Uri.fromFile(file).toString());

        result = DB.update("clothes", cv, "id=?", new String[]{String.valueOf(c.getId())});

        c.setPhoto(Uri.fromFile(file).toString());

        return result > -1;

    }

    public boolean deleteClothes(Clothes c) {
        openDatabase();
        int result = DB.delete("clothes", "id=?", new String[]{String.valueOf(c.getId())});
        File file = new File(c.getPhoto().split(":")[1]);
        file.delete();
        return result > -1;
    }

    public boolean updateClothes(Clothes c, Uri uri) {
        openDatabase();
        if (!c.getPhoto().equals(uri.toString())) {
            InputStream in = null;
            try {
                in = context.getContentResolver().openInputStream(uri);
            } catch (FileNotFoundException e) {
                return false;
            }
            File file = new File(c.getPhoto().split(":")[1]);
            file.delete();
            OutputStream out = null;
            try {
                out = new FileOutputStream(file);
                IOUtils.copy(in, out);
            } catch (IOException e) {
                return false;
            }
            c.setPhoto(Uri.fromFile(file).toString());

        }
        ContentValues cv = new ContentValues();
        cv.put("name", c.getName());
        cv.put("type", c.getType());
        cv.put("color", c.getColor());
        cv.put("pattern", c.getPattern());
        cv.put("date", c.getDate());
        cv.put("price", c.getPrice());
        cv.put("photo", c.getPhoto());
        long result = DB.update("clothes", cv, "id=?", new String[]{String.valueOf(c.getId())});

        return result > -1;
    }

    public boolean addCombine(Combine c) {
        openDatabase();
        ContentValues cv = new ContentValues();
        cv.put("topOfHead", c.getTopOfHead().getId());
        cv.put("face", c.getFace().getId());
        cv.put("top", c.getTop().getId());
        cv.put("lower", c.getLower().getId());
        cv.put("foot", c.getFoot().getId());
        long result = DB.insert("combines", null, cv);

        if (result < 0) return false;

        String[] columns = {"id"};
        Cursor cur = DB.query("combines", columns, null, null, null, null, "id desc");
        if (cur.moveToNext()) {
            c.setId(cur.getInt(0));
            return true;
        }
        return false;
    }

    public ArrayList<Combine> getCombines() {
        openDatabase();
        String[] columns = {"id", "topOfHead", "face", "top", "lower", "foot"};

        Cursor c = DB.query("combines", columns, null, null, null, null, null);
        ArrayList<Combine> combinesArrayList = new ArrayList<>();
        while (c.moveToNext()) {
            combinesArrayList.add(new Combine(c.getInt(0), getClothes(c.getInt(1)), getClothes(c.getInt(2)), getClothes(c.getInt(3)), getClothes(c.getInt(4)), getClothes(c.getInt(5))));
        }
        return combinesArrayList;
    }

    public Clothes getClothes(int id) {
        openDatabase();
        String[] columns = {"id", "name", "type", "color", "pattern", "date", "price", "photo", "drawer_id"};

        Cursor c = DB.query("clothes", columns, "id=?", new String[]{String.valueOf(id)}, null, null, null);
        Clothes clothes = null;
        if (c.moveToNext()) {
            clothes = new Clothes(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5), c.getInt(6), c.getString(7), c.getInt(8));
        }
        return clothes;
    }

    public boolean deleteCombine(Combine c) {
        openDatabase();
        int result = DB.delete("combines", "id=?", new String[]{String.valueOf(c.getId())});
        return result > -1;
    }

    public boolean addActivities(Activities a) {
        openDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", a.getName());
        cv.put("type", a.getType());
        cv.put("date", a.getDate());
        cv.put("latitude", String.valueOf(a.getLocation().latitude * 10000000));
        cv.put("longitude", String.valueOf(a.getLocation().longitude * 10000000));
        cv.put("combine", a.getCombine().getId());
        long result = DB.insert("activities", null, cv);

        if (result < 0) return false;

        String[] columns = {"id"};
        Cursor cur = DB.query("activities", columns, null, null, null, null, "id desc");
        if (cur.moveToNext()) {
            a.setId(cur.getInt(0));
            return true;
        }
        return false;
    }
}
