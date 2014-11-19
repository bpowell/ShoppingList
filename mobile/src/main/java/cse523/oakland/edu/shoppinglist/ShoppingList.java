package cse523.oakland.edu.shoppinglist;

import java.util.ArrayList;

/**
 * Created by brandon on 11/19/14.
 */
public class ShoppingList {
    private int id;
    private String name;
    private ArrayList<ShoppingItem> items;

    public ArrayList<ShoppingItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<ShoppingItem> items) {
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void addItem(ShoppingItem item) {
        this.items.add(item);
    }

    public void removeItem(ShoppingItem item) {
        this.items.remove(item);
    }

    public void removeItem(int i) {
        this.items.remove(i);
    }
}
