package com.bahti.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bahti.simpletodo.utils.Task;

public class AddTask extends AppCompatActivity {

    private TextView label_value;
    private EditText input_taskName;
    private EditText input_notes;
    private NumberPicker input_NP;
    private SeekBar input_priority;

    private static int taskID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        input_taskName = (EditText) findViewById(R.id.input_task_name);
        input_notes = (EditText) findViewById(R.id.input_notes);

        input_NP = (NumberPicker) findViewById(R.id.input_reminder_hour);
        input_NP.setMaxValue(24);
        input_NP.setMinValue(1);

        input_priority = (SeekBar) findViewById(R.id.input_priority_bar);
        input_priority.setProgress(35);

        label_value = (TextView) findViewById(R.id.label_value);
        label_value.setText(input_priority.getProgress()+"%");

        input_priority.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                label_value.setText(progress+"%");
            }
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        if (taskID != -1) {
            Task task = Task.tasks.get(taskID);
            input_taskName.setText(task.getName());
            input_notes.setText(task.getNotes());
            input_priority.setProgress(task.getPriority());
            label_value.setText(input_priority.getProgress()+"%");
            input_NP.setValue(task.getReminder());
        }
    }



    public static void preFill(int taskID){
        AddTask.taskID = taskID;
    }

    public void buttonDelete(View view){
        if (taskID != -1){
            Toast.makeText(getApplicationContext(), "Removing " + Task.tasks.get(taskID).getName(), Toast.LENGTH_SHORT).show();
            Task.tasks.remove(taskID);
            MainActivity.refreshData();
            taskID = -1;
        }
        startActivity(new Intent(AddTask.this, MainActivity.class));
    }

    public void buttonAdd(View view){
        String taskName = input_taskName.getText().toString();
        String notes = input_notes.getText().toString();
        int reminder = input_NP.getValue();
        int priority = input_priority.getProgress();

        if (taskID == -1){
            Task task = new Task();
            task.setName(taskName);
            task.setNotes(notes);
            task.setReminder(reminder);
            task.setPriority(priority);

            MainActivity.addTask(task);
        }else{
            Task task = Task.tasks.get(taskID);
            task.setName(taskName);
            task.setNotes(notes);
            task.setReminder(reminder);
            task.setPriority(priority);
            taskID = -1;
            MainActivity.refreshData();
        }

        startActivity(new Intent(AddTask.this, MainActivity.class));
    }
}
