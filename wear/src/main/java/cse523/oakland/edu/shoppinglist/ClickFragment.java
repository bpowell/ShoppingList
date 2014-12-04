package cse523.oakland.edu.shoppinglist;

import android.support.wearable.view.CardFragment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Rachel on 12/4/2014.
 */
public class ClickFragment extends CardFragment implements View.OnTouchListener {


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.d("Wow!", "wow");
        return false;
    }
}
