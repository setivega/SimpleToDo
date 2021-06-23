package com.example.simpletodo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder> {

    public interface OnClickListener {
        void onTaskClicked(int position);
    }


    public interface OnLongClickListener {
        void onTaskLongClicked(int position);
    }

    List<String> tasks;
    OnLongClickListener longClickListener;
    OnClickListener clickListener;

    public TasksAdapter(List<String> tasks, OnClickListener clickListener, OnLongClickListener longClickListener) {
        this.tasks = tasks;
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Use layout inflator to inflate a view
        View todoView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        // Wrap it inside of a View Holder and return it
        return new ViewHolder(todoView);
    }

    // responsible for binding data to a particular view holder
    @Override
    public void onBindViewHolder(@NonNull TasksAdapter.ViewHolder holder, int position) {
        // Grab the task at the position
        String task = tasks.get(position);
        // Bind the task into the specified view holder
        holder.bind(task);
    }


    @Override
    public int getItemCount() {
        return tasks.size();
    }

    // Container to provide easy access to views that represent each row of the list
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView taskTextView;

        public ViewHolder(@NonNull View taskView) {
            super(taskView);
            taskTextView = taskView.findViewById(android.R.id.text1);
        }

        // Update the view inside of the  view holder with this data
        public void bind(String task) {
            taskTextView.setText(task);
            taskTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get position of task that was clicked
                    clickListener.onTaskClicked(getAdapterPosition());
                }
            });
            taskTextView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // Getting position of the task that was long pressed
                    longClickListener.onTaskLongClicked(getAdapterPosition());
                    return true;
                }
            });
        }
    }

}
