package org.artoolkit.ar.samples.QuiRA;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by filip on 03/09/2018.
 */

public class ActWelcome extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 4000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntend = new Intent(ActWelcome.this, ActInicio.class);
                startActivity(homeIntend);
                finish();
            }
        }, SPLASH_TIME_OUT);

    }

}
