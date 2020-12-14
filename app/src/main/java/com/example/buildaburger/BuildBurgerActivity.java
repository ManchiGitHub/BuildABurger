package com.example.buildaburger;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

public class BuildBurgerActivity extends AppCompatActivity {

    LinearLayout burgerLayout;
    LinearLayout rateLayout;
    ScrollView scrollView;

    RatingBar ratingBar;

    private Animation rotateOpen;
    private Animation rotateClose;
    private Animation fromSide;
    private Animation toSide;

    String pattiesAmountSTR = "";
    String onionsAmountSTR = "";
    String picklesAmountSTR = "";
    String cheeseAmountSTR = "";

    int pattiesAmount;
    int onionsAmount;
    int picklesAmount;
    int cheeseAmount;
    int amount;

    boolean isKosher = false;
    boolean isClicked = false;
    boolean isFirstItem = true;

    CheckBox isKosherCheckBox;

    Button finishBtn;
    Button resetBtn;
    Button rateLayoutBtn;
    Button submitRating;
    Button orderBtn;
    ImageButton backBtn;

    FloatingActionButton fabBtn;
    FloatingActionButton aboutFabBtn;
    FloatingActionButton likeFABBtn;

    CustomSpinner pattiesSpinner;
    CustomSpinner onionsSpinner;
    CustomSpinner picklesSpinner;
    CustomSpinner cheeseSpinner;

    AlertDialog.Builder alert;
    EditText alertEditText;

    ImageView selectCheeseIMG;
    ImageView scrollArrow;

    TextView textCheese;
    TextView pattiesNumberTV;
    TextView onionsNumberTV;
    TextView picklesNumberTV;
    TextView cheeseNumberTV;
    TextView scrollHint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_burger);

        // animations
        rotateOpen = AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim);
        fromSide = AnimationUtils.loadAnimation(this, R.anim.from_side_anim);
        toSide = AnimationUtils.loadAnimation(this, R.anim.to_side_anim);

        // welcome user
        TextView userNameTV = findViewById(R.id.user_name_tv);
        userNameTV.setText(userNameTV.getText().toString() + getIntent().getStringExtra("user_name"));

        backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BuildBurgerActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });


        // the burger layout.
        burgerLayout = findViewById(R.id.burger_layout);

        // main FAB button. animated
        fabBtn = (FloatingActionButton) findViewById(R.id.fab_btn);
        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFABButtonClicked();
            }
        });

        // about FAB button, creates a toast. animated, toasts.
        aboutFabBtn = (FloatingActionButton) findViewById(R.id.about_btn);
        aboutFabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(BuildBurgerActivity.this, R.string.creator_text, Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        // like FAB button, creates an alert to like this app. animated, toasts.
        likeFABBtn = (FloatingActionButton) findViewById(R.id.rate_btn);
        likeFABBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(BuildBurgerActivity.this);

                builder.setCancelable(true);
                builder.setTitle(R.string.like);

                builder.setPositiveButton(R.string.alert_no_ans, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast toast = Toast.makeText(BuildBurgerActivity.this, R.string.alertSorry, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });

                builder.setNegativeButton(R.string.alert_like_ans, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast toast = Toast.makeText(BuildBurgerActivity.this, R.string.we_like_you, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //style id
                alertDialog.show();

                // aligning answers in alert dialog to center of window
                Button btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button btnNegative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);

                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
                layoutParams.weight = 5;
                btnPositive.setLayoutParams(layoutParams);
                btnNegative.setLayoutParams(layoutParams);
            }
        });

        // Number of ingredients to display.
        pattiesNumberTV = (TextView) findViewById(R.id.patties_amount_number);
        onionsNumberTV = (TextView) findViewById(R.id.onions_amount_number);
        picklesNumberTV = (TextView) findViewById(R.id.pickles_amount_number);
        cheeseNumberTV = (TextView) findViewById(R.id.cheese_amount_number);


        // adapter
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.spinner_items, R.layout.spinner_item);

        // drop down for patties.
        pattiesSpinner = findViewById(R.id.patties_spinner);
        pattiesSpinner.setAdapter(adapter);
        pattiesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                isFirstItem = position == 0;

                if (parent.getSelectedItem().toString().matches("[0-9]+")) {
                    pattiesAmountSTR = parent.getSelectedItem().toString();
                    pattiesAmount = Integer.parseInt(pattiesAmountSTR);
                    setAmountToDisplay(pattiesAmount, pattiesNumberTV);
                }
                else if (isFirstItem) {
                    return;
                }
                else {
                    selectAmountManually(R.string.patties_tv);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // drop down for onions.
        onionsSpinner = (CustomSpinner) findViewById(R.id.onions_spinner);
        onionsSpinner.setAdapter(adapter);
        onionsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                isFirstItem = position == 0;

                if (parent.getSelectedItem().toString().matches("[0-9]+")) {
                    onionsAmountSTR = parent.getSelectedItem().toString();
                    onionsAmount = Integer.parseInt(onionsAmountSTR);
                    setAmountToDisplay(onionsAmount, onionsNumberTV);
                }
                else if (isFirstItem) {
                    return;
                }
                else {
                    selectAmountManually(R.string.onions_tv);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // drop down for pickles.
        picklesSpinner = (CustomSpinner) findViewById(R.id.pickles_spinner);
        picklesSpinner.setAdapter(adapter);
        picklesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                isFirstItem = position == 0;

                if (parent.getSelectedItem().toString().matches("[0-9]+")) {
                    picklesAmountSTR = parent.getSelectedItem().toString();
                    picklesAmount = Integer.parseInt(picklesAmountSTR);
                    setAmountToDisplay(picklesAmount, picklesNumberTV);
                }
                else if (isFirstItem) {
                    return;

                }
                else {
                    selectAmountManually(R.string.pickles_tv);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // drop down for cheese.
        cheeseSpinner = (CustomSpinner) findViewById(R.id.cheese_spinner);
        cheeseSpinner.setAdapter(adapter);
        cheeseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                isFirstItem = position == 0;

                if (parent.getSelectedItem().toString().matches("[0-9]+")) {
                    cheeseAmountSTR = parent.getSelectedItem().toString();
                    cheeseAmount = Integer.parseInt(cheeseAmountSTR);
                    setAmountToDisplay(cheeseAmount, cheeseNumberTV);
                }
                else if (isFirstItem) {
                    return;
                }
                else {
                    selectAmountManually(R.string.cheese_tv);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // cheese refs for hiding the widgets on screen.
        selectCheeseIMG = (ImageView) findViewById(R.id.select_cheese_img);
        textCheese = findViewById(R.id.add_cheese_tv);

        // kosher checkbox and listener.
        isKosherCheckBox = (CheckBox) findViewById(R.id.kosher_checkbox);
        isKosherCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isKosher == false) {
                    cheeseAmount = 0;
                    cheeseAmountSTR = "";
                    cheeseNumberTV.setText(0 + "");
                    cheeseSpinner.setSelection(0);
                    selectCheeseIMG.setVisibility(View.GONE);
                    textCheese.setVisibility(View.GONE);
                    cheeseSpinner.setActivated(true);
                    cheeseSpinner.setVisibility(View.GONE);
                    cheeseNumberTV.setVisibility(View.GONE);
                    cheeseAmountSTR = "";
                    cheeseAmount = 0;
                    isKosher = true;
                }
                else {
                    selectCheeseIMG.setVisibility(View.VISIBLE);
                    textCheese.setVisibility(View.VISIBLE);
                    cheeseSpinner.setActivated(false);
                    cheeseSpinner.setVisibility(View.VISIBLE);
                    cheeseNumberTV.setVisibility(View.VISIBLE);
                    isKosher = false;
                }
            }
        });

        // rating refs and listener.
        rateLayout = findViewById(R.id.rate_layout);
        ratingBar = findViewById(R.id.rate_stars);
        rateLayoutBtn = findViewById(R.id.rate_layout_btn);
        rateLayoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rateLayout.getVisibility() == View.GONE) {
                    rateLayout.setVisibility(View.VISIBLE);
                }
                else if (rateLayout.getVisibility() == View.VISIBLE) {
                    rateLayout.setVisibility(View.GONE);
                }
            }
        });

        // submit rating button and listener. Toasts
        submitRating = findViewById(R.id.rate_btn_submit);
        submitRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float rating = ratingBar.getRating();

                if (ratingBar.getRating() == 5) {
                    Toast.makeText(getApplicationContext(), R.string.five_stars, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), rating + "", Toast.LENGTH_SHORT).show();
                }

                Toast.makeText(getApplicationContext(), R.string.thanks, Toast.LENGTH_SHORT).show();
            }
        });

        // scroll instructions for user.
        scrollHint = findViewById(R.id.scroll_hint);
        scrollArrow = findViewById(R.id.scroll_arrow);

        orderBtn = findViewById(R.id.order_btn);
        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), R.string.on_our_way, Toast.LENGTH_SHORT).show();
                orderBtn.setText(R.string.order_received);
                orderBtn.setClickable(false);
                orderBtn.setFocusable(false);
            }
        });

        // finish button. this will start building the burger, toasts.
        finishBtn = (Button) findViewById(R.id.finish_btn);
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pattiesAmount >= 0 && onionsAmount >= 0 && picklesAmount >= 0 && cheeseAmount >= 0) {
                    burgerLayout.setVisibility(View.VISIBLE);
                    finishBtn.setVisibility(View.GONE);
                    scrollHint.setVisibility(View.VISIBLE);
                    scrollArrow.setVisibility(View.VISIBLE);

                    // Start building the burger.
                    buildBurger();
                    orderBtn.setVisibility(View.VISIBLE);
                    orderBtn.setClickable(true);
                    orderBtn.setFocusable(true);
                }
                else {
                    Toast toast = Toast.makeText(BuildBurgerActivity.this, R.string.no_ingredients, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        // reset everything.
        resetBtn = (Button) findViewById(R.id.reset_btn);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                burgerLayout.setVisibility(View.INVISIBLE);
                isKosherCheckBox.setChecked(false);

                pattiesAmount = 0;
                onionsAmount = 0;
                picklesAmount = 0;
                cheeseAmount = 0;

                pattiesNumberTV.setText("");
                onionsNumberTV.setText("");
                picklesNumberTV.setText("");
                cheeseNumberTV.setText("");

                pattiesAmountSTR = "";
                onionsAmountSTR = "";
                picklesAmountSTR = "";
                cheeseAmountSTR = "";

                cheeseSpinner.setVisibility(View.VISIBLE);
                cheeseSpinner.setSelection(0);

                pattiesSpinner.setSelection(0);
                onionsSpinner.setSelection(0);
                picklesSpinner.setSelection(0);

                finishBtn.setVisibility(View.VISIBLE);
                scrollHint.setVisibility(View.GONE);
                scrollArrow.setVisibility(View.GONE);

                orderBtn.setText(R.string.order_now);
                orderBtn.setVisibility(View.GONE);

                burgerLayout.removeAllViews();
            }
        });
    }

    private void buildBurger() {

        // Top bun
        ImageView topBun = new ImageView(BuildBurgerActivity.this);
        topBun.setImageResource(R.drawable.top_bun);
        burgerLayout.addView(topBun);

        if (onionsAmount > 0) {
            addOnions();
        }

        if (picklesAmount > 0) {
            addPickles();
        }

        if (cheeseAmount > 0) {
            addCheese();
        }

        if (pattiesAmount > 0) {
            addPatties();
        }

        // Bottom bun
        ImageView bottomBun = new ImageView(BuildBurgerActivity.this);
        bottomBun.setImageResource(R.drawable.bottom_bun);
        burgerLayout.addView(bottomBun);

        ImageButton goUp = new ImageButton(getApplicationContext());
        goUp.setImageResource(R.drawable.ic_baseline_arrow_circle_up_24);
        goUp.setBackgroundColor(getResources().getColor(R.color.sunset));
        goUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                scrollView = findViewById(R.id.scroll_view);
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });

        burgerLayout.addView(goUp);
    }

    private void addCheese() {
        for (int i = 0; i < cheeseAmount; i++) {
            ImageView cheese = new ImageView(BuildBurgerActivity.this);
            cheese.setImageResource(R.drawable.cheese);
            burgerLayout.addView(cheese);
        }
    }

    private void addPickles() {
        for (int i = 0; i < picklesAmount; i++) {
            ImageView pickles = new ImageView(BuildBurgerActivity.this);
            pickles.setImageResource(R.drawable.pickles);
            burgerLayout.addView(pickles);
        }
    }

    private void addOnions() {
        for (int i = 0; i < onionsAmount; i++) {
            ImageView onions = new ImageView(BuildBurgerActivity.this);
            onions.setImageResource(R.drawable.onion);
            burgerLayout.addView(onions);
        }
    }

    private void addPatties() {

        for (int i = 0; i < pattiesAmount; i++) {
            ImageView patty = new ImageView(BuildBurgerActivity.this);
            patty.setImageResource(R.drawable.meat);
            burgerLayout.addView(patty);
        }
    }

    private void setAmountToDisplay(int numberToDisplay, TextView textView) {
        textView.setText(numberToDisplay + "");
    }

    // toasts
    private void selectAmountManually(int stringResTitle) {

        alert = new AlertDialog.Builder(BuildBurgerActivity.this);
        alertEditText = new EditText(BuildBurgerActivity.this);
        alertEditText.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        alert.setMessage(R.string.select_amount);
        alert.setTitle(stringResTitle);
        alert.setView(alertEditText);
        alert.setPositiveButton(R.string.alert_add, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (alertEditText.getText().toString().matches("[0-9]+")) {
                    String amountInputSTR = alertEditText.getText().toString();
                    amount = Integer.parseInt(amountInputSTR);
                    switchCaseOnIngredient(stringResTitle, amount);
                }
            }
        });

        alert.setNegativeButton(R.string.alert_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        AlertDialog alertDialog = alert.create();
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //style id
        alertDialog.show();
    }

    private void switchCaseOnIngredient(int ingredient, int amount) {

        switch (ingredient) {
            case R.string.patties_tv:
                pattiesAmount = amount;
                setAmountToDisplay(pattiesAmount, pattiesNumberTV);
                break;
            case R.string.onions_tv:
                onionsAmount = amount;
                setAmountToDisplay(onionsAmount, onionsNumberTV);
                break;
            case R.string.pickles_tv:
                picklesAmount = amount;
                setAmountToDisplay(picklesAmount, picklesNumberTV);
                break;
            case R.string.cheese_tv:
                cheeseAmount = amount;
                setAmountToDisplay(cheeseAmount, cheeseNumberTV);
                break;
        }
    }

    private void onFABButtonClicked() {
        setVisibilityAndClickability(isClicked);
        setAnimation(isClicked);
        isClicked = !isClicked;
    }

    private void setVisibilityAndClickability(boolean clicked) {
        if (!clicked) {
            aboutFabBtn.setVisibility(View.VISIBLE);
            likeFABBtn.setVisibility(View.VISIBLE);
            aboutFabBtn.setFocusable(true);
            likeFABBtn.setFocusable(true);
            aboutFabBtn.setClickable(true);
            likeFABBtn.setClickable(true);
        }
        else {
            aboutFabBtn.setVisibility(View.GONE);
            likeFABBtn.setVisibility(View.GONE);
            aboutFabBtn.setFocusable(false);
            likeFABBtn.setFocusable(false);
            aboutFabBtn.setClickable(false);
            likeFABBtn.setClickable(false);
        }
    }

    private void setAnimation(boolean clicked) {
        if (!clicked) {
            aboutFabBtn.startAnimation(fromSide);
            likeFABBtn.startAnimation(fromSide);
            fabBtn.startAnimation(rotateOpen);
        }
        else {
            aboutFabBtn.startAnimation(toSide);
            likeFABBtn.startAnimation(toSide);
            fabBtn.startAnimation(rotateClose);
        }
    }
}