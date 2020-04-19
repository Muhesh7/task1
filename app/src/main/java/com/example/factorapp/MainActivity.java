package com.example.factorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    Button mGoButton;
    int mCorrectOption;
    Button mOptionA;
    Button mOptionB;
    Button mOptionC;
    TextView mscore;
    TextView mtime;
    TextView mstreak;
    TextView mend;
    EditText numt;
    int CorrectCount = 0;
    int TotalCount = 0;
    int StreakCount =0;
    int CorrectAnsVal;
    CountDownTimer mCountDownTimer;
    long mTimeAtInstant;
    boolean mCountRunning =false;
    int mQuestionAnsORNot =0;
    int mHighestStreak =0;
    long mti =25900;
    long mtemp;

    int[] options = {1, 1, 1};

    public void click(View view) {
        numt = (EditText) findViewById(R.id.editText);
        if(numt.getText().toString().isEmpty()){
            Toast.makeText(this,"enter a number",Toast.LENGTH_LONG).show();}
        else
            if(Integer.parseInt(numt.getText().toString())<0){
                Toast.makeText(this,"type positive number",Toast.LENGTH_LONG).show();
            }
       else {
            int n = Integer.parseInt(numt.getText().toString());
            if (mCountRunning)
            {
                mCountDownTimer.cancel();}
            timer(mti);
            mCountRunning = true;
            view = this.getWindow().getDecorView();
            view.setBackgroundColor(Color.WHITE);
            Random random = new Random();

            CorrectAnsVal = n;

            mCorrectOption = random.nextInt(3);

            for (int j = 0; j < n; j++) {
                int a = random.nextInt(n) + 1;
                if (n % a == 0) {
                    CorrectAnsVal = a;
                    break;
                }
            }

            for (int i = 0; i < 3; ++i) {
                if (i == mCorrectOption)
                    options[i] = CorrectAnsVal;
                else {
                    int wrans = random.nextInt(n) + 1;
                    while (n % wrans == 0) {
                        wrans = random.nextInt(n) + 1;
                    }
                    options[i] = wrans;
                }
            }
            mOptionA.setText(Integer.toString(options[0]));
            mOptionB.setText(Integer.toString(options[1]));
            mOptionC.setText(Integer.toString(options[2]));
            mQuestionAnsORNot =1;
        }


    }

    public void answer(View view) {
        if(mQuestionAnsORNot ==1) {
            mQuestionAnsORNot = 0;
            int pos = Integer.parseInt(view.getTag().toString());
            ++TotalCount;
            if (mCorrectOption == pos) {
                Toast.makeText(getApplicationContext(), "correct", Toast.LENGTH_SHORT).show();
                view = this.getWindow().getDecorView();
                view.setBackgroundColor(Color.GREEN);
                ++CorrectCount;
                StreakCount++;

            } else {
                Toast.makeText(getApplicationContext(), "Wrong, the correct answer is" + " " + CorrectAnsVal, Toast.LENGTH_SHORT).show();

                view = this.getWindow().getDecorView();
                view.setBackgroundColor(Color.RED);
                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                if (Build.VERSION.SDK_INT >= 26) {
                    vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibrator.vibrate(200);
                    StreakCount = 0;
                }
            }
            mscore.setText(" " + Integer.toString(CorrectCount) + "/" + Integer.toString(TotalCount));
            mCountDownTimer.cancel();
            mCountRunning = false;

            if (mHighestStreak <= StreakCount) {
                mHighestStreak = StreakCount;
            }
            mstreak.setText(Integer.toString(mHighestStreak) + "-" + "0");
            save();

        }
        else
            Toast.makeText(getApplicationContext(), "press go to proceed", Toast.LENGTH_SHORT).show();

        }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            CorrectCount = savedInstanceState.getInt("answer");
            TotalCount = savedInstanceState.getInt("total");
           mHighestStreak =savedInstanceState.getInt("Streak");

        } else {
            CorrectCount = 0;
            TotalCount = 0;
            mTimeAtInstant =15900;
        }

        mOptionA = (Button) findViewById(R.id.b1);
        mOptionB = (Button) findViewById(R.id.b2);
        mOptionC = (Button) findViewById(R.id.b3);
        mGoButton = (Button) findViewById(R.id.but);
        mscore = (TextView) findViewById(R.id.score);
        mstreak = (TextView) findViewById(R.id.streak);
        mtime = (TextView) findViewById(R.id.time);
        mend = (TextView)  findViewById(R.id.end);
        mscore.setText(" " + Integer.toString(CorrectCount) + "/" + Integer.toString(TotalCount));
        load();
    }



    public void timer(long mti) {
       mCountDownTimer = new CountDownTimer(mti, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                mtime.setText(" " + String.valueOf(millisUntilFinished/ 1000) + "s");
                mTimeAtInstant = millisUntilFinished;
                mCountRunning = true;

            }

           @Override
            public void onFinish() {
                if(mQuestionAnsORNot ==0)
                dialog();

            }


        }.start();


    }

 public void dialog()
 {AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
     alertDialog.setTitle("Game over");
     alertDialog.setMessage("score:" + Integer.toString(CorrectCount) + "/" + Integer.toString(TotalCount));
     alertDialog.setCancelable(false);
     alertDialog.setPositiveButton("close application", new DialogInterface.OnClickListener() {
         @Override
         public void onClick(DialogInterface dialog, int which) {
             finish();

         }
     });
     AlertDialog Dialog  = alertDialog.create();
     Dialog.show();}
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("answer", CorrectCount);
        outState.putInt("total", TotalCount);
        outState.putInt("Streak", mHighestStreak);
        outState.putLong("instime", mTimeAtInstant);
        outState.putBoolean("tof", mCountRunning);
        outState.putInt("one", options[0]);
        outState.putInt("two", options[1]);
        outState.putInt("three", options[2]);
        outState.putInt("ansloc", mCorrectOption);
        outState.putInt("ans", CorrectAnsVal);
        outState.putInt("go", mQuestionAnsORNot);

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mTimeAtInstant =savedInstanceState.getLong("instime");
        mCountRunning =savedInstanceState.getBoolean("tof");
        mQuestionAnsORNot =savedInstanceState.getInt("go");
        mCorrectOption = savedInstanceState.getInt("ansloc");
        CorrectAnsVal =savedInstanceState.getInt("ans");
        options[0]= savedInstanceState.getInt("one");
        options[1]= savedInstanceState.getInt("two");
        options[2]= savedInstanceState.getInt("three");


        if(mCountRunning)
        {
            mtemp= mTimeAtInstant;
            timer(mtemp); }
        mOptionA.setText(Integer.toString(options[0]));
        mOptionB.setText(Integer.toString(options[1]));
        mOptionC.setText(Integer.toString(options[2]));
    }
    public void save(){
        SharedPreferences sharedPreferences = getSharedPreferences("STREAK",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("streak", mHighestStreak);
        editor.apply();

    }
    public void load(){
        SharedPreferences sharedPreferences = getSharedPreferences("STREAK",MODE_PRIVATE);
        mHighestStreak = sharedPreferences.getInt ("streak",0);
        mstreak.setText(Integer.toString(mHighestStreak) + "-" + "0");
    }



}

