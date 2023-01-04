package com.shehzad.gifsvideo;

import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
import com.pd.lookatme.LookAtMe;

public class LookBasedActivity extends AppCompatActivity {
    LookAtMe lookBased;
    String url;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_based);

        //make activity fulll screen
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        lookBased = findViewById(R.id.lookBased);

        //video url
        Bundle bundle = getIntent().getExtras();
        url = bundle.getString("url");

        if (url != null) {
            uri = Uri.parse(url);
        }

        lookBased.init(this);
        lookBased.setVideoPath(url);
        lookBased.start();
        lookBased.setLookMe();
    }
}
