package com.example.android.restful;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.restful.model.DataItem;
import com.example.android.restful.services.MyService;
import com.example.android.restful.utils.NetworkHelper;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    private static final String JSON_URL =
            "http://560057.youcanlearnit.net/services/json/itemsfeed.php";

    private boolean networkOk;
    TextView output;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
         /*   String message =
                    intent.getStringExtra(MyService.MY_SERVICE_PAYLOAD);
            output.append(message + "\n");*/
            DataItem[] dataItems=(DataItem[]) intent.getParcelableArrayExtra(MyService.MY_SERVICE_PAYLOAD);
            for (DataItem item:dataItems){
                output.append(item.getItemName() + "\n");

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        output = (TextView) findViewById(R.id.output);

        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mBroadcastReceiver,
                        new IntentFilter(MyService.MY_SERVICE_MESSAGE));

        networkOk = NetworkHelper.hasNetworkAccess(this);
        output.append("Network ok: " + networkOk);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(mBroadcastReceiver);
    }

    public void runClickHandler(View view) {

        if (networkOk) {
            Intent intent = new Intent(this, MyService.class);
            intent.setData(Uri.parse(JSON_URL));
            startService(intent);
        } else {
            Toast.makeText(this, "Network not available!", Toast.LENGTH_SHORT).show();
        }
    }

    public void clearClickHandler(View view) {
        output.setText("");
    }

}
