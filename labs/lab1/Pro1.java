import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

public class Pro1 {
    public static void main(String[] args) {
        Manager manager = new Manager();
        manager.add("A", 25);
        manager.add("B", 30);
        manager.add("C", 22);

        manager.delete("B");

        manager.add("A", 27);
        manager.add("D", 35);

        manager.show();

    }

}

class Manager {
    private List<Employee> employees;

    public Manager() {
        employees = new ArrayList<>();
    }
    void add(String name, int age) {
        for (int i = 0; i < employees.size(); i++) {
            if (name.equals(employees.get(i).getName())) {
                return;
            }
        }
        Employee employee = new Employee(name, age);
        employees.add(employee);
        sorted();
    }
    void delete(String name) {
        for (int i = 0; i < employees.size(); i++) {
            if (name.equals(employees.get(i).getName())) {
                employees.remove(i);
            }
        }
    }
    void show() {
        for (Employee employee : employees) {
            System.out.println(employee.getName() + " " + employee.getAge());
        }
    }
    private void sorted() {
        employees.sort(Comparator.comparing(Employee::getName));
    }
}

class Employee {
    private String name;
    private int age;

    public Employee(String name, int age) {
        this.name = name;
        this.age = age;
    }

    String getName() {
        return name;
    }
    int getAge() {
        return age;
    }
}