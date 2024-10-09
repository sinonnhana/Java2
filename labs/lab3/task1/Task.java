package task1;

import tutoral.Item;

public class Task implements Comparable<Task> {
    public String description;
    public int priority;
    public Task(String description, int priority){
        this.description = description;
        this.priority = priority;
    }
    @Override
    public int compareTo(Task other) {
        // 先按部件号排序，如果部件号相同，则按描述排序
        int diff = Integer.compare(other.priority, this.priority);
        if (diff != 0) {
            return diff;
        }
        return other.description.compareTo(this.description);
    }
//Task{description='Team meeting', priority=5}
    public String toString() {
        return "Task{description='" + description + "', priority='" + priority + "'}";
    }


}
