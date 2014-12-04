package cse523.oakland.edu.shoppinglist;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.support.wearable.view.ImageReference;
import android.view.Gravity;

public class SampleGridPagerAdapter extends FragmentGridPagerAdapter {

    private final Context mContext;
    public ShoppingList shoppingList;

    public SampleGridPagerAdapter(Context ctx, FragmentManager fm) {
        super(fm);
        mContext = ctx;
        shoppingList = new ShoppingList();
    }

    @Override
    public Fragment getFragment(int row, int col) {
        String title = shoppingList.getName() + " item: ";
        String text;
        if(shoppingList.getItems().size()!=0 || row > shoppingList.getItems().size()) {
            text = shoppingList.getItems().get(row).getItemName();
        }else{
            text = "No items";
        }
        CardFragment fragment = CardFragment.create(title, text, R.drawable.ic_launcher);
        fragment.setCardGravity(Gravity.BOTTOM);
        fragment.setExpansionEnabled(true);
        fragment.setExpansionDirection(CardFragment.EXPAND_UP);
        return fragment;
    }

    @Override
    public ImageReference getBackground(int row, int column) {
        return ImageReference.forDrawable(R.drawable.ic_launcher);
    }

    @Override
    public int getRowCount() {
        //make this higher than what we demo in class
        //other wise it breaks
        return 2; //shoppingList.getItems().size()+1;
    }

    @Override
    public int getColumnCount(int rowNum) {
        return 1;
    }
}
