package com.group0562.adventureofpost.sudoku.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.group0562.adventureofpost.GameActivity;
import com.group0562.adventureofpost.R;
import com.group0562.adventureofpost.sudoku.SudokuPresenter;
import com.group0562.adventureofpost.sudoku.SudokuView;

import java.util.List;

public class SudokuActivity extends AppCompatActivity implements
        SudokuCellFragment.OnFragmentInteractionListener,
        SudokuStatsFragment.OnFragmentInteractionListener, SudokuView {

    private SudokuPresenter presenter;

    private SudokuCellFragment cellGroupFrag;
    private SudokuStatsFragment statsFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);

        presenter = new SudokuPresenter(getResources().openRawResource(R.raw.sudoku), this);
        cellGroupFrag = (SudokuCellFragment) getSupportFragmentManager().findFragmentById(R.id.boardFragment);
        statsFrag = (SudokuStatsFragment) getSupportFragmentManager().findFragmentById(R.id.statsFragment);

        // Display initial board values
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                int cellValue = presenter.getCellValue(row, col);
                if (cellValue != 0) {
                    cellGroupFrag.loadValues(cellValue, row, col);
                }
            }
        }
    }

    public void onClickNumpad(View view) {
        Button numClicked = (Button) view;
        int newValue = Integer.parseInt(numClicked.getTag().toString());

        // Update backend board
        boolean updateSuccess = presenter.addNum(newValue);

        // Load value on board
        if (updateSuccess) {
            cellGroupFrag.loadValues(newValue, presenter.getCurrRow(), presenter.getCurrCol());
            updateStats(true, false);
        } else {
            Toast.makeText(getApplicationContext(), "Conflict detected!", Toast.LENGTH_SHORT).show();
            updateStats(false, true);
        }

        // Update
        presenter.update();
    }

    public void onClickRemove(View view) {
        presenter.removeNum();
        cellGroupFrag.removeValue(presenter.getCurrRow(), presenter.getCurrCol());

        // Update
        presenter.update();
    }

    public void onClickReset(View view) {
        List<int[]> resetCells = presenter.resetGameBoard();
        for (int[] cellLoc : resetCells) {
            cellGroupFrag.removeValue(cellLoc[0], cellLoc[1]);
        }

        // Update
        updateStats(false, false);
        presenter.update();
    }

    public void updateStats(boolean newMove, boolean newConflict) {
        if (newMove) {
            presenter.addMoves();
        }

        if (newConflict) {
            presenter.addConflicts();
        }

        int moves = presenter.getMoves();
        int conflicts = presenter.getConflicts();
        statsFrag.updateStats(moves, conflicts);
    }

    public void endDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getApplicationContext());
        dialogBuilder.setTitle("Puzzle Completed!")
                .setMessage("Congratulation! You have completed the puzzle.")
                .setPositiveButton("Restart", (dialog, which) -> this.recreate())
                .setNeutralButton("Home", (dialog, which) -> {
                    Intent intent = new Intent(this, GameActivity.class);
                    this.startActivity(intent);
                });

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    @Override
    public void onFragmentInteraction(int row, int col, View view) {
        if (presenter.getCellLocked(row, col)) {
            Toast.makeText(getApplicationContext(), "Can not change start piece", Toast.LENGTH_SHORT).show();
        } else {
            cellGroupFrag.loadBackground(presenter.getCurrRow(), presenter.getCurrCol(), R.drawable.table_border_cell);
            view.setBackgroundResource(R.drawable.table_selected_cell);
            presenter.setCurrRow(row);
            presenter.setCurrCol(col);
        }
    }

    @Override
    public void onFragmentInteraction(View view) {

    }

    @Override
    public void onGameComplete() {
        endDialog();
    }
}
