package com.tec9rushbgroup.ourspace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class AddAnniversary extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private Button addAnniversary, backToAnniversaries,btnDatePicker;
    private TextView welcomeTV, sloganTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_anniversary);

        btnDatePicker = findViewById(R.id.btnDatePicker);


        welcomeTV = findViewById(R.id.welcome_text);
        sloganTV = findViewById(R.id.slogan_text);
        welcomeTV.setTypeface(Typeface.createFromAsset(getAssets(), "logo.ttf"));
        sloganTV.setTypeface(Typeface.createFromAsset(getAssets(), "slogan.ttf"));

        //Buttons
        addAnniversary = findViewById(R.id.add_anniversary_button);
        backToAnniversaries = findViewById(R.id.back_to_anniversaries);

        String uid = getIntent().getStringExtra("uid");
        String user1 = getIntent().getStringExtra("user1");
        String user2 = getIntent().getStringExtra("user2");
        backToAnniversaries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddAnniversary.this, AnniversaryPage.class);
                intent.putExtra("uid",uid);
                intent.putExtra("user1",user1);
                intent.putExtra("user2",user2);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
            }
        });
        btnDatePicker.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"date picker");
            }
        });

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

        String pickerDateString = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
//        TextView tvDatePicker = findViewById(R.id.textViewContent);
//        CountdownView myCountdownView = findViewById(R.id.countdownView);
//
//        try{
//            tvDatePicker.setText(pickerDateString);
//            Date now = new Date();
//
//            long currentDate = now.getTime();
//            long pickerDate = calendar.getTimeInMillis();
//            long countDownToPickerDate = pickerDate - currentDate;
//            myCountdownView.start(countDownToPickerDate);
//        } catch(Exception e){
//            e.printStackTrace();
//        }
    }
    //    public static CharSequence getCountdownText(Context context, Date futureDate){
//        StringBuilder countdownText = new StringBuilder();
//
//        long timeRemaining = futureDate.getTime() - new Date().getTime();
//
//        if(timeRemaining > 0){
//            Resources resources = context.getResources();
//
//            //Calculate the days, hours, minutes and seconds until anniversary
//            //We also have to make sure to subtract from the total value or else we won't get proper values
//            int days = (int) TimeUnit.MILLISECONDS.toDays(timeRemaining);
//            timeRemaining -= TimeUnit.MILLISECONDS.toDays(timeRemaining);
//            int hours = (int) TimeUnit.MILLISECONDS.toHours(timeRemaining);
//            timeRemaining -= TimeUnit.MILLISECONDS.toHours(timeRemaining);
//            int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(timeRemaining);
//            timeRemaining -= TimeUnit.MILLISECONDS.toMinutes(timeRemaining);
//            int seconds = (int)TimeUnit.MILLISECONDS.toSeconds(timeRemaining);
//
//
//            //now add these quantities to a string
//            if(days > 0){
//                countdownText.append(resources.getQuantityString(R.plurals.days,days,days));
//                countdownText.append(" ");
//            }
//            if(hours > 0){
//                countdownText.append(resources.getQuantityString(R.plurals.hours,hours,hours));
//                countdownText.append(" ");
//            }
//            if(minutes > 0){
//                countdownText.append(resources.getQuantityString(R.plurals.minutes,minutes,minutes));
//                countdownText.append(" ");
//            }
//            if(seconds > 0){
//                countdownText.append(resources.getQuantityString(R.plurals.seconds,seconds,seconds));
//                countdownText.append(" ");
//            }
//        }
//        return countdownText.toString();
//    }

    @Override
    public void onBackPressed() {
        String uid = getIntent().getStringExtra("uid");
        String user1 = getIntent().getStringExtra("user1");
        String user2 = getIntent().getStringExtra("user2");
        Intent intent = new Intent(AddAnniversary.this, AnniversaryPage.class);
        intent.putExtra("uid",uid);
        intent.putExtra("user1",user1);
        intent.putExtra("user2",user2);
        startActivity(intent);
        overridePendingTransition(0,0);
        finish();
    }
}