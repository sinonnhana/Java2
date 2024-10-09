import java.util.*;
import java.util.stream.Collectors;

public class test {

    private List<String[]> Olympic_Athlete_Event_Results;
    private Map<String, String[]> Olympic_Athlete_Bio_filtered_M;
    private Map<String, String> Olympics_Country_M;  // 存储国家代码和国家名称的映射

    // 返回 2020 年夏季奥运会中，平均年龄最小的前 10 个国家
    public Map<String, Integer> topCountryWithYoungAthletes() {
        Map<String, List<Integer>> countryAgeMap = new HashMap<>();

        for (String[] eventRow : Olympic_Athlete_Event_Results) {
            String edition = eventRow[0];
            String countryCode = eventRow[2];  // 国家代码
            String athleteId = eventRow[7];  // 运动员ID

            // 筛选 2020 年夏季奥运会
            if ("2020 Summer Olympics".equals(edition)) {
                if (Olympic_Athlete_Bio_filtered_M.containsKey(athleteId)) {
                    String[] bio = Olympic_Athlete_Bio_filtered_M.get(athleteId);
                    String bornYearStr = bio[3];  // 运动员的出生年份

                    try {
                        // 计算运动员的年龄
                        int bornYear = Integer.parseInt(bornYearStr.split(" ")[2]);  // 从出生日期中提取年份
                        int ageIn2020 = 2020 - bornYear;

                        // 将年龄加入该运动员所属国家
                        countryAgeMap.computeIfAbsent(countryCode, k -> new ArrayList<>()).add(ageIn2020);

                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                        // 跳过没有有效出生年份的运动员
                    }
                }
            }
        }

        // 计算每个国家的平均年龄并存储到 Map 中
        Map<String, Integer> avgAgeByCountry = countryAgeMap.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> Olympics_Country_M.get(entry.getKey()),  // 将国家代码映射为国家名称
                        entry -> {
                            List<Integer> ages = entry.getValue();
                            double avgAge = ages.stream().mapToInt(Integer::intValue).average().orElse(0.0);
                            return (int) Math.round(avgAge);  // 四舍五入到整数
                        }
                ));

        // 对平均年龄进行排序，先按年龄从小到大排序，年龄相同按国家名称字母顺序排序
        return avgAgeByCountry.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry<String, Integer>::getValue)
                        .thenComparing(Map.Entry<String, Integer>::getKey))
                .limit(10)  // 只返回前 10 个国家
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,  // 处理键冲突（不应发生）
                        LinkedHashMap::new));  // 保持排序后的顺序
    }
}
