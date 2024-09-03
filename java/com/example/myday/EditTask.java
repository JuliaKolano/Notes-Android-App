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

public class EditTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        // create references to all the elements on screen
        EditText taskName = findViewById(R.id.taskNameEdit);
        EditText taskDescription = findViewById(R.id.descriptionEdit);
        TimePicker timePicker = findViewById(R.id.timeEdit);
        Spinner spinner = findViewById(R.id.priorityEdit);

        // make the time picker display in 24h mode
        timePicker.setIs24HourView(true);

        // populate the spinner with priority values
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.priorityArray, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        // receive the intent from the MainActivity
        Task task = getIntent().getParcelableExtra("task");
        ArrayList<Task> tasks = getIntent().getParcelableArrayListExtra("tasks");
        int taskIndex = getIntent().getIntExtra("position", -1);

        // set the values of all the elements to values from the Parcelable object

        // text fields
        taskName.setText(task.getName());
        taskDescription.setText(task.getDescription());

        // time picker
        timePicker.setHour(Integer.parseInt(task.getTime().substring(0, 2)));
        timePicker.setMinute(Integer.parseInt(task.getTime().substring(3)));

        // priority spinner
        int spinnerPosition = arrayAdapter.getPosition(task.getPriority());
        spinner.setSelection(spinnerPosition);

        // add an on click listener to the edit button
        Button editButton = findViewById(R.id.editButton);
        editButton.setOnClickListener(v -> {

            // get the updated values from all the elements

            // text input fields
            String taskNameNew = taskName.getText().toString();
            String taskDescriptionNew = taskDescription.getText().toString();

            // time picker
            String taskHourNew = String.valueOf(timePicker.getHour());
            String taskMinuteNew = String.valueOf(timePicker.getMinute());
            String timeNew = taskHourNew + ":" +taskMinuteNew;

            // priority spinner
            String priorityNew = spinner.getSelectedItem().toString();

            // create a new task with the updated values
            Task taskNew = new Task(taskNameNew, taskDescriptionNew, timeNew, priorityNew, task.getCompleted());

            // replace the updated task with the old one in the list
            tasks.set(taskIndex, taskNew);

            // create an intent and send the updated array list back to MainActivity
            Intent intent = new Intent(this, MainActivity.class);
            intent.putParcelableArrayListExtra("tasks", tasks);
            startActivity(intent);
        });
    }
}