package com.wintercore.fastasks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.R.layout;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.NumberPicker.Formatter;
import android.widget.PopupMenu;
import android.widget.PopupWindow;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public TaskRecyclerViewAdapter adapter;
    private SharedPreferences sharedPreferences;
    public ArrayList<TaskModel> taskList;
    public PopupWindow popupWindow;
    public Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialog = new Dialog(this);
        RecyclerView recyclerView = findViewById(R.id.rvTasks);
        Button addButton = findViewById(R.id.btnAddTask);
        sharedPreferences = getPreferences(Context.MODE_PRIVATE);

        taskList = new ArrayList<>();
        loadTasks();

        if (taskList.size() == 0) {
            TaskModel defaultTask = new TaskModel(6, 0, "Example text");
            taskList.add(defaultTask);
        }


        adapter = new TaskRecyclerViewAdapter(this, taskList);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupWindow(-1);
            }
        });
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            if (direction == ItemTouchHelper.LEFT) {
                int position = viewHolder.getAdapterPosition();
                taskList.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, adapter.getItemCount());
            }

            if (direction == ItemTouchHelper.RIGHT) {
                int position = viewHolder.getAdapterPosition();
                showPopupWindow(position);

            }
        }
    };

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

    public void showPopupWindow(int position) {
        dialog.setContentView(R.layout.edit_window);
        NumberPicker npMinutes = dialog.findViewById(R.id.npMinutes);
        NumberPicker npHours = dialog.findViewById(R.id.npHours);
        EditText etTaskName = dialog.findViewById(R.id.etTaskName);


        int interval = 15;
        String[] displayedValues = new String[60 / interval];
        for (int i = 0; i <= 3; i++) {
            displayedValues[i] = String.valueOf(i * interval);
        }

        npMinutes.setMinValue(0);
        npMinutes.setMaxValue(displayedValues.length - 1);
        npMinutes.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        npMinutes.setDisplayedValues(displayedValues);

        npHours.setMaxValue(23);
        npHours.setMinValue(0);

        if (taskList.size() > 0) {
            npHours.setValue(taskList.get(taskList.size() - 1).hours + 1);
        }

        if (position != -1) {
            npHours.setValue(taskList.get(position).getHours());
            npMinutes.setValue(taskList.get(position).getMinutes());
            etTaskName.setText(taskList.get(position).getTaskName());
        }
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                TaskModel newTask = new TaskModel(npHours.getValue(), npMinutes.getValue(), etTaskName.getText().toString());
                if (position == -1) {
                    taskList.add(newTask);
                    adapter.notifyItemInserted(adapter.getItemCount());
                } else {
                    taskList.set(position, newTask);
                    adapter.notifyItemChanged(position);
                }
            }
        });

        dialog.show();
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

    public void loadTasks() {
        try {
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
        } catch (Exception ignored) {
        }
    }

}