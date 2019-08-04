package com.tancorikworld.spacesurvivor.presentation.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.tancorikworld.spacesurvivor.presentation.views.DemoScreenView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new DemoScreenView(this));
    }
}
