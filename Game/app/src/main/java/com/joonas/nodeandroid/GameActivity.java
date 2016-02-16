package com.joonas.nodeandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameActivity extends Activity implements View.OnClickListener{

    private TextView num1Tv, num2Tv, notationTv, correctTv, incorrectTv, scoreTv, timerTv;
    private Button equalBtn, biggerBtn, smallerBtn, quitBtn, newGameBtn;
    private int result, correct, incorrect;
    int scorenum = 3;
    boolean isPlaying = false;
    String scoretxt = "8";
    List<NameValuePair> params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        init();
    }

    private void init() {

        num1Tv = (TextView) findViewById(R.id.tvNum1);
        num2Tv = (TextView) findViewById(R.id.tvNum2);
        notationTv = (TextView) findViewById(R.id.tvNotation);
        correctTv = (TextView) findViewById(R.id.tvCorrect);
        incorrectTv = (TextView) findViewById(R.id.tvIncorrect);
        scoreTv = (TextView) findViewById(R.id.tvScore);
        timerTv = (TextView) findViewById(R.id.tvTimer);

        biggerBtn = (Button)findViewById(R.id.btnBigger);
        smallerBtn = (Button)findViewById(R.id.btnSmaller);
        equalBtn = (Button)findViewById(R.id.btnEqual);
        quitBtn = (Button)findViewById(R.id.btnQuit);
        newGameBtn = (Button)findViewById(R.id.btnNewGame);

        biggerBtn.setOnClickListener(this);
        smallerBtn.setOnClickListener(this);
        equalBtn.setOnClickListener(this);
        quitBtn.setOnClickListener(this);
        newGameBtn.setOnClickListener(this);

        hideButtons();
    }

    public static int randInt(int min, int max) {

        Random rand = new Random();
        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public int newCalculation() {

        int i1 = randInt(1, 5);
        int i2 = randInt(1, 5);

        String rand1 = String.valueOf(i1);
        String rand2 = String.valueOf(i2);

        num1Tv.setText(rand1);
        num2Tv.setText(rand2);

        correctTv.setText("Correct answers: " + correct);
        incorrectTv.setText("Wrong answers: " + incorrect);
        scoreTv.setText("Score: " + scorenum);

        if (i1 < i2) {
            result = 1;
            return result;
        } else if (i1 > i2) {
            result = 2;
            return result;
        } else {
            result = 3;
            return result;
        }
    }

    public void answerCorrect(boolean bool){
        if(bool == true){
            correct++;
            scorenum++;
            Toast.makeText(GameActivity.this, "Answer is correct!", Toast.LENGTH_SHORT).show();
        }
        else{
            incorrect++;
            scorenum--;
            Toast.makeText(GameActivity.this, "Answer is incorrect!", Toast.LENGTH_SHORT).show();
        }
        newCalculation();
    }

    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btnSmaller:
                if(result == 1) {
                    answerCorrect(true);
                }
                else{
                    answerCorrect(false);
                }
                break;
            case R.id.btnBigger:
                if(result == 2){
                    answerCorrect(true);
                }
                else{
                    answerCorrect(false);
                }
                break;
            case R.id.btnEqual:
                if(result == 3){
                    answerCorrect(true);
                }
                else{
                    answerCorrect(false);
                }
                break;
            case R.id.btnQuit:
                startActivity(new Intent(GameActivity.this, LoginActivity.class));
                this.finish();
                break;
            case R.id.btnNewGame:
                sendScores();
                newCalculation();
                timer();
                biggerBtn.setVisibility(View.VISIBLE);
                equalBtn.setVisibility(View.VISIBLE);
                smallerBtn.setVisibility(View.VISIBLE);
                break;
        }

    }

    public void hideButtons() {
        biggerBtn.setVisibility(View.INVISIBLE);
        equalBtn.setVisibility(View.INVISIBLE);
        smallerBtn.setVisibility(View.INVISIBLE);
    }

    public void sendScores() {
       // scoretxt = String.valueOf(scorenum);
        //token = pref.getString("token", "");
        params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("score", scoretxt));
        //params.add(new BasicNameValuePair("age", scoretxt));
        //params.add(new BasicNameValuePair("id", token));
        ServerRequest sr = new ServerRequest();
        JSONObject json = sr.getJSON("http://10.0.2.2:8080/sendScores",params);
        //JSONObject json = sr.getJSON("http://192.168.56.1:8080/register",params);

        if(json != null){
            try{
                String jsonstr = json.getString("response");

               // Toast.makeText(getApplication(),jsonstr,Toast.LENGTH_LONG).show();
                Toast.makeText(GameActivity.this, "Score sent to database", Toast.LENGTH_SHORT).show();
                Log.d("Hello", jsonstr);
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    public void timer() {
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                timerTv.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                //   textView4.setText("done!");
                //   Toast.makeText(GameActivity.this, "Time's up", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(GameActivity.this, GameActivity.class));

            }
        }.start();
    }


}
