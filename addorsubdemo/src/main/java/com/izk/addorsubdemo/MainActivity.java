package com.izk.addorsubdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private AddOrSubLayout add_or_sub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add_or_sub = findViewById(R.id.add_or_sub);

        add_or_sub.setTexViewtBackground(getDrawable(R.color.colorAccent));
        add_or_sub.setButtonAddBackgroud(getDrawable(R.color.colorPrimary));
        add_or_sub.setButtonSubBackgroud(getDrawable(R.color.colorPrimary));

        add_or_sub.setValue(3);
        add_or_sub.setMaxValue(10);
        add_or_sub.setMinValue(1);

    }
}
