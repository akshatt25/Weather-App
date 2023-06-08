package com.example.webmobiweather;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;

public class MyDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "my_database";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "my_table";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_STRING = "my_string";

    private static MyDatabase instance;

    private MyDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized MyDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new MyDatabase(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_STRING + " TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTableQuery = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(dropTableQuery);
        onCreate(db);
    }

    public static void saveString(Context context, String value) {
        SQLiteDatabase db = getInstance(context).getWritableDatabase();
        db.delete(TABLE_NAME, null, null);

        ContentValues values = new ContentValues();
        values.put(COLUMN_STRING, value);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public static String getString(Context context) {
        SQLiteDatabase db = getInstance(context).getReadableDatabase();
        String selectQuery = "SELECT " + COLUMN_STRING + " FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        String result = "";

        if (cursor.moveToFirst()) {
            result = cursor.getString(0);
        }

        cursor.close();
        db.close();
        return result;
    }

}
