package cse523.oakland.edu.shoppinglist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * Created by Rachel on 11/19/2014.
 */

public class NewItemDialog extends DialogFragment {

    EditText newItemName;
    ShoppingItem newItem = new ShoppingItem();

    int imageResources[] ={
            R.drawable.img_produce_nt,
            R.drawable.img_grain_nt,
            R.drawable.img_meat_nt,
            R.drawable.img_dairy_nt,
            R.drawable.img_snacks_nt,
            R.drawable.img_other_nt
    };

    CharSequence groceryTypes[] = {
    "Produce", "Grains", "Meat", "Dairy", "Snacks", "Other"};


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.add_item_view, null))
                .setSingleChoiceItems(groceryTypes, 0,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                newItem.setImageId(imageResources[which]);
                            }
                })
                .setPositiveButton("Add Item", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                    Dialog dialogView = getDialog();
                    newItemName = (EditText) dialogView.findViewById(R.id.newItemName);
                    ShoppingList shoppingList = ShoppingList.getAppData();

                    newItem.setItemName(newItemName.getText().toString());
                    shoppingList.addItem(newItem);
                    Activity mainList = getActivity();
                    ((Main) mainList).onShoppingListUpdate();
                    ((Main) mainList).sendData("/data", shoppingList);

                                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                     NewItemDialog.this.getDialog().cancel();
                }
                });
        return builder.create();
    }
}
