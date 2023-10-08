package com.wintercore.fastasks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewAdapter.TaskViewHolder> {
    Context context;
    ArrayList<TaskModel> taskModels;

    public TaskRecyclerViewAdapter(Context context, ArrayList<TaskModel> taskModels) {
        this.context = context;
        this.taskModels = taskModels;
    }

    @NonNull
    @Override
    public TaskRecyclerViewAdapter.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.task_line, parent, false);
        return new TaskRecyclerViewAdapter.TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskRecyclerViewAdapter.TaskViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // Update values
        holder.npMinutes.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                taskModels.get(position).minutes = holder.npMinutes.getValue();
            }
        });

        holder.npHours.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                taskModels.get(position).hours = holder.npHours.getValue();
            }
        });

        holder.etTaskName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                taskModels.get(position).taskName = holder.etTaskName.getText().toString();
            }
        });

        holder.etTaskName.setText(taskModels.get(position).getTaskName());
        holder.npMinutes.setValue(taskModels.get(position).getMinutes());
        holder.npHours.setValue(taskModels.get(position).getHours());

    }

    @Override
    public int getItemCount() {
        return taskModels.size();
    }


    public class TaskViewHolder extends RecyclerView.ViewHolder {
        NumberPicker npHours;
        NumberPicker npMinutes;
        EditText etTaskName;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);

            npHours = itemView.findViewById(R.id.npHourPicker);
            npMinutes = itemView.findViewById(R.id.npMinutePicker);
            etTaskName = itemView.findViewById(R.id.etTaskName);

            npHours.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
            npHours.setWrapSelectorWheel(false);
            npHours.setMaxValue(23);
            npHours.setMinValue(0);

            int interval = 15;
            String[] displayedValues = new String[60 / interval];
            for (int i = 0; i <= 3; i++) {
                displayedValues[i] = String.valueOf(i * interval);
            }

            npMinutes.setMinValue(0);
            npMinutes.setMaxValue(displayedValues.length - 1);
            npMinutes.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
            npMinutes.setDisplayedValues(displayedValues);



        }
    }
}
