package com.wintercore.fastasks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.R.layout;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.NumberPicker.Formatter;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ArrayList<TaskModel> taskList;
    private SharedPreferences sharedPreferences;
    private TaskRecyclerViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getPreferences(Context.MODE_PRIVATE);

        // Load saved data
        taskList = new ArrayList<>();
        String jsonString = sharedPreferences.getString("tasks", "DEFAULT");
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(jsonString);
        } catch (JSONException e) {
            jsonArray = new JSONArray();
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject;
            try {
                jsonObject = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                break;
            }

            int hours = 6;
            try {
                hours = jsonObject.getInt("hours");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            int minutes = 0;
            try {
                minutes = jsonObject.getInt("minutes");
            } catch (JSONException ignored) {
            }

            String taskName = "";
            try {
                taskName = jsonObject.getString("taskName");
            } catch (JSONException ignored) {
            }

            TaskModel newTask = new TaskModel(hours, minutes, taskName);
            taskList.add(newTask);
        }

        RecyclerView recyclerView = findViewById(R.id.rvTasks);
        Button addButton = findViewById(R.id.btnAddTask);


        if (taskList.size() == 0) {
            TaskModel defaultTask = new TaskModel(6, 2, "Example text");
            taskList.add(defaultTask);
        }

        adapter = new TaskRecyclerViewAdapter(this, taskList);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TaskModel emptyTask = new TaskModel(6, 0, "");
                adapter.notifyItemInserted(adapter.getItemCount());
                taskList.add(emptyTask);
            }
        });

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if (direction == ItemTouchHelper.LEFT) {
                    adapter.notifyItemRemoved(position);
                    adapter.notifyItemRangeChanged(position, adapter.getItemCount());
                    taskList.remove(position);
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    @Override
    protected void onStop() {
        super.onStop();

        // Saving taskList on application stop
        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            editor.putString("tasks", getTasksJson());
        } catch (JSONException ignored) {
        }
        editor.apply();

    }

    public String getTasksJson() throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < taskList.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("hours", taskList.get(i).getHours());
            jsonObject.put("minutes", taskList.get(i).getMinutes());
            jsonObject.put("taskName", taskList.get(i).getTaskName());
            jsonArray.put(jsonObject);
        }
        return jsonArray.toString();
    }
}