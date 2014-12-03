package cse523.oakland.edu.shoppinglist;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by brandon on 12/2/14.
 */
public class DataLayerListenerService extends WearableListenerService {
    private static final String TAG = "WEARTHEREHERE";

    GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "do we get here man?");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.d(TAG, "onDataChanged in listserv: " + dataEvents);
        final List<DataEvent> events = FreezableUtils.freezeIterable(dataEvents);
        dataEvents.close();
        if(!mGoogleApiClient.isConnected()) {
            ConnectionResult connectionResult = mGoogleApiClient
                    .blockingConnect(130, TimeUnit.SECONDS);
            if (!connectionResult.isSuccess()) {
                Log.e(TAG, "DataLayerListenerService failed to connect to GoogleApiClient.");
                return;
            }else{
                Log.d(TAG, "WE CONNECTED TO GOOGLEAPI CLIENT");
            }
        }else{
            Log.d(TAG, "we not connected yo");
        }

        // Loop through the events and send a message back to the node that created the data item.
        //for (DataEvent event : events) {
        //}
        for (DataEvent event : events) {
            DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
            Log.d(TAG, dataMapItem.getDataMap().getString("listname"));
            Uri uri = event.getDataItem().getUri();
            String path = uri.getPath();
            // Get the node id of the node that created the data item from the host portion of
            // the uri.
            String nodeId = uri.getHost();
            // Set the data of the message to be the bytes of the Uri.
            byte[] payload = uri.toString().getBytes();

            // Send the rpc
            Wearable.MessageApi.sendMessage(mGoogleApiClient, nodeId, "/ret", payload);
        }
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d(TAG, "onMessageReceived: " + messageEvent);
        Intent startIntent = new Intent(this, MainWear.class);
        startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startIntent);
    }

    @Override
    public void onPeerConnected(Node peer) {
        Log.d(TAG, "onPeerConnected: " + peer);
    }

    @Override
    public void onPeerDisconnected(Node peer) {
        Log.d(TAG, "onPeerDisconnected: " + peer);
    }
}
