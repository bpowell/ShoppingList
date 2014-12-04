package cse523.oakland.edu.shoppinglist;

import com.google.android.gms.wearable.DataMap;

import java.util.ArrayList;

/**
 * Created by brandon on 11/19/14.
 */
public class ShoppingList {
    private int id;
    private String name;
    private ArrayList<ShoppingItem> items;

    public ShoppingList() {
        items = new ArrayList<ShoppingItem>();
    }

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

    private ArrayList<String> getAllItemNames() {
        ArrayList<String> allNames = new ArrayList<String>();
        for(ShoppingItem i : items){
            allNames.add(i.getItemName());
        }

        return allNames;
    }

    private ArrayList<Integer> getAllImageId() {
        ArrayList<Integer> allImageIds = new ArrayList<Integer>();
        for(ShoppingItem i : items) {
            allImageIds.add(i.getImageId());
        }

        return allImageIds;
    }

    private ArrayList<Integer> getAllPurchased() {
        ArrayList<Integer> allPur = new ArrayList<Integer>();
        for(ShoppingItem i : items) {
            allPur.add(i.isPurchased());
        }

        return allPur;
    }

    public ShoppingList(String name, ArrayList<String> itemNames, ArrayList<Integer> itemImageIds, ArrayList<Integer> itemPur) {
        this.name = name;
        items = new ArrayList<ShoppingItem>();
        for(int i = 0; i<itemNames.size(); i++) {
            ShoppingItem q = new ShoppingItem();
            q.setItemName(itemNames.get(i));
            q.setImageId(itemImageIds.get(i));
            q.setPurchased(itemPur.get(i));
            items.add(q);
        }

    }
    public void putToDataMap(DataMap map) {
        map.putString("listname", name);
        map.putStringArrayList("itemNames", getAllItemNames());
        map.putIntegerArrayList("itemImageIds", getAllImageId());
        map.putIntegerArrayList("itemPurchased", getAllPurchased());

        //return map;
    }
}
