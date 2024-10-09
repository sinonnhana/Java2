package task1;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class TaskScheduler {
    PriorityQueue<Task> tasks = new PriorityQueue<>();
    public void addTask(String description, int priority){
        Task task = new Task(description, priority);
        tasks.add(task);
    }
    public List<Task> getTopKTasks(int k){
        PriorityQueue<Task> tasks_tep = new PriorityQueue<>(tasks);
        List<Task> top_tasks = new ArrayList<>();
        while(!tasks_tep.isEmpty() && k > 0){
            top_tasks.add(tasks_tep.poll());
            k--;
        }
        return top_tasks;
    }
    public void finishNextTask(){
        tasks.poll();
    }
}
