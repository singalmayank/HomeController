package com.iothome.app.homecontroller;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory;
import com.iothome.app.homecontroller.model.Asset;
import com.iothome.app.homecontroller.model.Room;
import com.iothome.app.homecontroller.model.User;
import com.iothome.app.homecontroller.model.Thing;
import com.iothome.app.homecontroller.util.Constants;

import com.iothome.app.homecontroller.util.ThingsExpandableListAdapter;
import com.iothome.app.homecontroller.util.Utilities;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ThingsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ThingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_things);

        // Get the Intent that started this activity and extract the required data
        String loginName = getIntent().getStringExtra(Constants.EXTRA_NAME);
        String loginEmail = getIntent().getStringExtra(Constants.EXTRA_LOGIN);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        new SendRequest().execute(loginEmail);
    }

    public class SendRequest extends AsyncTask<String, Void, User> {

        protected void onPreExecute() {
        }

        protected User doInBackground(String... arg0) {

            User user = null;
            try {

                //TODO : Use this once we have complete end point setup
                String loginName = arg0[0];

                java.net.URL url = new URL(Constants.URL);

                ApiClientFactory factory = new ApiClientFactory();
                // Create an instance of your SDK.
                final UserManagementClient client = factory.build(UserManagementClient.class);
                user = client.usersEmailIdGet(loginName);


            } catch (Exception e) {
                e.printStackTrace();
                //return new String("Exception: " + e.getMessage());
            }
            return user;
        }

        @Override
        protected void onPostExecute(User user) {
            populateView(user);
        }
    }

    private void populateView(User user) {

        //Main outer layout
        LinearLayout verticalLinLayout = (LinearLayout) findViewById(R.id.list_layout);

        //Read the Asset level Things first.
        if (user.getThings() != null){
            Iterator assetThingsIterator = user.getThings().iterator();
            while (assetThingsIterator.hasNext()) {
                verticalLinLayout.addView(
                        Utilities.createThingView(null, (Thing)assetThingsIterator.next(), this));
            }

        }

        Iterator assetIterator = user.getAssets().iterator();

        while (assetIterator.hasNext()) {

            Asset asset = (Asset) assetIterator.next();

            LinearLayout assetLayout = new LinearLayout(this);
            assetLayout.setOrientation(LinearLayout.VERTICAL);


            TextView tv = new TextView(this);
            tv.setText(asset.getDisplayName());
            tv.setTextSize(32f);
            assetLayout.addView(tv, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

            Iterator roomIterator = asset.getRooms().iterator();

            while (roomIterator.hasNext()) {
                List<String> roomListHeaders = new ArrayList<String>();
                HashMap<String, List<Thing>> roomListChild = new HashMap<String, List<Thing>>();

                List<Thing> thingsListHeaders = new ArrayList<Thing>();
                Room room = (Room) roomIterator.next();
                roomListHeaders.add(room.getDisplayName());

                LinearLayout roomLayout = new LinearLayout(this);
                roomLayout.setOrientation(LinearLayout.VERTICAL);

                ExpandableListView roomListView = new ExpandableListView(this);

                Iterator thingIterator = room.getThings().iterator();
                while (thingIterator.hasNext()) {
                    Thing thing = (Thing) thingIterator.next();
                    thingsListHeaders.add(thing);
                }

                roomListView.setAdapter(new ThingsExpandableListAdapter(this,roomListHeaders,roomListChild));
                roomListChild.put(room.getDisplayName(), thingsListHeaders);
                roomLayout.addView(roomListView);
                assetLayout.addView(roomLayout);
            }
            verticalLinLayout.addView(assetLayout,new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getApplicationContext(), "Switch state updated for : ." + v.getId(),
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_things, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.add_thing:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
