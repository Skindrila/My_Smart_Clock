package com.example.skindrila.my_smart_clock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private AlarmManager alarmManager;
    private Calendar calendar;
    private Button alarmOnBtn;
    private Button alarmOffBtn;
    private TimePicker timePicker;
    private Intent intent;
    private PendingIntent pendingIntent;
    private TextView setTone;
    private TextView setNumberOfQ;
    private TextView datePicked;
    private TextView updateAlarmTxt;
    private boolean isSet = false;
    private boolean isSetDate = false;
    private int date, month, year;

    @Override
    protected void onResume() {
        super.onResume();
        int mode=MODE_PRIVATE;
        SharedPreferences mySharedPreferences=getSharedPreferences("com.example.skindrila.my_smart_clock.preference", mode);
        SharedPreferences.Editor editor=mySharedPreferences.edit();

        if(mySharedPreferences.contains("isTrue"))
        {
            cancelIntent();
            editor.remove("isTrue");
        }

        if(mySharedPreferences.contains("selectedTone"))
        {
            String tone = mySharedPreferences.getString("selectedTone","Tone1");
            confirmSelectedTone(tone);
            editor.remove("selectedTone");
        }

        if(mySharedPreferences.contains("newDate"))
        {
            date = Integer.parseInt(mySharedPreferences.getString("newDate","1"));
            month = Integer.parseInt(mySharedPreferences.getString("newMonth","1"));
            year = Integer.parseInt(mySharedPreferences.getString("newYear","2050"));
            String nowDate = String.valueOf(date) + "." + String.valueOf(month+1) + "." + String.valueOf(year);
            datePicked.setText(nowDate);
            editor.remove("newDate");
            editor.remove("newMonth");
            editor.remove("newYear");
        }
        editor.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Calendar now = Calendar.getInstance();
        date = now.get(Calendar.DATE);
        month = now.get(Calendar.MONTH);
        year = now.get(Calendar.YEAR);
        String nowDate = String.valueOf(date) + "." + String.valueOf(month+1) + "." + String.valueOf(year);
        datePicked = (TextView) findViewById(R.id.datePicked);
        datePicked.setText(nowDate);

        updateAlarmTxt = (TextView) findViewById(R.id.alarmUpdate);
        updateAlarmTxt.setBackgroundColor(Color.RED);

        timePicker = (TimePicker) findViewById(R.id.timePicker);

        calendar = Calendar.getInstance();

        intent = new Intent(this, AlarmReceiver.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT |Intent.FLAG_ACTIVITY_NEW_TASK);

        setTone = (TextView) findViewById(R.id.melodyText);
        setTone.setText("Rock");

        setNumberOfQ = (TextView) findViewById(R.id.textNumberOfQs);
        setNumberOfQ.setOnCreateContextMenuListener(this);
        setNumberOfQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNumberOfQ.performLongClick();
            }
        });
        setNumberOfQ.setText("1");

        alarmOnBtn = (Button) findViewById(R.id.onAlarm);
        alarmOnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isSet) return;
                isSet = true;

                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                calendar.set(Calendar.MINUTE, timePicker.getMinute());
                calendar.set(Calendar.DATE, date);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.YEAR,year);

                intent.putExtra("Tone", setTone.getText().toString());
                intent.putExtra("Qs", setNumberOfQ.getText().toString());

                int Hour = calendar.get(Calendar.HOUR_OF_DAY);
                int Minute = calendar.get(Calendar.MINUTE);
                String HourString = String.valueOf(Hour);
                String MinuteString = String.valueOf(Minute);
                if(Minute < 10) MinuteString = "0" + MinuteString;
                if(Hour < 10) HourString = "0" + HourString;

                checkTime();

                updateAlarmTxt.setText("Alarm set on " + HourString + ":"
                        + MinuteString + ", on day " + String.valueOf(date) + "."
                        + String.valueOf(month + 1) + "." + String.valueOf(year) + "!");
                updateAlarmTxt.setBackgroundColor(Color.GREEN);

                pendingIntent = PendingIntent.getBroadcast(MainActivity.this,0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        });

        alarmOffBtn = (Button) findViewById(R.id.offAlarm);
        alarmOffBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelIntent();
            }
        });
    }

    public void chooseDate(View view)
    {
        Intent i = new Intent(this, SetDataActivity.class);
        startActivity(i);
    }

    public void setTheTone(View view)
    {
        Intent i = new Intent(this, MusicPick.class);
        startActivity(i);
    }

    private void cancelIntent()
    {
        if(!isSet) return;
        isSet = false;
        Calendar now = Calendar.getInstance();
        date = now.get(Calendar.DATE);
        month = now.get(Calendar.MONTH);
        year = now.get(Calendar.YEAR);
        isSetDate = false;
        datePicked.setText(String.valueOf(date) + "." + String.valueOf(month+1) + "." + String.valueOf(year));
        updateAlarmTxt.setText("Alarm is not set!");
        updateAlarmTxt.setBackgroundColor(Color.RED);
        alarmManager.cancel(pendingIntent);
    }

    private void checkTime()
    {
        if(calendar.getTimeInMillis() < System.currentTimeMillis() && !isSetDate)
        {
            Toast.makeText(getApplicationContext(), "Alarm scheduled to tommorow!", Toast.LENGTH_LONG).show();

            if((date==28 && month == 1 && year%4 != 0)||(date==29 && month == 1 && year%4 == 0))
            {
                date = date + 1;
                month = month + 1;
            }
            else if(date == 30 && (month==3 || month == 5 || month == 8 ||month == 10))
            {
                date = date + 1;
                month = month + 1;
            }
            else if(date == 31)
            {
                if(month == 11)
                {
                    date = date + 1;
                    month = month + 1;
                    year = year + 1;
                }
                else
                {
                    date = date + 1;
                    month = month + 1;
                }
            }
            else
            {
                date = date + 1;
            }
            calendar.set(Calendar.DATE, date);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.YEAR, year);
        }
    }

    private void confirmSelectedTone(String tone)
    {
        switch (tone)
        {
            case "Tone1":
                setTone.setText("Rock");
                break;

            case "Tone2":
                setTone.setText("Hybrid");
                break;

            case "Tone3":
                setTone.setText("Nature");
                break;

            default:
                setTone.setText("Techno");
                break;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return menuChoice(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        createMenu(menu);
    }

    private void createMenu(Menu menu)
    {
        menu.setQwertyMode(true);
        MenuItem mnu1 = menu.add(0,0,0,"1");
        MenuItem mnu2 = menu.add(0,1,1,"2");
        MenuItem mnu3 = menu.add(0,2,2,"3");
    }

    private boolean menuChoice(MenuItem item)
    {
        switch(item.getItemId())
        {
            case 0:
                setNumberOfQ.setText("1");
                break;

            case 1:
                setNumberOfQ.setText("2");
                break;

            default:
                setNumberOfQ.setText("3");
                break;

        }
        return true;
    }
}

