import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class test2 {
    public static void main(String[] args) {
        String filePath = "path_to_your_csv_file.csv";
        String line = "";
        String csvSplitBy = ",";  // CSV文件的分隔符号，通常为逗号

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            while ((line = br.readLine()) != null) {
                // 按逗号分隔
                String[] data = line.split(csvSplitBy);
                // 打印读取的数据
                for (String field : data) {
                    System.out.print(field + " ");
                }
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
