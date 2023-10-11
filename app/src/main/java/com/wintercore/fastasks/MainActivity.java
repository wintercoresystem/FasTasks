package com.wintercore.fastasks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.ClipData;
import android.content.DialogInterface;
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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public TaskRecyclerViewAdapter adapter;

    public ArrayList<TaskModel> taskList;
    PopupWindow popupWindow;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.rvTasks);
        Button addButton = findViewById(R.id.btnAddTask);
        dialog = new Dialog(this);

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
//                if (taskList.size() >= 1) {
//                    dynamicHours = taskList.get(taskList.size() - 1).getHours() + 1;
//                }
//
//                TaskModel emptyTask = new TaskModel(dynamicHours, 0, "Example text");
//                taskList.add(emptyTask);
//                adapter.notifyItemInserted(adapter.getItemCount());
                showPopupWindow(view);


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

    public void showPopupWindow(View view) {
        dialog.setContentView(R.layout.edit_window);
        NumberPicker npMinutes = dialog.findViewById(R.id.npMinutes);
        NumberPicker npHours = dialog.findViewById(R.id.npHours);
        EditText etTaskName = dialog.findViewById(R.id.etTaskName);

        npMinutes.setMaxValue(59);
        npMinutes.setMinValue(0);

        npHours.setMaxValue(23);
        npHours.setMinValue(0);

        if (taskList.size() > 1) {
            npHours.setValue(taskList.get(taskList.size() - 1).hours + 1);
        }
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                TaskModel emptyTask = new TaskModel(npHours.getValue(), npMinutes.getValue(), etTaskName.getText().toString());
                taskList.add(emptyTask);
                adapter.notifyItemInserted(adapter.getItemCount());
            }
        });

        dialog.show();
    }
}