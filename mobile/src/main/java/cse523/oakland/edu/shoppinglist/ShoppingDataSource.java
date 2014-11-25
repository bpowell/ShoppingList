package cse523.oakland.edu.shoppinglist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by brandon on 11/19/14.
 */
public class ShoppingDataSource {
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;

    public ShoppingDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void addShoppingList(ShoppingList shoppingList) {
        ContentValues values = new ContentValues();
        values.put("name", shoppingList.getName());
        long insertId = database.insert(MySQLiteHelper.TABLE_SHOPPING_LIST, null,
                values);
        for(ShoppingItem item : shoppingList.getItems()) {
            ContentValues v = new ContentValues();
            values.put("name", item.getItemName());
            values.put("picid", item.getImageId());
            values.put("purchase", Integer.valueOf(String.valueOf(item.isPurchased())));
            long id = database.insert(MySQLiteHelper.TABLE_ITEMS, null, values);
        }
    }

    public ShoppingList getShoppingList() {
        Cursor cursor = database.rawQuery("select * from shopping_list where id = (select max(id) from shopping_list)", null);
        ShoppingList sl = new ShoppingList();
        cursor.moveToFirst();
        sl.setName(cursor.getString(1));
        cursor.close();

        return sl;
    }
}
