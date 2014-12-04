package cse523.oakland.edu.shoppinglist;

import android.app.Activity;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.wearable.view.GridViewPager;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi.DataItemResult;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageApi.SendMessageResult;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import android.view.View.OnApplyWindowInsetsListener;

import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MainWear extends Activity implements ConnectionCallbacks, OnConnectionFailedListener,
        DataApi.DataListener, MessageApi.MessageListener, NodeApi.NodeListener {

    private TextView mTextView;
    private GridViewPager pager;
    public static final String TAG = "WEARTHEREHERE";

    private GoogleApiClient mGoogleApiClient;
    private SampleGridPagerAdapter sampleGridPagerAdapter;
    private ShoppingList shoppingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_wear);
        Log.d(TAG, "start wear main");

        shoppingList = new ShoppingList();
        shoppingList.setName("test name");

        pager = (GridViewPager) findViewById(R.id.pager);
        final Resources res = getResources();
        pager.setOnApplyWindowInsetsListener(new OnApplyWindowInsetsListener() {
            @Override
            public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                // Adjust page margins:
                //   A little extra horizontal spacing between pages looks a bit
                //   less crowded on a round display.
                final boolean round = insets.isRound();
                int rowMargin = res.getDimensionPixelOffset(R.dimen.page_row_margin);
                int colMargin = res.getDimensionPixelOffset(round ?
                        R.dimen.page_column_margin_round : R.dimen.page_column_margin);
                pager.setPageMargins(rowMargin, colMargin);
                return insets;
            }
        });
        sampleGridPagerAdapter = new SampleGridPagerAdapter(getApplication(), getFragmentManager());
        sampleGridPagerAdapter.shoppingList = shoppingList;
        pager.setAdapter(sampleGridPagerAdapter);

        //yup
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Wearable.DataApi.removeListener(mGoogleApiClient, this);
        Wearable.MessageApi.removeListener(mGoogleApiClient, this);
        Wearable.NodeApi.removeListener(mGoogleApiClient, this);
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "WE CONNECT YOHO");
        Wearable.DataApi.addListener(mGoogleApiClient, this);
        Wearable.MessageApi.addListener(mGoogleApiClient, this);
        Wearable.NodeApi.addListener(mGoogleApiClient, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "connection suspended");
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.d(TAG, "onDataChanged in the main wear");
        final List<DataEvent> events = FreezableUtils.freezeIterable(dataEvents);
        dataEvents.close();
        for (DataEvent event : events) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                String path = event.getDataItem().getUri().getPath();
                if ("/shop".equals(path)) {
                    DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
                    shoppingList.setName(dataMapItem.getDataMap().getString("listname"));
                    ShoppingList s = new ShoppingList(
                            dataMapItem.getDataMap().getString("listname"),
                            dataMapItem.getDataMap().getStringArrayList("itemNames"),
                            dataMapItem.getDataMap().getIntegerArrayList("itemImageIds"),
                            dataMapItem.getDataMap().getIntegerArrayList("itemPurchased")
                            );
                    shoppingList = s;
                    sampleGridPagerAdapter.shoppingList = shoppingList;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pager.setAdapter(sampleGridPagerAdapter);
                        }
                    });
                } else {
                    Log.d(TAG, "Unrecognized path: " + path);
                }
            }
        }
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d(TAG, "message received");
    }

    @Override
    public void onPeerConnected(Node node) {
        Log.d(TAG, "peer be connected");
    }

    @Override
    public void onPeerDisconnected(Node node) {
        Log.d(TAG, "peer be disconnected");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "CONNECTION FAILED HOMEY");
    }
}
