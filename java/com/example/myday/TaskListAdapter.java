package com.example.myday;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Objects;

public class TaskListAdapter extends ArrayAdapter<Task> {

    //generate the array adapter
    public TaskListAdapter(@NonNull Context context,  @NonNull ArrayList<Task> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // get the parcelable object based on its position in the array
        Task task = getItem(position);
        // create a view for the single task layout if one doesn't already exist
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.single_task, parent, false);
        }

        // check the state of the checkbox and change the completed variable accordingly
        CheckBox checkBox = convertView.findViewById(R.id.checkbox);
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (checkBox.isChecked()) {
                task.setCompleted("true");
            } else {
                task.setCompleted("false");
            }
        });

        // set the state of the checkbox depending on the value of 'completed'
        if (Objects.equals(task.getCompleted(), "true")) {
            checkBox.setChecked(true);
        } else if (Objects.equals(task.getCompleted(), "false")) {
            checkBox.setChecked(false);
        }

        // create references to all the text views from the single task layout
        TextView taskNameView = convertView.findViewById(R.id.taskNameView);
        TextView descriptionView = convertView.findViewById(R.id.descriptionView);
        TextView timeView = convertView.findViewById(R.id.timeView);
        TextView priorityView = convertView.findViewById(R.id.priorityView);

        // change the text of all the text views to the information from the parcelable
        taskNameView.setText(task.getName());
        descriptionView.setText(task.getDescription());
        timeView.setText(String.format("Time: %s", task.getTime()));
        priorityView.setText(String.format("Priority: %s", task.getPriority()));

        // return the updated view for the single task layout
        return convertView;
    }
}
