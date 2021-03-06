package com.group0562.adventureofpost.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;
import java.util.Map;

import static com.group0562.adventureofpost.shapeClicker.ShapeClickerStats.SC_STAT1;
import static com.group0562.adventureofpost.shapeClicker.ShapeClickerStats.SC_STAT2;
import static com.group0562.adventureofpost.shapeClicker.ShapeClickerStats.SC_STAT3;
import static com.group0562.adventureofpost.sudoku.SudokuStats.SUDOKU_STAT1;
import static com.group0562.adventureofpost.sudoku.SudokuStats.SUDOKU_STAT2;
import static com.group0562.adventureofpost.sudoku.SudokuStats.SUDOKU_STAT3;
import static com.group0562.adventureofpost.trivia.TriviaStats.TRIVIA_STAT1;
import static com.group0562.adventureofpost.trivia.TriviaStats.TRIVIA_STAT2;
import static com.group0562.adventureofpost.trivia.TriviaStats.TRIVIA_STAT3;

public class DatabaseHelper extends SQLiteOpenHelper {

    private final static String SUDOKU_TABLE = "sudokuStats";
    private final static String TRIVIA_TABLE = "triviaStats";
    private final String SC_TABLE = "shapeClickerStats";

    private AuthManager authManager;
    private GameManager gameManager;

    public DatabaseHelper(Context context) {
        super(context, "adventureOfPost.db", null, 1);

        authManager = new AuthManager();
        gameManager = new GameManager();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Auth table
        db.execSQL("CREATE TABLE users (username TEXT PRIMARY KEY, password TEXT)");

        // Sudoku stats table
        db.execSQL("CREATE TABLE " + SUDOKU_TABLE + " (id INTEGER PRIMARY KEY, " +
                SUDOKU_STAT1 + " INTEGER, " +
                SUDOKU_STAT2 + " INTEGER, " +
                SUDOKU_STAT3 + " INTEGER," +
                "username TEXT, FOREIGN KEY (username) REFERENCES users (username))"); // time, conflicts, moves

        // Trivia stats table
        db.execSQL("CREATE TABLE " + TRIVIA_TABLE + " (id INTEGER PRIMARY KEY, " +
                TRIVIA_STAT1 + " INTEGER, " +
                TRIVIA_STAT2 + " INTEGER, " +
                TRIVIA_STAT3 + " INTEGER, " +
                "username TEXT, FOREIGN KEY (username) REFERENCES users (username))"); // correct, incorrect, score

        // ShapeClicker stats table
        db.execSQL("CREATE TABLE " + SC_TABLE + " (id INTEGER PRIMARY KEY, " +
                SC_STAT1 + " INTEGER, " +
                SC_STAT2 + " INTEGER, " +
                SC_STAT3 + " INTEGER," +
                "username TEXT, FOREIGN KEY (username) REFERENCES users (username))"); // time, score, lives

        // table for storing saved game states
        db.execSQL("CREATE TABLE game_saves (gameId text primary key, username text, gameState text)");

        db.execSQL("CREATE TABLE game_stats (id text primary key, stats text)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS sudokuStats");
        db.execSQL("DROP TABLE IF EXISTS triviaStats");
        db.execSQL("DROP TABLE IF EXISTS shapeClickerStats");
        onCreate(db);
    }

    /**
     * retrieves the resumable game stats that were stored previously.
     *
     * @return returns a string representing the stats of the resumable game.
     */
    public String retrieveResumeStats() {
        SQLiteDatabase db = getWritableDatabase();
        try (Cursor cursor = db.rawQuery("SELECT * FROM game_stats WHERE id =?", new String[]{"resume_stats"})) {
            String game_stats = "";
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    game_stats = cursor.getString(cursor.getColumnIndex("stats"));
                    cursor.moveToNext();
                }
            }
            return game_stats;
        }
    }

    /**
     * retrieves a previously saved game state.
     *
     * @param gameId:   type of game
     * @param username: username of player
     * @return returns a string representing the saved game state.
     */
    public String retrieveGameState(String gameId, String username) {
        SQLiteDatabase db = getWritableDatabase();
        try (Cursor cursor = db.rawQuery("SELECT * FROM game_saves WHERE gameId = ? and username = ?", new String[]{gameId, username})) {
            String gameState = "";
            if (cursor.getCount() < 1) {
                return gameState;
            } else {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        gameState = cursor.getString(cursor.getColumnIndex("gameState"));
                        cursor.moveToNext();
                    }
                }
                return gameState;
            }
        }
    }

    /**
     * method for returning the stats across a given game.
     *
     * @param username: username of the player
     * @param game:     the type of game
     * @return returns a map containing all the player stats for a certain game.
     */
    public Map<String, List<Integer>> playerStats(String username, String game) {
        SQLiteDatabase db = getReadableDatabase();

        if (game.equals("sudoku")) {
            return gameManager.playerStats(db, username, SUDOKU_STAT1, SUDOKU_STAT2, SUDOKU_STAT3, SUDOKU_TABLE);
        } else if (game.equals("trivia")) {
            return gameManager.playerStats(db, username, TRIVIA_STAT1, TRIVIA_STAT2, TRIVIA_STAT3, TRIVIA_TABLE);
        } else {
            return gameManager.playerStats(db, username, SC_STAT1, SC_STAT2, SC_STAT3, SC_TABLE);
        }
    }

    public long insertTriviaStats(String username, int correct, int incorrect, int score) {
        SQLiteDatabase db = getWritableDatabase();
        return gameManager.insertStats(db, username, correct, incorrect, score, TRIVIA_STAT1,
                TRIVIA_STAT2, TRIVIA_STAT3, TRIVIA_TABLE);
    }

    public long insertShapeClickerStats(String username, long time, int points, int lives) {
        SQLiteDatabase db = getWritableDatabase();
        return gameManager.insertStats(db, username, (int) time, points, lives, SC_STAT1, SC_STAT2,
                SC_STAT3, SC_TABLE);
    }

    public long insertAuth(String username, String password) {
        SQLiteDatabase db = getWritableDatabase();
        return authManager.insertAuth(db, username, password);
    }

    public boolean checkUsernameDup(String username) {
        SQLiteDatabase db = getReadableDatabase();
        return authManager.checkUsernameDup(db, username);
    }

    public boolean verify(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();
        return authManager.verify(db, username, password);
    }

    public long insertGameState(String gameId, String username, String gameState) {
        SQLiteDatabase db = getWritableDatabase();
        return gameManager.insertGameState(db, gameId, username, gameState);
    }

    public long insertSudokuStats(String username, long time, int conflicts, int moves) {
        SQLiteDatabase db = getWritableDatabase();
        return gameManager.insertStats(db, username, (int) time, moves, conflicts,
                SUDOKU_STAT1, SUDOKU_STAT2, SUDOKU_STAT3, SUDOKU_TABLE);
    }

    public long insertResumeStats(String stats) {
        SQLiteDatabase db = getWritableDatabase();
        return gameManager.insertResumeStats(db, stats);
    }
}
