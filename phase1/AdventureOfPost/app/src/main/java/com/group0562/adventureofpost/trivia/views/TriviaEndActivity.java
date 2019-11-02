package com.group0562.adventureofpost.trivia.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.group0562.adventureofpost.GameActivity;
import com.group0562.adventureofpost.R;

public class TriviaEndActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia_end);
        int[] values = getIntent().getIntArrayExtra("stats");



        //the player stats
        String correct, incorrect, score;
        correct = "correct: " + values[0];
        incorrect = "Incorrect: " + values[1];
        score = "Score: " + values[2];


        //display the player stats on the user screen
        TextView correctTextView = (TextView) findViewById(R.id.Correct);
        correctTextView.setText(correct);
        TextView incorrectTextView = (TextView) findViewById(R.id.Incorrect);
        incorrectTextView.setText(incorrect);
        TextView scoreTextView = (TextView) findViewById(R.id.Score);
        scoreTextView.setText(score);

    }

    //returns the user to the game screen, readies them to play another puzzle
    public void onClickNext(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }
}