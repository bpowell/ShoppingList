package cse523.oakland.edu.shoppinglist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by brandon on 11/19/14.
 */
public class MySQLiteHelper extends SQLiteOpenHelper{
    public static String TABLE_SHOPPING_LIST = "shopping_list";
    public static String TABLE_ITEMS = "shopping_items";

    private static final String DB_NAME = "shoppinglist.db";
    private static final int DB_VERSION = 1;

    private static final String CREATE_TABLE_SHOPPING_LIST = "" +
            "create table " + TABLE_SHOPPING_LIST +
            " (id integer primary key autoincrement," +
            " name text not null)";

    private static final String CREATE_TABLE_SHOPPING_ITEMS = "" +
            "create table " + TABLE_ITEMS +
            " (id integer primary key autoincrement," +
            " name text not null," +
            " picid integer," +
            " purchased integer)";


    public MySQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SHOPPING_LIST);
        db.execSQL(CREATE_TABLE_SHOPPING_ITEMS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
