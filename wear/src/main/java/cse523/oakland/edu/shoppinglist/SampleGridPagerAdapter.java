package cse523.oakland.edu.shoppinglist;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.support.wearable.view.ImageReference;
import android.util.Log;
import android.view.Gravity;

public class SampleGridPagerAdapter extends FragmentGridPagerAdapter {

    private final Context mContext;
    public static final String TAG = "WEARTHEREHERE";
    public ShoppingList shoppingList;

    public SampleGridPagerAdapter(Context ctx, FragmentManager fm, ShoppingList shoppingList) {
        super(fm);
        mContext = ctx;
        this.shoppingList = shoppingList;
    }

    @Override
    public Fragment getFragment(int row, int col) {
        String title = shoppingList.getName() + " item: ";
        String text;
        CardFragment fragment;

        if(shoppingList.getItems().size()==0 || row >= shoppingList.getItems().size()) {
                text = "No items";
                fragment = CardFragment.create(title, text, R.drawable.shopping_icon);
        }else{
            text = shoppingList.getItems().get(row).getItemName();
            int imgid= R.drawable.shopping_icon;

            fragment = CardFragment.create(title, text, imgid);
            Log.d(TAG, "img id = " + shoppingList.getItems().get(row).getImageId());
        }
        fragment.setCardGravity(Gravity.CENTER);
        //fragment.setExpansionEnabled(true);
        //fragment.setExpansionDirection(CardFragment.EXPAND_UP);
        return fragment;
    }

    @Override
    public ImageReference getBackground(int row, int column) {
        return ImageReference.forDrawable(R.drawable.green);
    }

    @Override
    public int getRowCount() {
        //make this higher than what we demo in class
        //other wise it breaks
        //return 10; //shoppingList.getItems().size()+1;
        return  shoppingList.getItems().size()+1;
    }

    @Override
    public int getColumnCount(int rowNum) {
        return 1;
    }
}
