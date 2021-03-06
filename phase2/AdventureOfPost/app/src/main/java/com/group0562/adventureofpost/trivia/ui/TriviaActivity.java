package com.group0562.adventureofpost.trivia.ui;

//import statements

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.group0562.adventureofpost.R;
import com.group0562.adventureofpost.trivia.Question;
import com.group0562.adventureofpost.trivia.TriviaPresenter;

/**
 * This is the main activity class of the Trivia game it will give questions to the user which they
 * will have to answer
 */
public class TriviaActivity extends AppCompatActivity {

    /**
     * an instance of Trivia
     */
    TriviaPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);

        //default operation and difficulty in case where no operator and difficulty is passed
        int diff = 1;
        int op = 1;

        //checks if intent has difficulty and operation to set values for game
        if (getIntent().hasExtra("diff") && getIntent().hasExtra("op")) {
            switch (getIntent().getStringExtra("diff")) {
                case "Easy":
                    diff = 1;
                    break;
                case "Medium":
                    diff = 2;
                    break;
                default:
                    diff = 3;
                    break;
            }
            switch (getIntent().getStringExtra("op")) {
                case "Addition":
                    op = 1;
                    break;
                case "Subtraction":
                    op = 2;
                    break;
                default:
                    op = 3;
                    break;
            }
        }

        //checks if a save was passed from some other activity, if so will load the save
        if (getIntent().hasExtra("saveState")) {
            presenter = new TriviaPresenter(getIntent().getStringExtra("username"), getIntent().getStringExtra("saveState"));
        } else {
            presenter = new TriviaPresenter(getIntent().getStringExtra("username"), op, diff);
        }

        //checks if user has save stored in database and will load it
        presenter.loadFromDatabase(this, getIntent().getStringExtra("username"));

        //checks for if settings activity has passed any settings that need to be changed
        if (getIntent().hasExtra("backgroundColor")) {
            presenter.setBackgroundColor(getIntent().getStringExtra("backgroundColor"));
        }
        if (getIntent().hasExtra("buttonColor")) {
            presenter.setButtonColor(getIntent().getStringExtra("buttonColor"));
        }
        if (getIntent().hasExtra("textColor")) {
            presenter.setTextColor(getIntent().getStringExtra("textColor"));
        }


        //changes view background color
        setActivityBackgroundColor();

        //calls helper to display questions
        onClickOptionHelper();

    }

    /**
     * Changes Background color of the game
     */
    public void setActivityBackgroundColor() {
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(getColorValue(presenter.getBackgroundColor()));
    }

    /**
     * Handles case where user clicks first button
     *
     * @param v the view
     */
    public void onClickA(View v) {
        presenter.onClick(0);
        onClickOptionHelper();
    }

    /**
     * Handles case where user clicks second button
     *
     * @param v the view
     */
    public void onClickB(View v) {
        presenter.onClick(1);
        onClickOptionHelper();
    }

    /**
     * Handles case where user clicks third button
     *
     * @param v the view
     */
    public void onClickC(View v) {
        presenter.onClick(2);
        onClickOptionHelper();
    }

    /**
     * Handles case where user clicks fourth button
     *
     * @param v the view
     */
    public void onClickD(View v) {
        presenter.onClick(3);
        onClickOptionHelper();
    }

    /**
     * Will take in color as a string and return Hex code for the color,
     * will return Hex code for white if color is invalid
     *
     * @param color the color as a string
     * @return color as an int
     */
    int getColorValue(String color) {
        int colorValue = 0xFFFFFFFF;
        switch (color) {
            case "Black":
                colorValue = 0XFF000000;
                break;
            case "White":
                colorValue = 0xFFFFFFFF;
                break;
            case "Blue":
                colorValue = 0xFF1CF7F5;
                break;
            case "Green":
                colorValue = 0xFF00FF00;
                break;
            case "Yellow":
                colorValue = 0xFFFFFF00;
                break;
            case "Red":
                colorValue = 0xFFF71C1C;
                break;
            case "Pink":
                colorValue = 0xFFF71CE6;
                break;
        }
        return colorValue;
    }

    /**
     * Semi-universal button helper function that updates the button, scores and question
     */
    public void onClickOptionHelper() {
        //checks if user has completed 10 questions
        if (presenter.hasNext()) {
            //gets questions form presenter
            Question q = presenter.getQuestion();
            String[] s = q.getOptions();

            //gets text color and texts on display
            TextView question = findViewById(R.id.textView3);
            question.setText(q.getQuestion());
            question.setTextColor(getColorValue(presenter.getTextColor()));

            Button buttonA = findViewById(R.id.button4);
            buttonA.setText(s[0]);
            buttonA.setTextColor(getColorValue(presenter.getTextColor()));
            buttonA.setBackgroundColor(getColorValue(presenter.getButtonColor()));

            Button buttonB = findViewById(R.id.button5);
            buttonB.setText(s[1]);
            buttonB.setTextColor(getColorValue(presenter.getTextColor()));
            buttonB.setBackgroundColor(getColorValue(presenter.getButtonColor()));

            Button buttonC = findViewById(R.id.button6);
            buttonC.setText(s[2]);
            buttonC.setTextColor(getColorValue(presenter.getTextColor()));
            buttonC.setBackgroundColor(getColorValue(presenter.getButtonColor()));

            Button buttonD = findViewById(R.id.button7);
            buttonD.setText(s[3]);
            buttonD.setTextColor(getColorValue(presenter.getTextColor()));
            buttonD.setBackgroundColor(getColorValue(presenter.getButtonColor()));


        } else {
            //if user has completed 10 questions will then move to end activity
            Intent intent = new Intent(this, TriviaEndActivity.class);
            intent.putExtra("saveState", presenter.saveGame());
            intent.putExtra("username", getIntent().getStringExtra("username"));
            startActivity(intent);
        }
    }

    /**
     * will start the settings activity for user to customize settings for game
     *
     * @param view View from previous activity
     */
    public void onClickSettings(View view) {
        Intent intent = new Intent(this, TriviaSettingsActivity.class);
        String saveState = presenter.saveGame();
        intent.putExtra("saveState", saveState);
        intent.putExtra("username", getIntent().getStringExtra("username"));
        startActivity(intent);
    }

    /**
     * Store the current game's info into user-specific data
     */
    public void onClickPause(View view) {
        Intent intent = new Intent(this, TriviaPauseActivity.class);
        intent.putExtra("saveState", presenter.saveGame());
        intent.putExtra("stats", presenter.getStats());
        intent.putExtra("username", getIntent().getStringExtra("username"));
        startActivity(intent);
    }

}
