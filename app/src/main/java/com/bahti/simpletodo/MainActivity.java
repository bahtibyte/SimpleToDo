package com.bahti.simpletodo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bahti.simpletodo.utils.Task;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Text;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static MainActivity self;
    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView todoList;

    private TextView currentDate;

    public MainActivity(){
        Thread t = new Thread() {
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateTime();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();
    }
    private void updateTime(){

        if (currentDate != null){
            String date = new Date().toString();
            currentDate.setText(date.substring(0,date.indexOf("EST")));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        self = this;

        items = new ArrayList<String>();
        items.add("TEST");
        //itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);


        itemsAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Cast the list view each item as text view
                TextView item = (TextView) super.getView(position,convertView,parent);

                Task task = Task.tasks.get(position);

                int per = task.getPriority();

                int red = (int)(255F * (per / 100.0));
                int green =  (int)(255 * ((100-per) / 100.0));
                // Set the list view item's text color
                System.out.println("Red: "+red +" Green: "+green+" Blue: "+0);
                item.setTextColor(Color.rgb(red,green,0));

                // Set the item text style to bold
                item.setTypeface(item.getTypeface(), Typeface.BOLD);

                // Change the item text size
                item.setTextSize(TypedValue.COMPLEX_UNIT_DIP,24);

                // return the view
                return item;
            }
        };

        todoList = (ListView) findViewById(R.id.todoList);
        todoList.setAdapter(itemsAdapter);

        currentDate = (TextView) findViewById(R.id.currentDate);

        String date = new Date().toString();
        currentDate.setText(date.substring(0,date.indexOf("EST")));

        updateData();

        setupListener();
    }

    public static void addTask(Task task) {
        Task.tasks.add(task);
        self.saveTasks();
    }

    public static void refreshData(){
        self.saveTasks();
    }

    private void saveTasks(){
        ArrayList<String> lines = new ArrayList<String>();

        for (Task task : Task.tasks){
            lines.add(task.toString());
        }

        try {
            FileUtils.writeLines(getDataFile(),lines);
            updateData();
        }catch (Exception e){
            Log.e("MainActivity","Error writing file",e);
        }

        updateData();
    }

    //Add button is linked to this method.
    public void onAddItem(View v) {
        startActivity(new Intent(MainActivity.this, AddTask.class));
    }

    private void setupListener() {

        todoList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AddTask.preFill(i);
                startActivity(new Intent(MainActivity.this, AddTask.class));
            }
        });
    }

    private File getDataFile() {
        return new File(getFilesDir(), "todo.txt");
    }

    private void updateData() {
        Task.tasks = new ArrayList<>();
        try {
            ArrayList<String> lines = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
            for (String line : lines){

                try {
                    String temp = line;
                    String name = temp.substring(0, temp.indexOf(","));
                    temp = temp.substring(temp.indexOf(",") + 1);
                    String rawReminder = temp.substring(0, temp.indexOf(","));
                    temp = temp.substring(temp.indexOf(",") + 1);
                    String rawPriority = temp.substring(0, temp.indexOf(","));
                    temp = temp.substring(temp.indexOf(",") + 1);

                    int reminder = Integer.parseInt(rawReminder);
                    int priority = Integer.parseInt(rawPriority);

                    Task task = new Task();
                    task.setNotes(temp);
                    task.setName(name);
                    task.setReminder(reminder);
                    task.setPriority(priority);

                    Task.tasks.add(task);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }catch(Exception e){
            Log.e("Main Activity","Error reading file",e);
        }

        items.clear();

        Task[] t = new Task[Task.tasks.size()];
        for (int i = 0; i < t.length; i++){
            t[i] = Task.tasks.get(i);
        }

        Arrays.sort(t);

        Task.tasks.clear();

        for (Task task : t){
            Task.tasks.add(task);
            items.add(task.display());
        }

        itemsAdapter.notifyDataSetChanged();
    }
}