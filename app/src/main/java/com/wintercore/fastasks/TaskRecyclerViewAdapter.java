package com.wintercore.fastasks;

import android.content.Context;
import android.view.LayoutInflater;
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
    public void onBindViewHolder(@NonNull TaskRecyclerViewAdapter.TaskViewHolder holder, int position) {
        holder.etTaskName.setText(taskModels.get(position).getTaskName());
        holder.npMinutes.setValue(taskModels.get(position).getMinutes());
        holder.npHours.setValue(taskModels.get(position).getHours());
    }

    @Override
    public int getItemCount() {
        return taskModels.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {

        NumberPicker npHours;
        NumberPicker npMinutes;
        EditText etTaskName;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);

            npHours = itemView.findViewById(R.id.npHourPicker);
            npMinutes = itemView.findViewById(R.id.npMinutePicker);
            etTaskName = itemView.findViewById(R.id.etTaskName);

            npHours.setMaxValue(23);
            npHours.setMinValue(0);

            // To make incremental values
            NumberPicker.Formatter formatter = new NumberPicker.Formatter() {
                @Override
                public String format(int value) {
                    int temp = value * 15;
                    return "" + temp;
                }
            };
            npMinutes.setMaxValue(3);
            npMinutes.setMinValue(0);
            npMinutes.setFormatter(formatter);

        }
    }

}
