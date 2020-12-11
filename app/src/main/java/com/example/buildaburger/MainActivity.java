package com.example.buildaburger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final LinearLayout linearLayoutStartBtn = findViewById(R.id.start_btn_layout);
        final TextView textView = findViewById(R.id.start_text_view);
        final Button buildBurger = findViewById(R.id.build_burger_btn);

        final Animation fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        final Animation slideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.start_btn_slide_up);

        textView.startAnimation(fadeIn);
        textView.setVisibility(View.VISIBLE);
        linearLayoutStartBtn.startAnimation(slideUp);
        linearLayoutStartBtn.setVisibility(View.VISIBLE);


        buildBurger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intentBuild = new Intent(MainActivity.this, BuildBurgerActivity.class);
                startActivity(intentBuild);
            }
        });
    }
}