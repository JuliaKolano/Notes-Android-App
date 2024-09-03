package com.example.myday;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.util.ArrayList;

public class MakeTask extends AppCompatActivity {

    ArrayList<Task> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_task);

        // make the time picker display in 24h mode
        TimePicker timePicker = findViewById(R.id.time);
        timePicker.setIs24HourView(true);

        // populate the spinner with priority values
        Spinner spinner = findViewById(R.id.priority);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.priorityArray, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        // reference the edit text fields
        EditText taskNameField = findViewById(R.id.taskName);
        EditText descriptionDField = findViewById(R.id.description);

        // add intent to add task button
        Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(v -> {

            // create the intent and pass the parcelable
            Intent intent = new Intent(this, MainActivity.class);

            // receive the array list from the main activity
            tasks = getIntent().getParcelableArrayListExtra("tasks");

            // create the array list for the tasks
            if (tasks == null) {
                tasks = new ArrayList<>();
            }

            // get the user input

            // from text fields
            String taskName = taskNameField.getText().toString();
            String description = descriptionDField.getText().toString();

            // from time picker
            String taskHour = String.valueOf(timePicker.getHour());
            String taskMinute = String.valueOf(timePicker.getMinute());
            String time = taskHour + ":" + taskMinute;

            // from priority spinner
            String priority = spinner.getSelectedItem().toString();

            // create the parcelable task object
            Task task = new Task(taskName, description, time, priority, "false");

            // add the parcelable task object to the array list
            tasks.add(task);

            // pass the array task list along with the intent
            intent.putParcelableArrayListExtra("tasks", tasks);
            startActivity(intent);
        });
    }
}