package com.wintercore.fastasks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
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
    public TaskRecyclerViewAdapter adapter;

    public ArrayList<TaskModel> taskList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.rvTasks);
        Button addButton = findViewById(R.id.btnAddTask);

        TaskModel defaultTask = new TaskModel(6, 30, "Example text");
        TaskModel anotherDefaultTask = new TaskModel(7, 30, "Example text");

        taskList = new ArrayList<>();
        taskList.add(defaultTask);
        taskList.add(anotherDefaultTask);

        adapter = new TaskRecyclerViewAdapter(this, taskList);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int dynamicHours = 6;
                if (taskList.size() >= 1) {
                    dynamicHours = taskList.get(taskList.size() - 1).getHours() + 1;
                }

                TaskModel emptyTask = new TaskModel(dynamicHours, 0, "Example text");
                taskList.add(emptyTask);
                adapter.notifyItemInserted(adapter.getItemCount());
            }
        });



    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
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
        }
    };

}