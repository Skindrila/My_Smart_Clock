package com.example.skindrila.my_smart_clock;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.skindrila.my_smart_clock.R;

import java.util.Random;

public class SetTaskActivity extends AppCompatActivity {

    private MyDBHandler myDBHandler;

    private EditText answerText;
    private TextView questionText;
    private String answer;
    private String question;
    private int correct;
    private TextView txtRemaining;

    @Override
    public void onBackPressed() {}

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView(R.layout.activity_main_task);

        Bundle data = getIntent().getExtras();
        final int numOfQs = Integer.valueOf(data.getString("Qs"));
        correct = 0;

        final Button RngToneOffBtn = (Button) findViewById(R.id.RngToneOffBtn);
        RngToneOffBtn.setText("Submit answer");
        txtRemaining = (TextView) findViewById(R.id.txtRemaining);
        txtRemaining.setText("Remaining correct answers needed: " + numOfQs);
        questionText = (TextView) findViewById(R.id.questionText);
        answerText = (EditText) findViewById(R.id.answerText);
        myDBHandler = new MyDBHandler(this, null, null, 1);
        final Intent intent = new Intent(this, AlarmReceiver.class);
        answerText.setText("");

        Random r = new Random();
        int id = r.nextInt(100) % 50;

        while(id > 49 && id < 0 ) {
            id = r.nextInt(100)%50;
        }
        question = myDBHandler.getQuestion(id);
        answer = myDBHandler.getAnswer(id);

        questionText.setText(question);
        RngToneOffBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ans = answerText.getText().toString();
                if (ans.compareTo(answer) == 0) {
                    correct += 1;
                    if(numOfQs - correct == 1)
                        RngToneOffBtn.setText("Submit answer to stop alarm");
                    if(correct==numOfQs)
                    {
                        int mode=MODE_PRIVATE;
                        SharedPreferences mySharedPreferences=getSharedPreferences("com.example.skindrila.my_smart_clock.preference", mode);
                        SharedPreferences.Editor editor=mySharedPreferences.edit();
                        editor.putBoolean("isTrue", true);
                        editor.commit();
                        sendBroadcast(intent);
                        Toast.makeText(getApplicationContext(), "Alarm successfully turned off!", Toast.LENGTH_LONG).show();
                        finish();
                    }
                    else
                    {
                        txtRemaining.setText("Remaining correct answers needed: " + (numOfQs-correct));
                        answerText.setText("");
                        Toast.makeText(getApplicationContext(), "Correct answer, " + (numOfQs - correct) + " more!", Toast.LENGTH_LONG).show();
                        Random r = new Random();
                        int id = r.nextInt(100) % 50;
                        while(id > 49 && id < 0 ) {
                            id = r.nextInt(100)%50;
                        }
                        question = myDBHandler.getQuestion(id);
                        answer = myDBHandler.getAnswer(id);
                        questionText.setText(question);
                    }
                }
                else {
                    answerText.setText("");
                    Toast.makeText(getApplicationContext(), "Wrong answer!", Toast.LENGTH_LONG).show();
                    Random r = new Random();
                    int id = r.nextInt(100) % 50;
                    while(id > 49 && id < 0 ) {
                        id = r.nextInt(100)%50;
                    }
                    question = myDBHandler.getQuestion(id);
                    answer = myDBHandler.getAnswer(id);
                    questionText.setText(question);
                }
            }
        });
    }
}