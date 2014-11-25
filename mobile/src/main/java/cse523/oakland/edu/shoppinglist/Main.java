package cse523.oakland.edu.shoppinglist;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.logging.Logger;


public class Main extends Activity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        String[] myDataset = new String[10];
        myDataset[0] = "wow";
        myDataset[1] = "wow";
        myDataset[2] = "wow";
        myDataset[3] = "wow";
        myDataset[4] = "wow";
        myDataset[5] = "wow";
        myDataset[6] = "wow";
        myDataset[7] = "wow";
        myDataset[8] = "wow";
        myDataset[9] = "wow";
        mAdapter = new MyAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);

        //start test code
        ShoppingList a = new ShoppingList();
        a.setName("list1");
        ShoppingItem i1 = new ShoppingItem();
        i1.setPurchased(0);
        i1.setImageId(1000);
        i1.setItemName("apples");
        a.addItem(i1);

        ShoppingDataSource ds = new ShoppingDataSource(this);
        ds.open();
        ds.addShoppingList(a);

        ShoppingList b = ds.getShoppingList();
        ds.close();
        Log.d("========", b.getName());
        //end test code

        Button c = (Button)findViewById(R.id.addItem);
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int notificationId = 101;
                // Build intent for notification content
                Intent viewIntent = new Intent(getApplicationContext(), SecondActivity.class);
                PendingIntent viewPendingIntent =
                        PendingIntent.getActivity(getApplicationContext(), 0, viewIntent, 0);

                //Building notification layout
                NotificationCompat.Builder notificationBuilder =
                        new NotificationCompat.Builder(getApplicationContext())
                                .setSmallIcon(R.drawable.ic_launcher)
                                .setContentTitle("Demo")
                                .setContentText("It's demo of simple notification")
                                .addAction(R.drawable.ic_launcher, "Open The Google", getPendingIntent())
                                .setContentIntent(viewPendingIntent);

                // instance of the NotificationManager service
                NotificationManagerCompat notificationManager =
                        NotificationManagerCompat.from(getApplicationContext());

                // Build the notification and notify it using notification manager.
                notificationManager.notify(notificationId, notificationBuilder.build());
            }

            public PendingIntent getPendingIntent(){
                // Build an intent for an action to open a url
                Intent urlIntent = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse("http://www.google.com");
                urlIntent.setData(uri);
                PendingIntent urlPendingIntent =
                        PendingIntent.getActivity(getApplicationContext(), 0, urlIntent, 0);

                return urlPendingIntent;
            }
        });
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
}
