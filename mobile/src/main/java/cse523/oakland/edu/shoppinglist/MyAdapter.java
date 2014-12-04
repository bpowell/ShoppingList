package cse523.oakland.edu.shoppinglist;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Rachel on 11/19/2014.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<ShoppingItem> mShoppingItems;
    private Context mContext;
    private ShoppingItem shoppingItem;


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
    public MyAdapter(ArrayList<ShoppingItem> shoppingItems, Context context) {
        mShoppingItems = shoppingItems;
        mContext = context;
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
        CardView mCardView;
        ImageView mTrash;
        final int currentPosition = position;
        shoppingItem = mShoppingItems.get(position);

        mTextView = (TextView) holder.mView.findViewById(R.id.itemName);
        mImageView = (ImageView) holder.mView.findViewById(R.id.itemImage);
        mCardView = (CardView) holder.mView.findViewById(R.id.cardView);
        mTrash = (ImageView) holder.mView.findViewById(R.id.itemTrash);
        mTextView.setText(shoppingItem.getItemName());
        mImageView.setImageResource(shoppingItem.getImageId());

        if (shoppingItem.isPurchased() == 1) {
            mImageView.setBackgroundColor(Color.parseColor("#EE30B77F"));
            shoppingItem.setPurchased(1);
        } else {
            mImageView.setBackgroundColor(Color.parseColor("#EF9A9A"));
            shoppingItem.setPurchased(0);
        }

        //change color of background
        mImageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ImageView thisImage = (ImageView) v;
                if (shoppingItem.isPurchased() == 1) {
                    thisImage.setBackgroundColor(Color.parseColor("#EF9A9A"));
                    shoppingItem.setPurchased(0);
                } else {
                    thisImage.setBackgroundColor(Color.parseColor("#EE30B77F"));
                    shoppingItem.setPurchased(1);
                }
        }
        });

        //dismiss card
        mTrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShoppingItems.remove(currentPosition);
                refreshShoppingList(mShoppingItems);
            }
        });

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
