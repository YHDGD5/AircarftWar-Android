package com.example.aircraftwar2024.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aircraftwar2024.R;

public class EndActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        TextView yourScoreTextView = findViewById(R.id.textViewYourScoreValue);
        TextView opponentScoreTextView = findViewById(R.id.textViewOpponentScoreValue);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int yourScore = extras.getInt("YOUR_SCORE", 0);
            int opponentScore = extras.getInt("OPPONENT_SCORE", 0);

            yourScoreTextView.setText(String.valueOf(yourScore));
            opponentScoreTextView.setText(String.valueOf(opponentScore));
        }
    }
}
