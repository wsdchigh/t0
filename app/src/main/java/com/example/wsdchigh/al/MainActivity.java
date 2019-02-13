package com.example.wsdchigh.al;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    ViewHolder0 vh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vh = new ViewHolder0(this);
        setContentView(vh.getView());
        vh.install();


    }
}
