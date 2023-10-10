package com.wintercore.fastasks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

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
        View view = inflater.inflate(R.layout.task_row, parent, false);
        return new TaskRecyclerViewAdapter.TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskRecyclerViewAdapter.TaskViewHolder holder, int position) {

        // To combine hours and minutes
        String time = taskModels.get(position).getHours() + ":" + taskModels.get(position).getMinutes();

        holder.tvTaskName.setText(taskModels.get(position).getTaskName());
        holder.tvTime.setText(time);
    }

    @Override
    public int getItemCount() {
        return taskModels.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {

        TextView tvTime;
        TextView tvTaskName;
        TextView tvEdit;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTime = itemView.findViewById(R.id.tvTime);
            tvTaskName = itemView.findViewById(R.id.tvTaskName);
            tvEdit = itemView.findViewById(R.id.tvEdit);

        }
    }

}
