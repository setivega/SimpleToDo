package com.example.simpletodo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.apache.commons.io.FileUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_TASK_TEXT = "task_text";
    public static final String KEY_TASK_POSITION = "task_position";
    public static final int EDIT_TEXT_CODE = 20;

    List<String> tasks;

    FloatingActionButton addButton;
    EditText addTextField;
    RecyclerView taskList;
    TasksAdapter tasksAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addButton = findViewById(R.id.addButton);
        addTextField = findViewById(R.id.addTextField);
        taskList = findViewById(R.id.taskList);

        getSupportActionBar().setTitle("To Do List");

        loadTasks();

        TasksAdapter.OnLongClickListener onLongClickListener = new TasksAdapter.OnLongClickListener() {
            @Override
            public void onTaskLongClicked(int position) {
                //Delete the item from the model
                tasks.remove(position);
                //Notify the adapter
                tasksAdapter.notifyItemRemoved(position);
                Toast.makeText(MainActivity.this, "Task Removed", Toast.LENGTH_SHORT).show();
                saveTasks();
            }
        };

        TasksAdapter.OnClickListener onClickListener = new TasksAdapter.OnClickListener() {
            @Override
            public void onTaskClicked(int position) {
                Log.d("Main Activity", "Single click at position " + position);
                // create the new activity
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                // pass the data being edited
                i.putExtra(KEY_TASK_TEXT, tasks.get(position));
                i.putExtra(KEY_TASK_POSITION, position);
                // display the edit activity
                startActivityForResult(i, EDIT_TEXT_CODE);
            }
        };

        tasksAdapter = new TasksAdapter(tasks, onClickListener, onLongClickListener);
        taskList.setAdapter(tasksAdapter);
        taskList.setLayoutManager(new LinearLayoutManager(this));

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoItem = addTextField.getText().toString();
                //Add task to model
                tasks.add(todoItem);
                //Notify adapter that and item is inserted
                tasksAdapter.notifyItemInserted(tasks.size()-1);
                addTextField.setText("");
                Toast.makeText(MainActivity.this, "Task Added", Toast.LENGTH_SHORT).show();
                saveTasks();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE) {
            // Retrieve the updated text value
            String taskText = data.getStringExtra(KEY_TASK_TEXT);
            // extract the original position of the edited item from the position key
            int position = data.getExtras().getInt(KEY_TASK_POSITION);

            // update the model at the right position with the new task text
            tasks.set(position, taskText);
            // notify the adapter
            tasksAdapter.notifyItemChanged(position);
            // persist the changes
            Toast.makeText(MainActivity.this, "Task Edited", Toast.LENGTH_SHORT).show();
            saveTasks();

        } else {
            Log.w("MainActivity", "Unknown call to onActivityResult");
        }
    }

    private File getDataFile(){
        return new File(getFilesDir(), "data.txt");

    }

    // This function will load items by reading every line of data file
    private void loadTasks() {
        try{
            tasks = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e){
            Log.e("MainActivity", "Error reading tasks", e);
            tasks = new ArrayList<>();
        }
    }

    // This function saves items by writing them into the data file
    private void saveTasks(){
        try {
            FileUtils.writeLines(getDataFile(), tasks);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing tasks", e);
        }
    }

}