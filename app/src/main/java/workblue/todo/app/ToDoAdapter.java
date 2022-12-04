package workblue.todo.app;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> implements Filterable {
    private List<Task> tasks;
    private List<Task> mTasks;
    private MainActivity activity;
    private FirebaseFirestore firestore;

    public ToDoAdapter(MainActivity mainActivity, List<Task> tasks) {
        this.tasks = tasks;
        this.mTasks = tasks;
        activity = mainActivity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(activity).inflate(R.layout.item_task, parent, false);
       firestore = FirebaseFirestore.getInstance();
       return new MyViewHolder(view);
    }

    public void deleteTask(int position){
        Task task = tasks.get(position);
        firestore.collection("task").document(task.TaskId).delete();
        tasks.remove(position);
        notifyItemRemoved(position);
    }
    public Context getContext(){
        return activity;
    }

    public void editTask(int position){
        Task toDoModel = tasks.get(position);

        Bundle bundle = new Bundle();
        bundle.putString("task" , toDoModel.getTask());
        bundle.putString("due" , toDoModel.getDue());
        bundle.putString("id" , toDoModel.TaskId);

        AddNewTask addNewTask = new AddNewTask();
        addNewTask.setArguments(bundle);
        addNewTask.show(activity.getSupportFragmentManager() , addNewTask.getTag());
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Task task =  tasks.get(position);
        holder.mCheckBox.setText(task.getTask());
        holder.tvDueDate.setText("Due on: " +task.getDue());

        holder.mCheckBox.setChecked(toBoolean(task.getStatus()));

        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    firestore.collection("task").document(task.TaskId).update("status",1);
                    buttonView.setTextColor(Color.GREEN);
                    holder.mCheckBox.setEnabled(false);
                } else {
                    firestore.collection("task").document(task.TaskId).update("status", 0);
                }
            }
        });
    }

    private boolean toBoolean(int status) {
        return status != 0;
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDueDate;
        CheckBox mCheckBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDueDate = itemView.findViewById(R.id.tv_due_date);
            mCheckBox = itemView.findViewById(R.id.checkbox_task);
        }
    }

    @Override // xử lý search
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if(strSearch.isEmpty()) {
                    tasks = mTasks;
                } else {
                    List<Task> taskList = new ArrayList<>();
                    for (Task task: mTasks) {
                        if(task.getTask().toLowerCase().contains(strSearch.toLowerCase())) {
                            taskList.add(task);
                        }
                    }
                    tasks = taskList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = tasks;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                tasks = (List<Task>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
