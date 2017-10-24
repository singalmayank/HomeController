package com.iothome.app.homecontroller;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.iothome.app.homecontroller.model.Customer;
import com.iothome.app.homecontroller.model.Room;
import com.iothome.app.homecontroller.model.Thing;
import com.iothome.app.homecontroller.util.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class ThingsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ThingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_things);

        // Get the Intent that started this activity and extract the required data
        String loginName = getIntent().getStringExtra(Constants.EXTRA_NAME);
        String loginEmail = getIntent().getStringExtra(Constants.EXTRA_LOGIN);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        new SendRequest().execute(loginEmail);
    }

    public class SendRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
        }

        protected String doInBackground(String... arg0) {

            try {

                //TODO : Use this once we have complete end point setup
                String loginName = arg0[0];

                java.net.URL url = new URL(Constants.URL);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoOutput(true);

                int responseCode = conn.getResponseCode();
                Log.d(TAG, "HTTP Response : " + responseCode);

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {
                        sb.append(line);
                    }

                    in.close();
                    return sb.toString();

                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Customer customer = new Customer();
            try {

                JSONObject json = new JSONObject(result);
                JSONObject customerObject = json.getJSONObject("customer");

                customer.setCid(customerObject.getLong("cid"));
                customer.setCemail(customerObject.getString("cemail"));


                JSONArray rooms = customerObject.getJSONArray("rooms");

                for (int i = 0; i < rooms.length(); i++) {
                    JSONObject roomObject = rooms.getJSONObject(i);
                    Room room = new Room();
                    room.setRid(roomObject.getLong("rid"));
                    room.setName(roomObject.getString("name"));

                    JSONArray things = roomObject.getJSONArray("things");

                    for (int j = 0; j < things.length(); j++) {
                        JSONObject thingObject = things.getJSONObject(j);
                        Thing thing = new Thing(thingObject.getLong("tid"),
                                thingObject.getString("name"),
                                thingObject.getString("state"));
                        room.getThings().add(thing);

                    }
                    customer.getRooms().add(room);
                }
            } catch (Exception e) {
                // manage exceptions
                e.printStackTrace();
            }

            populateView(customer);
        }
    }

    private void populateView(Customer customer) {
        //Main outer layout
        LinearLayout verticalLinLayout = (LinearLayout) findViewById(R.id.list_layout);

        Iterator i = customer.getRooms().iterator();

        while (i.hasNext()) {
            Room r = (Room) i.next();

            //One horizontal for each room
            LinearLayout linLayout = new LinearLayout(this);

            TextView tv = new TextView(this);
            tv.setText(r.getName());
            linLayout.addView(tv, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

            verticalLinLayout.addView(linLayout);

            Iterator j = r.getThings().iterator();

            //One horizontal for each thing
            LinearLayout verticalThingslinLayout = new LinearLayout(this);
            verticalThingslinLayout.setOrientation(LinearLayout.VERTICAL);
            verticalLinLayout.addView(verticalThingslinLayout, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

            while (j.hasNext()) {
                Thing t = (Thing) j.next();

                LinearLayout horizontalThingsLayout = new LinearLayout(this);
                horizontalThingsLayout.setWeightSum(4);
                verticalLinLayout.addView(horizontalThingsLayout);

                TextView tvt = new TextView(this);
                tvt.setText(t.getName());
                tvt.setId(Integer.parseInt(String.valueOf(t.getTid())));
                horizontalThingsLayout.addView(tvt, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 3.5f));

                ToggleButton tb = new ToggleButton(this);
                tb.setId(Integer.valueOf(String.valueOf(t.getTid())));
                if (Constants.ON.equals(t.getState())) {
                    tb.setChecked(true);
                } else {
                    tb.setChecked(false);
                }
                tb.setOnClickListener(this);
                horizontalThingsLayout.addView(tb, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0.5f));
            }

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
