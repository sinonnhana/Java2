
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class CityAnalysis {
    public static class City
    {
        private String name;
        private String state;
        private int population;

        public City(String name, String state, int population)
        {
            this.name = name;
            this.state = state;
            this.population = population;
        }

        public String getName()
        {
            return name;
        }

        public String getState()
        {
            return state;
        }
        public int getPop()
        {
            return population;
        }

    }

    public static Stream<City> readCities(String filename) throws IOException
    {
        return Files.lines(Paths.get(filename))
                .map(l -> l.split(", "))
                .map(a -> new City(a[0], a[1], Integer.parseInt(a[2])));
    }

    public static void main(String[] args) throws IOException {

        Stream<City> cities = readCities("cities.txt");


        // Q1: count how many cities there are for each state
        cities = readCities("cities.txt");
        Map<String, Long> cityCountPerState = cities
                .collect(Collectors.groupingBy(City::getState, Collectors.counting()));

        System.out.println(cityCountPerState);


        cities = readCities("cities.txt");
        // Q2: count the total population for each state
        // TODO: Map<String, Integer> statePopulation = ...
        Map<String, Integer> statePopulation = cities
                .collect(Collectors.groupingBy(City::getState,Collectors.summingInt(City::getPop)));
        System.out.println(statePopulation);


        cities = readCities("cities.txt");
        // Q3: for each state, get the city with the longest name
        // TODO: Map<String, String> longestCityNameByState = ...
        Map<String, String> longestCityNameByState = cities
                .collect(Collectors.groupingBy(
                        City::getState,
                        Collectors.collectingAndThen(
                                Collectors.maxBy(Comparator.comparingInt(c -> c.getName().length())),
                                optCity -> optCity.map(City::getName).orElse("")
                            )
                        )
                );
        System.out.println(longestCityNameByState);


                cities = readCities("cities.txt");
        // Q4: for each state, get the set of cities with >500,000 population
        // TODO: Map<String, Set<City>> largeCitiesByState = ...
        Map<String, Set<String>> largeCitiesByState = cities
                .collect(Collectors.groupingBy(
                        City::getState,
                        Collectors.filtering(city -> city.getPop() > 500000,
                                Collectors.mapping(City::getName, Collectors.toSet()))));

        System.out.println(largeCitiesByState);
    }
}