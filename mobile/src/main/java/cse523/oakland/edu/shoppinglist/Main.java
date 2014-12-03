package cse523.oakland.edu.shoppinglist;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi.DataItemResult;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageApi.SendMessageResult;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.Date;
import java.util.logging.Logger;


public class Main extends Activity implements DataApi.DataListener, MessageApi.MessageListener,
        NodeApi.NodeListener, ConnectionCallbacks, OnConnectionFailedListener{

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public static final String TAG = "=====";
    public static final String PATH = "/shop";
    public static final String START_ACTIVITY_PATH = "/start-activity";

    private GoogleApiClient mGoogleApiClient;

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
        final ShoppingList a = new ShoppingList();
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


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wearable.API)
                .build();

        Button btn_open_wear_activity = (Button) findViewById(R.id.open);
        btn_open_wear_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(START_ACTIVITY_PATH, "null");
            }
        });

        Button btn2 = (Button) findViewById(R.id.addItem);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData("/data", a);
            }
        });

    }
    private void sendMessage( final String path, final String text ) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
                for (Node node : nodes.getNodes()) {
                    MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                            mGoogleApiClient, node.getId(), path, text.getBytes()).await();
                }
            }
        }).start();
    }

    private void sendData(final String path, final ShoppingList sl){
        new Thread(new Runnable() {
            @Override
            public void run() {
                PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(PATH);
                sl.putToDataMap(putDataMapRequest.getDataMap());
                putDataMapRequest.getDataMap().putString("testing", "one two three");
                putDataMapRequest.getDataMap().putLong("time", new Date().getTime());
                Log.d(TAG, putDataMapRequest.getDataMap().getString("listname"));
                PutDataRequest request = putDataMapRequest.asPutDataRequest();

                Log.d(TAG, "Generating DataItem: " + request);
                if (!mGoogleApiClient.isConnected()) {
                    return;
                }
                DataItemResult dataItemResult = Wearable.DataApi.putDataItem(mGoogleApiClient, request).await();
                Log.d(TAG, "done: " + dataItemResult.getStatus());
            }
        }).start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (null != mGoogleApiClient && !mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        if (null != mGoogleApiClient && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
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
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.d(TAG, "onDataChanged: " + dataEvents);
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d(TAG, "MSG REC" + messageEvent);
        Toast.makeText(this, "WE GOT A MSG!!" + messageEvent, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPeerConnected(Node node) {

    }

    @Override
    public void onPeerDisconnected(Node node) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "Connection to Google API client has failed");
        Wearable.DataApi.removeListener(mGoogleApiClient, this);
        Wearable.MessageApi.removeListener(mGoogleApiClient, this);
        Wearable.NodeApi.removeListener(mGoogleApiClient, this);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "Google API Client was connected");
        Wearable.DataApi.addListener(mGoogleApiClient, this);
        Wearable.MessageApi.addListener(mGoogleApiClient, this);
        Wearable.NodeApi.addListener(mGoogleApiClient, this);
        Toast.makeText(this, "we connected", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended");
    }
}