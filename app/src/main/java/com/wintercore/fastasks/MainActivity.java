package com.wintercore.fastasks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.R.layout;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.NumberPicker.Formatter;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.rvTasks);
        Button addButton = findViewById(R.id.btnAddTask);

        TaskModel defaultTask = new TaskModel(6, 2, "Example text");
        TaskModel anotherDefaultTask = new TaskModel(7, 2, "Example text");

        ArrayList<TaskModel> taskList = new ArrayList<>();
        taskList.add(defaultTask);
        taskList.add(anotherDefaultTask);

        TaskRecyclerViewAdapter adapter = new TaskRecyclerViewAdapter(this, taskList);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TaskModel emptyTask = new TaskModel(6, 2, "Example text");
                taskList.add(emptyTask);
                adapter.notifyItemInserted(adapter.getItemCount());
            }
        });

    }

}