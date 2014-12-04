package cse523.oakland.edu.shoppinglist;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rachel on 12/4/2014.
 */
public class SMSReceiver extends BroadcastReceiver {

    static IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");

    int imageResources[] ={
            R.drawable.img_produce_nt,
            R.drawable.img_grain_nt,
            R.drawable.img_meat_nt,
            R.drawable.img_dairy_nt,
            R.drawable.img_snacks_nt,
            R.drawable.img_other_nt
    };

    Context mContext;

    ArrayList<String> groceryTypes = new ArrayList<String>();
    ShoppingItem newItem = new ShoppingItem();

    public SMSReceiver (Context context) {
        mContext = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        final Object[] pdusObj = (Object[]) bundle.get("pdus");
        SmsMessage message = SmsMessage.createFromPdu((byte[]) pdusObj[0]);
        String msgString = message.getMessageBody();
        groceryTypes.add("produce");
        groceryTypes.add("grains");
        groceryTypes.add("meat");
        groceryTypes.add("dairy");
        groceryTypes.add("snacks");
        groceryTypes.add("other");

        validMessage(msgString);
        Main mainActivity = (Main) context;
        mainActivity.onShoppingListUpdate();
        mainActivity.donotification();
    }

    //Texts in form 1111:Category:Item
    public void validMessage(String message) {
        ShoppingList shoppingList = ShoppingList.getAppData();
        if (message.contains("1111")) {
            String[] items = message.split(":");

            if (items.length == 3) {
                String category = items[1];
                String item = items[2];

                if (groceryTypes.indexOf(category) != -1) {
                    int index = groceryTypes.indexOf(category);
                    newItem.setImageId(imageResources[index]);
                } else {
                    newItem.setImageId(imageResources[5]);
                }

                newItem.setItemName(item);
                newItem.setPurchased(0);
                shoppingList.addItem(newItem);

            } else if (items.length == 2) {
                String item = items[1];
                newItem.setItemName(item);
                newItem.setImageId(imageResources[5]);
                newItem.setPurchased(0);
                shoppingList.addItem(newItem);
            }
        }
    }
}
