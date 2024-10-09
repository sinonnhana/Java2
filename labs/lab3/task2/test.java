package task2;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class test {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        while (true) {
            System.out.println("Please input the function no:");
            System.out.println("1 - Get even numbers");
            System.out.println("2 - Get odd numbers");
            System.out.println("3 - Get prime numbers");
            System.out.println("0 - Quit");

            int choice = input.nextInt();
            input.nextLine();

            if (choice == 0) {
                break;
            }

            System.out.println("Input the integer list:");
            String line = input.nextLine();
            String[] tokens = line.split(" ");
            
            List<Integer> numbers = new ArrayList<>();
            for (String token : tokens) {
                numbers.add(Integer.parseInt(token));
            }

            List<Integer> result = null;
            switch (choice) {
                case 1:
                    result = FilterUtil.filter(numbers, FilterUtil::isEven);
                    break;
                case 2:
                    result = FilterUtil.filter(numbers, FilterUtil::isOdd);
                    break;
                case 3:
                    result = FilterUtil.filter(numbers, FilterUtil::isPrime);
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
                    continue;
            }

            System.out.println("Filter results:");
            System.out.println(result);
        }
        input.close();
    }
}