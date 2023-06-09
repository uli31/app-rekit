package com.example.rekit;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;



import androidx.fragment.app.FragmentActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends FragmentActivity {

    private Button btnStart;
    private Button btnReset;
    private TextView txtCounter;
    private TextView txtSelectedDate;
    private TextView txtTimeRemaining;

    private int counter = 0;
    private Calendar selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = findViewById(R.id.btn_start);
        btnReset = findViewById(R.id.btn_reset);
        txtCounter = findViewById(R.id.txt_counter);
        txtSelectedDate = findViewById(R.id.txt_selected_date);
        txtTimeRemaining = findViewById(R.id.txt_time_remaining);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedDate != null) {
                    startCounting();
                } else {
                    showDatePicker();
                }
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetCount();
            }
        });
    }

    private void showDatePicker() {
        DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                selectedDate = Calendar.getInstance();
                selectedDate.set(Calendar.YEAR, year);
                selectedDate.set(Calendar.MONTH, month);
                selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", new Locale("es", "ES"));
                String selectedDateString = sdf.format(selectedDate.getTime());
                txtSelectedDate.setText(getString(R.string.selected_date, selectedDateString));
             }
        };

        Calendar currentDate = Calendar.getInstance();
        int currentYear = currentDate.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH);
        int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                MainActivity.this,
                dateListener,
                currentYear,
                currentMonth,
                currentDay
        );
        datePickerDialog.show();
    }

    private void startCounting() {
        long currentTime = System.currentTimeMillis();
        long selectedTime = selectedDate.getTimeInMillis();

        long timeDifference = currentTime - selectedTime;
        int daysPassed = (int) (timeDifference / (24 * 60 * 60 * 1000));

        counter = daysPassed;
        updateCounter();

        btnStart.setEnabled(false);
    }

    private void resetCount() {
        counter = 0;
        selectedDate = null;
        updateCounter();
        txtSelectedDate.setText(getString(R.string.select_date));
        btnStart.setEnabled(true);
    }

    private void updateCounter() {
        txtCounter.setText(String.valueOf(counter));

        if (selectedDate != null) {
            Calendar currentDate = Calendar.getInstance();
            currentDate.set(Calendar.HOUR_OF_DAY, 0);
            currentDate.set(Calendar.MINUTE, 0);
            currentDate.set(Calendar.SECOND, 0);
            currentDate.set(Calendar.MILLISECOND, 0);

            Calendar nextChangeDate = (Calendar) selectedDate.clone();
            nextChangeDate.add(Calendar.DAY_OF_MONTH, counter + 1);
            nextChangeDate.set(Calendar.HOUR_OF_DAY, 0);
            nextChangeDate.set(Calendar.MINUTE, 0);
            nextChangeDate.set(Calendar.SECOND, 0);
            nextChangeDate.set(Calendar.MILLISECOND, 0);

            long timeDifference = nextChangeDate.getTimeInMillis() - currentDate.getTimeInMillis();
            int daysRemaining = (int) (timeDifference / (24 * 60 * 60 * 1000));

            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", new Locale("es", "ES"));
            String nextChangeDateString = sdf.format(nextChangeDate.getTime());

            txtTimeRemaining.setText(getString(R.string.time_remaining, daysRemaining));
            txtTimeRemaining.setVisibility(View.VISIBLE);
            txtSelectedDate.setText(nextChangeDateString);
        } else {
            txtTimeRemaining.setVisibility(View.GONE);
        }
    }
}
