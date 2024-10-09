import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;

public class OlympicsAnalyzer implements OlympicsAnalyzerInterface {
    public List<String[]> Olympic_Athlete_Bio_filtered = new ArrayList<>();
    public List<String[]> Olympic_Athlete_Event_Results = new ArrayList<>();
    public List<String[]> Olympic_Games_Medal_Tally = new ArrayList<>();
    public List<String[]> Olympic_Results = new ArrayList<>();
    public List<String[]> Olympics_Country = new ArrayList<>();
    public List<String[]> Olympics_Games = new ArrayList<>();

    public Map<String,String[]> Olympic_Athlete_Bio_filtered_M = new HashMap<>();
    public Map<String,String[]> Olympic_Athlete_Event_Results_M = new HashMap<>();
    public Map<String,String[]> Olympic_Games_Medal_Tally_M = new HashMap<>();
    public Map<String,String[]> Olympic_Results_M = new HashMap<>();
    public Map<String,String> Olympics_Country_M = new HashMap<>();
    public Map<String,String[]> Olympics_Games_M = new HashMap<>();

    // to cope "xxx,xxx"
    public static String[] splitCSV(String input) {
        List<String> result = new ArrayList<>();

        StringBuilder currentField = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(currentField.toString().trim());
                currentField.setLength(0);
            } else {
                currentField.append(c);
            }
        }
        result.add(currentField.toString().trim());

        return result.toArray(new String[0]);
    }

    public OlympicsAnalyzer(String datasetPath) {
        String line = "";
        // first list
        try (BufferedReader br = new BufferedReader(new FileReader(datasetPath + "/Olympic_Athlete_Bio_filtered.csv"))){
            br.readLine();
            while ((line = br.readLine()) != null){
                String[] data = splitCSV(line);
                Olympic_Athlete_Bio_filtered.add(data);
                Olympic_Athlete_Bio_filtered_M.put(data[0],data);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        // second list
        try (BufferedReader br = new BufferedReader(new FileReader(datasetPath + "/Olympic_Athlete_Event_Results.csv"))){
            br.readLine();
            while ((line = br.readLine()) != null){
                String[] data = splitCSV(line);
                Olympic_Athlete_Event_Results.add(data);
                Olympic_Athlete_Event_Results_M.put(data[5],data);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        // third list
        try (BufferedReader br = new BufferedReader(new FileReader(datasetPath + "/Olympic_Games_Medal_Tally.csv"))){
            br.readLine();
            while ((line = br.readLine()) != null){
                String[] data = splitCSV(line);
                Olympic_Games_Medal_Tally.add(data);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        // forth list
        try (BufferedReader br = new BufferedReader(new FileReader(datasetPath + "/Olympic_Results.csv"))){
            br.readLine();
            while ((line = br.readLine()) != null){
                String[] data = splitCSV(line);
                Olympic_Results.add(data);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        // fifth list
        try (BufferedReader br = new BufferedReader(new FileReader(datasetPath + "/Olympics_Country.csv"))){
            br.readLine();
            while ((line = br.readLine()) != null){
                String[] data = splitCSV(line);
                Olympics_Country.add(data);
                Olympics_Country_M.put(data[0],data[1]);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        // sixth list
        try (BufferedReader br = new BufferedReader(new FileReader(datasetPath + "/Olympics_Games.csv"))){
            br.readLine();
            while ((line = br.readLine()) != null){
                String[] data = splitCSV(line);
                Olympics_Games.add(data);
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    @Override
    public Map<String, Integer> topPerformantFemale() {
        Map<String, Integer> topFemale = new LinkedHashMap<>();

        Map<String,Long> golds = Olympic_Athlete_Event_Results.stream()
                .filter(row -> "Gold".equalsIgnoreCase(row[9]))
                .filter(row -> "False".equalsIgnoreCase(row[10]))
                .collect(Collectors.groupingBy(
                        row -> row[6],
                        Collectors.counting()
                ));

        List<Map.Entry<String, Long>> sortedList = golds.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry<String, Long>::getValue).reversed()  // descending gold
                        .thenComparing(Map.Entry<String, Long>::getKey))  // alphabetical order
                .toList();

        Iterator<Map.Entry<String, Long>> iterator = sortedList.iterator();

        int count = 0;
        while (count < 10) {
            Map.Entry<String, Long> entry = iterator.next();
            String key = entry.getKey();
            long goldNum = entry.getValue();
            golds.remove(key);
            String sex = "";
            sex = Olympic_Athlete_Bio_filtered.stream()
                    .filter(row -> key.equalsIgnoreCase(row[1]))
                    .map(row -> row[2])
                    .findFirst()
                            .orElse("Unknown");

            if (sex.equalsIgnoreCase("Female")){
               count++;
               topFemale.put(key, (int) goldNum);
            }
        }
        return topFemale;
    }

    @Override
    public Map<String, Float> bmiBySports() {
        Map<String, List<Double>> sportBmiMap = new HashMap<>();
        Map<String, List<String>> culculated = new HashMap<>();
        for (String[] eventRow : Olympic_Athlete_Event_Results) {
            String sport = eventRow[3];  // sport type
            String athleteId = eventRow[7];  // athlete ID
            if (Olympic_Athlete_Bio_filtered_M.containsKey(athleteId)) {
                String[] bio = Olympic_Athlete_Bio_filtered_M.get(athleteId);
                try {
                    double height = Double.parseDouble(bio[4]) / 100.0;  // meter
                    double weight = Double.parseDouble(bio[5]);
                    if (height > 0 && weight > 0 && !culculated.computeIfAbsent(athleteId, k -> new ArrayList<>()).contains(sport)) {
                        double bmi = weight / (height * height);  // BMI
                        sportBmiMap.computeIfAbsent(sport, k -> new ArrayList<>()).add(bmi);
                        culculated.computeIfAbsent(athleteId, k -> new ArrayList<>()).add(sport);
                    }
                } catch (NumberFormatException e) {
                }
            }
        }
        // average BMI
        Map<String, Float> avgBmiBySport = sportBmiMap.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,  // sport type
                        entry -> {
                            List<Double> bmis = entry.getValue();
                            double avgBmi = bmis.stream()
                                    .mapToDouble(Double::doubleValue)
                                    .average()
                                    .orElse(0.0);
                            return (float) Math.round(avgBmi * 10) / 10;  // one decimal
                        }
                ));

        // sort
        return avgBmiBySport.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry<String, Float>::getValue).reversed()
                        .thenComparing(Map.Entry<String, Float>::getKey))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,  // conflict
                        LinkedHashMap::new));
    }

    @Override
    public Map<String, Set<Integer>> leastAppearedSport() {
        Map<String, Set<Integer>> sportAppearances = new HashMap<>();
        for (String[] eventRow : Olympic_Athlete_Event_Results) {
            String sport = eventRow[3];  // sport type
            String year = eventRow[0].split(" ")[0];  // year
            String season = eventRow[0].split(" ")[1];  // season

            if ("Summer".equalsIgnoreCase(season)) {  // summer
                sportAppearances.computeIfAbsent(sport, k -> new HashSet<>()).add(Integer.parseInt(year));
            }
        }
        Map<String, Set<Integer>> sortedSports = sportAppearances.entrySet().stream()
                .sorted(Comparator.comparing((Map.Entry<String, Set<Integer>> entry) -> entry.getValue().size())
                        .thenComparing(Map.Entry::getKey))
                .limit(10)  // only 10
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,  // set of year
                        (e1, e2) -> e1,
                        LinkedHashMap::new));
        return sortedSports;
    }

    @Override
    public Map<String, Integer> winterMedalsByCountry() {
        Map<String, Integer> countryMedalCount = new HashMap<>();
        for (String[] medalRow : Olympic_Games_Medal_Tally) {
            String edition = medalRow[0];
            String year = edition.split(" ")[0];
            String countryCode = medalRow[4];
            String season = edition.split(" ")[1];  // "Winter"

            if ("Winter".equalsIgnoreCase(season) && year.compareTo("2000")>=0) {  // after 2000
                int totalMedals = Integer.parseInt(medalRow[8]);
                countryMedalCount.merge(countryCode, totalMedals, Integer::sum);
            }
        }
        // sort Map
        Map<String, Integer> sortedCountries = countryMedalCount.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry<String, Integer>::getValue).reversed()
                        .thenComparing(Map.Entry::getKey))
                .limit(10)  // only 10
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new));

        return sortedCountries;
    }

    @Override
    public Map<String, Integer> topCountryWithYoungAthletes() {
        Map<String, List<Integer>> countryAgeMap = new HashMap<>();
        Map<String, List<String>> culculated = new HashMap<>();

        for(String[] eventRow : Olympic_Athlete_Event_Results) {
            String edition = eventRow[0];
            String countryCode = eventRow[2];
            String athleteId = eventRow[7];

            // select 2020 summer Olympics
            if ("2020 Summer Olympics".equals(edition)) {
                if (Olympic_Athlete_Bio_filtered_M.containsKey(athleteId)) {
                    String[] bio = Olympic_Athlete_Bio_filtered_M.get(athleteId);
                    String bornYearStr = bio[3];

                    try {
                        if (!culculated.computeIfAbsent(countryCode, k -> new ArrayList<>()).contains(athleteId)) {
                            int bornYear = Integer.parseInt(bornYearStr.split(" ")[2]);
                            int ageIn2020 = 2020 - bornYear;

                            countryAgeMap.computeIfAbsent(countryCode, k -> new ArrayList<>()).add(ageIn2020);
                            culculated.computeIfAbsent(countryCode, k -> new ArrayList<>()).add(athleteId);
                        }
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                        // ignore
                    }
                }
            }
        }

        // calculate the average age and store into Map
        Map<String, Integer> avgAgeByCountry = countryAgeMap.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> Olympics_Country_M.get(entry.getKey()),  // country name
                        entry -> {
                            List<Integer> ages = entry.getValue();
                            double avgAge = ages.stream().mapToInt(Integer::intValue).average().orElse(0.0);
                            return (int) Math.round(avgAge);  // Integer
                        }
                ));

        // sort age
        return avgAgeByCountry.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry<String, Integer>::getValue)
                        .thenComparing(Map.Entry<String, Integer>::getKey))
                .limit(10)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new));
    }
}
