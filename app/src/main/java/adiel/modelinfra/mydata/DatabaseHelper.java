package adiel.modelinfra.mydata;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static adiel.modelinfra.mydata.UsersContract.*;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "users.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_CATEGORIES_CREATE=
            "CREATE TABLE " + UsersEntry.TABLE_NAME + " (" +
                    UsersEntry._ID + " INTEGER PRIMARY KEY, " +
                    UsersEntry.COLUMN_NAME+ " TEXT, " +
                    UsersEntry.COLUMN_AGE + " INTEGER " +
                    ")";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CATEGORIES_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + UsersEntry.TABLE_NAME);
        onCreate(db);
    }
}
