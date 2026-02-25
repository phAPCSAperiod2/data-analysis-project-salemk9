import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Main application for analyzing World Indicators 2000 dataset.
 *
 * This application reads a CSV file containing world indicators, creates Data objects
 * to represent each country, and analyzes the correlation between countries and their
 * birth rates. It implements algorithms to calculate averages and identify countries
 * with above-average birth rates.
 */
public class App {

    /**
     * Main method that orchestrates the data analysis.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        // Load the CSV file
        File file = new File("WorldIndicators2000.csv");

        // Create an ArrayList to store Data objects
        ArrayList<Data> countries = new ArrayList<>();

        // Read and parse the CSV file
        try (Scanner scanner = new Scanner(file)) {
            // Skip the header row
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            // Read each data row
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");

                // Ensure we have at least the required columns
                if (parts.length >= 16) {
                    try {
                        String country = parts[0].trim();
                        double birthRate = parts[2].trim().isEmpty() ? 0 : Double.parseDouble(parts[2].trim());
                        double lifeExpectancy = parts[15].trim().isEmpty() ? 0 : Double.parseDouble(parts[15].trim());

                        // Only add countries with valid data
                        if (birthRate > 0 && lifeExpectancy > 0) {
                            countries.add(new Data(country, birthRate, lifeExpectancy));
                        }
                    } catch (NumberFormatException e) {
                        // Skip rows with invalid numeric data
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found - " + e.getMessage());
            return;
        }

        // Perform analysis
        System.out.println("=== World Indicators 2000 Data Analysis ===");
        System.out.println();
        System.out.println("Total countries loaded: " + countries.size());
        System.out.println();

        // Calculate average birth rate
        double averageBirthRate = calculateAverageBirthRate(countries);
        System.out.printf("Average Birth Rate: %.4f%n", averageBirthRate);
        System.out.println();

        // Count and list countries with above-average birth rates
        int aboveAverageCount = countAboveAverageBirthRate(countries, averageBirthRate);
        System.out.println("Countries with above-average birth rates: " + aboveAverageCount);
        System.out.println();

        // Show top 5 countries with highest birth rates
        System.out.println("Top 5 Countries by Birth Rate:");
        printTopCountriesByBirthRate(countries, 5);
        System.out.println();

        // Answer the guiding question
        System.out.println("=== Answer to Guiding Question ===");
        System.out.println("Question: What is the correlation between countries and birth rate?");
        System.out.println();
        System.out.println("Findings:");
        System.out.println("- Birth rates vary significantly across countries, ranging from very low");
        System.out.println("  (developed nations) to quite high (developing nations).");
        System.out.printf("- The dataset shows an average birth rate of %.4f across %d countries.%n", averageBirthRate, countries.size());
        System.out.printf("- %d countries (%.1f%%) have birth rates above the average.%n",
                aboveAverageCount, (aboveAverageCount * 100.0 / countries.size()));
        System.out.println("- Higher birth rates are generally associated with developing nations,");
        System.out.println("  while developed nations tend to have lower birth rates.");
    }

    /**
     * Calculates the average birth rate across all countries in the dataset.
     *
     * @param countries the ArrayList of Data objects representing countries
     * @return the average birth rate
     */
    public static double calculateAverageBirthRate(ArrayList<Data> countries) {
        if (countries.isEmpty()) {
            return 0;
        }

        double sum = 0;
        for (Data country : countries) {
            sum += country.getBirthRate();
        }

        return sum / countries.size();
    }

    /**
     * Counts how many countries have a birth rate above the specified average.
     *
     * @param countries the ArrayList of Data objects representing countries
     * @param average the average birth rate to compare against
     * @return the count of countries with above-average birth rates
     */
    public static int countAboveAverageBirthRate(ArrayList<Data> countries, double average) {
        int count = 0;
        for (Data country : countries) {
            if (country.getBirthRate() > average) {
                count++;
            }
        }
        return count;
    }

    /**
     * Prints the top N countries sorted by birth rate in descending order.
     *
     * @param countries the ArrayList of Data objects representing countries
     * @param topN the number of top countries to display
     */
    public static void printTopCountriesByBirthRate(ArrayList<Data> countries, int topN) {
        // Simple bubble sort to order countries by birth rate (descending)
        ArrayList<Data> sorted = new ArrayList<>(countries);
        for (int i = 0; i < sorted.size(); i++) {
            for (int j = 0; j < sorted.size() - 1 - i; j++) {
                if (sorted.get(j).getBirthRate() < sorted.get(j + 1).getBirthRate()) {
                    Data temp = sorted.get(j);
                    sorted.set(j, sorted.get(j + 1));
                    sorted.set(j + 1, temp);
                }
            }
        }

        // Print top N
        for (int i = 0; i < Math.min(topN, sorted.size()); i++) {
            System.out.printf("%d. %s (Birth Rate: %.4f)%n", i + 1, sorted.get(i).getCountry(), sorted.get(i).getBirthRate());
        }
    }

}
