package cse523.oakland.edu.shoppinglist;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.logging.Logger;


public class Main extends Activity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    Button addItemButton;
    FragmentManager fm = getFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        addItemButton = (Button) findViewById(R.id.addItem);

        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        ShoppingList shoppingList = ShoppingList.getAppData();

        ShoppingItem test = new ShoppingItem();
        test.setItemName("Banana");
        test.setImageId(R.drawable.img_produce_nt);
        test.setPurchased(1);
        shoppingList.addItem(test);

        ShoppingItem test2 = new ShoppingItem();
        test2.setItemName("Steak");
        test2.setImageId(R.drawable.img_meat_nt);
        shoppingList.addItem(test2);

        mAdapter = new MyAdapter(shoppingList.getItems(), this);
        mRecyclerView.setAdapter(mAdapter);

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewItemDialog newItemDialog = new NewItemDialog();
                newItemDialog.show(fm, "WOW");
            }
        });

        SMSReceiver receiver = new SMSReceiver(this);
        registerReceiver(receiver, SMSReceiver.filter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onShoppingListUpdate() {
        mAdapter.notifyDataSetChanged();
    }
}
