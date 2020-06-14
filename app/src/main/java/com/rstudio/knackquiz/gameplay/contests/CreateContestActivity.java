package com.rstudio.knackquiz.gameplay.contests;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.rstudio.knackquiz.R;

import java.util.Calendar;

public class CreateContestActivity extends AppCompatActivity {

    private Button btDate, btTime, btCreate;
    private TextView tvSelectedDate, tvSelectedTime;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;

    //Time
    private int mYear, mMonth, mDay, mHour, mMinute, mSeconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contest);

        initValues();
        setDateDialogues();
        setListeners();

    }

    private void setListeners() {
        btDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        tvSelectedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        btTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.show();
            }
        });

        tvSelectedTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.show();
            }
        });

    }

    private void setDateDialogues() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        mSeconds = c.get(Calendar.SECOND);

        datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        tvSelectedDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        btDate.setVisibility(View.GONE);
                    }
                }, mYear, mMonth, mDay);

        timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        tvSelectedDate.setText(hourOfDay + ":" + minute);
                        tvSelectedTime.setVisibility(View.VISIBLE);
                        btTime.setVisibility(View.GONE);
                    }
                }, mHour, mMinute, false);

    }

    private void initValues() {
        btDate = findViewById(R.id.bt_selectDateCreateContest);
        btTime = findViewById(R.id.bt_selectTimeCreateContest);


        tvSelectedDate = findViewById(R.id.tv_dateCreateContest);
        tvSelectedTime = findViewById(R.id.tv_timeCreateContest);

    }
}
