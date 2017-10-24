package com.iothome.app.homecontroller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.iothome.app.homecontroller.util.Constants;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private static String loginName;
    private static String loginEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Button listeners
        findViewById(R.id.list_things_button).setOnClickListener(this);

        // Get the Intent that started this activity and extract the required data
        Intent intent = getIntent();
        loginName = intent.getStringExtra(Constants.EXTRA_NAME);
        loginEmail = intent.getStringExtra(Constants.EXTRA_LOGIN);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // Capture the layout's TextView and set the string as its text
        TextView welcomeTextView = (TextView) findViewById(R.id.welcome_text);
        welcomeTextView.setText(loginName + getString(R.string.main_welcome_message));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.list_things_button:
                showThings();
                break;
        }
    }

    /**
     * This will call the Rest service and get all the things already setup for this user.
     */
    private void showThings() {
        //Navigate to Things page
        Intent intent = new Intent(this, ThingsActivity.class);
        intent.putExtra(Constants.EXTRA_NAME, loginName);
        intent.putExtra(Constants.EXTRA_LOGIN, loginEmail);
        startActivity(intent);
    }


    private void showSettings() {
        //TODO : move this to menu bar
    }

    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }
}
