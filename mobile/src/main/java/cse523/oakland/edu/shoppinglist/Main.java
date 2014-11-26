package cse523.oakland.edu.shoppinglist;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.logging.Logger;


public class Main extends Activity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

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


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(new ConnectionCallbacks())
                .addOnConnectionFailedListener(new ConnectionFailedListener())
                .addApi(Wearable.API)
                .build();
    }

    //start for sync
    private static class SendDataTask extends AsyncTask<Object, Void, Void> {
        private static final String TAG = "MessageUtils.SendDataTask";
        private static final String ASSET_KEY = "data";

        protected Void doInBackground(Object... params) {
            GoogleApiClient client = (GoogleApiClient)params[0];

            PutDataMapRequest dataMap = PutDataMapRequest.create((String)params[1]);
            dataMap.getDataMap().putAsset(ASSET_KEY, (Asset)params[2]);
            PutDataRequest request = dataMap.asPutDataRequest();
            DataApi.DataItemResult result = Wearable.DataApi
                    .putDataItem(client, request)
                    .await();
            if (!result.getStatus().isSuccess()) {
                Log.e(TAG, "could not send data (" + result.getStatus() + ")");
            }

            return null;
        }
    }

    public static void sendData(GoogleApiClient client, String path, Asset asset) {
        new SendDataTask().execute(client, path, asset);
    }

    private class ConnectionCallbacks implements
            GoogleApiClient.ConnectionCallbacks {
        @Override
        public void onConnected(Bundle bundle) {
            // empty
        }

        @Override
        public void onConnectionSuspended(int i) {
            // empty
        }
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

    private class ConnectionFailedListener implements
            GoogleApiClient.OnConnectionFailedListener {
        @Override
        public void onConnectionFailed(ConnectionResult result) {
            // empty
        }
    }
    //end for sync

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
