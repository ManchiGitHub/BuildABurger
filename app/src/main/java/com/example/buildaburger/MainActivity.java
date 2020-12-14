package com.example.buildaburger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
//            // Activity was brought to front and not created,
//            // Thus finishing this will get us to the last viewed activity
//            finish();
//            return;
//        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final LinearLayout linearLayoutStartBtn = findViewById(R.id.start_btn_layout);
        final TextView textView = findViewById(R.id.start_text_view);
        final Button buildBurger = findViewById(R.id.build_burger_btn);
        final EditText userNameET = findViewById(R.id.name_et);

        final Animation fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        final Animation slideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.start_btn_slide_up);

        userNameET.startAnimation(fadeIn);
        userNameET.setVisibility(View.VISIBLE);
        textView.startAnimation(fadeIn);
        textView.setVisibility(View.VISIBLE);
        linearLayoutStartBtn.startAnimation(slideUp);
        linearLayoutStartBtn.setVisibility(View.VISIBLE);


        buildBurger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userName = userNameET.getText().toString();
                userName = userName.trim();

                if (userName.length() == 0) {
                    userName = getString(R.string.anonymous_name);
                }

                userName = " " + userName;

                Intent intent = new Intent(MainActivity.this, BuildBurgerActivity.class);
                intent.putExtra("user_name", userName);

                startActivity(intent);
            }
        });
    }

}