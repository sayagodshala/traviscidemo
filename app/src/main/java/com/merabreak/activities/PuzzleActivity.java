package com.merabreak.activities;

import android.os.Bundle;

import com.merabreak.BaseActivity;
import com.merabreak.games.puzzle.JigsawPuzzle;
import com.merabreak.games.puzzle.JigsawPuzzleConfiguration;
import com.merabreak.games.puzzle.PuzzleCompactSurface;

public class PuzzleActivity extends BaseActivity {

    private PuzzleCompactSurface puzzleSurface;

    private static final String TAG = "PuzzleActivity";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle config = JigsawPuzzleConfiguration.getRcatKittenExample();
        puzzleSurface = new PuzzleCompactSurface(this);
        JigsawPuzzle jigsawPuzzle = new JigsawPuzzle(this, config);
        puzzleSurface.setPuzzle(jigsawPuzzle);
        setContentView(puzzleSurface);
    }

    @Override
    protected void onPause() {
        super.onPause();
        puzzleSurface.getThread().pause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        puzzleSurface.getThread().saveState(outState);
    }
}