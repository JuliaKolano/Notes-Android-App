package com.example.myday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    ArrayList<Task> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // create a reference to the list view of tasks
        ListView taskListView = findViewById(R.id.taskListView);

        // receive the parcelable array list through the intent
        tasks = getIntent().getParcelableArrayListExtra("tasks");

        // error handling for the null pointer exception
        // (check if the array list exists)
        if (tasks != null) {

            // create and attach a new array list adapter to the list view
            TaskListAdapter taskListAdapter = new TaskListAdapter(this, tasks);
            taskListView.setAdapter(taskListAdapter);

            // display a snackbar after the user presses and holds an item.
            taskListView.setOnItemLongClickListener((parent, view, position, id) -> {
                Snackbar.make(view, R.string.snackbarDeleteAction, Snackbar.LENGTH_LONG)
                        .setAction(R.string.snackbarDeleteMessage, v -> {
                            Snackbar.make(view, R.string.snackbarDeleteDone, Snackbar.LENGTH_SHORT)
                                            .show();
                            tasks.remove(position);
                            taskListAdapter.notifyDataSetChanged();
                        })
                .show();
                return true;
            });

            // pass the object clicked on, its position in the list
            // and the  array list to the EditTask activity
            taskListView.setOnItemClickListener((parent, view, position, id) -> {
                Task task = tasks.get(position);

                Intent intent = new Intent(this, EditTask.class);
                intent.putExtra("task", task);
                intent.putExtra("position", position);
                intent.putParcelableArrayListExtra("tasks", tasks);
                startActivity(intent);
            });

            // DOESN'T WORK :(
            //  set the notification for uncompleted tasks an hour before it's due
            for (Task task : tasks) {
                // only proceed for the uncompleted tasks
                if (Objects.equals(task.getCompleted(), "false")) {

                    // change the time of the task from a string to time format
                    String taskTimeString = task.getTime();
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("hh:mm");
                    try {
                        Time taskTime = new Time(Objects.requireNonNull(format.parse(taskTimeString)).getTime());

                        // get the time one hour before the task is supposed to be completed
                        Calendar notificationTime = Calendar.getInstance();
                        notificationTime.setTime(taskTime);
                        notificationTime.add(Calendar.MILLISECOND, - (60 * 60 * 1000));

                        // invoke the NotificationReceiver class using an intent
                        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
                        intent.putExtra("taskName", task.getName());

                        // set the pending intent for the Notification receiver class
                        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getBroadcast(
                                getApplicationContext(), 0, intent,
                                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                        // create the alarm manager
                        AlarmManager alarmManager = (AlarmManager) getApplicationContext()
                                .getSystemService(Context.ALARM_SERVICE);

                        // set the time the notification will be displayed
                        alarmManager.set(AlarmManager.RTC_WAKEUP, notificationTime.getTimeInMillis(), pendingIntent);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        // add intent to add task button
        FloatingActionButton addTaskButton = findViewById(R.id.addTaskButton);
        addTaskButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MakeTask.class);
            intent.putParcelableArrayListExtra("tasks", tasks);
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    // make the toolbar options change the background colour
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        View constraintLayout = findViewById(R.id.constraintLayout);
        switch (item.getItemId()) {
            case R.id.purpleTheme:
                constraintLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_light_purple));
                break;
            case R.id.blueTheme:
                constraintLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_light_blue));
                break;
            case R.id.yellowTheme:
                constraintLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_light_yellow));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // save the state of the activity after the orientation changes
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("tasks", tasks);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ArrayList<Task> tasks = savedInstanceState.getParcelableArrayList("tasks");
        ListView taskListView = findViewById(R.id.taskListView);
        TaskListAdapter taskListAdapter = new TaskListAdapter(this, tasks);
        taskListView.setAdapter(taskListAdapter);
    }
}