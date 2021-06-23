package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    EditText editTextField;
    Button editButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editTextField = findViewById(R.id.editTextField);
        editButton = findViewById(R.id.editButton);

        getSupportActionBar().setTitle("Edit Task");

        editTextField.setText(getIntent().getStringExtra(MainActivity.KEY_TASK_TEXT));

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // create an intent with the updates
                Intent intent = new Intent();

                // pass the data that was edited
                intent.putExtra(MainActivity.KEY_TASK_TEXT, editTextField.getText().toString());
                intent.putExtra(MainActivity.KEY_TASK_POSITION, getIntent().getExtras().getInt(MainActivity.KEY_TASK_POSITION));

                // set the result of the intent
                setResult(RESULT_OK, intent);

                // finish activity, close the screen and go back
                finish();
            }
        });

    }
}