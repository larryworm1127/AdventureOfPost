package com.group0562.adventureofpost.trivia;

import android.view.View;

import com.group0562.adventureofpost.Puzzles;

public class TriviaPresenter extends Puzzles {

    private Trivia game;
    private TriviaView view;
    private TriviaStats gameStats;

    public TriviaPresenter(TriviaView view, TriviaStats gameStats, int op, int diff) {
        super(gameStats);
        this.gameStats = gameStats;
        this.view = view;
        game = new Trivia(op, diff);

    }


    @Override
    public void checkComplete() {
        if(game.checkComplete())
            setPuzzleComplete(true);
    }

    public void onClick(int n){
        boolean correct = game.checkCorrect(n);
    }

    public Question getQuestion(){
        return game.getQuestion();
    }

    public boolean hasNext() {
        return game.hasNext();
    }

    @Override
    public void update() {
        view.updateStats();

        // Check complete
        checkComplete();
        if (getPuzzleComplete()) {
            onStop();
        }
    }

    public String saveGame() {
        return null;
    }

    public void loadGame() {

    }
    @Override
    public void onStop() {
        view.onGameComplete();
    }


}
