package cse523.oakland.edu.shoppinglist;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Rachel on 11/19/2014.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<ShoppingItem> mShoppingItems;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mView;
        public ViewHolder(View v) {
            super(v);
            mView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<ShoppingItem> shoppingItems) {
        mShoppingItems = shoppingItems;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        TextView mTextView;
        ImageView mImageView;

        mTextView = (TextView) holder.mView.findViewById(R.id.itemName);
        mImageView = (ImageView) holder.mView.findViewById(R.id.itemImage);
        mTextView.setText(mShoppingItems.get(position).getItemName());
        mImageView.setImageResource(mShoppingItems.get(position).getImageId());

        if (mShoppingItems.get(position).isPurchased() == 1) {
            mImageView.setBackgroundColor(Color.parseColor("#EE30B77F"));
        } else {
            mImageView.setBackgroundColor(Color.parseColor("#EF9A9A"));
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mShoppingItems.size();
    }

    public void refreshShoppingList(ArrayList<ShoppingItem> shoppingItems) {
        this.mShoppingItems = shoppingItems;
        this.notifyDataSetChanged();
    }
}
