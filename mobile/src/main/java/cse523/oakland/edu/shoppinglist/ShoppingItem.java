package cse523.oakland.edu.shoppinglist;

/**
 * Created by Rachel on 11/19/2014.
 */
public class ShoppingItem {

    private String itemName;
    private int imageId;
    private String itemComment;
    private int purchased;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int isPurchased() {
        return purchased;
    }

    public void setPurchased(int purchased) {
        this.purchased = purchased;
    }

    public String getItemComment() {
        return itemComment;
    }

    public void setItemComment(String itemComment) {
        this.itemComment = itemComment;
    }



}
