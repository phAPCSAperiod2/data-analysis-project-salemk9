import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Main application for analyzing World Indicators 2000 WorldIndicator2000set.
 *
 * This application reads a CSV file containing world indicators, creates WorldIndicator2000 objects
 * to represent each country, and analyzes the correlation between countries and their
 * birth rates. It implements algorithms to calculate averages and identify countries
 * with above-average birth rates.
 * @author Salem Kiar
 * @collaborator CoPilot
 */
public class App {

    /**
     * Main method that executes the WorldIndicator2000 analysis.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        // Load countries from the CSV file. This method handles file reading and parsing 
        // returning a list of WorldIndicator2000 objects.
        ArrayList countries = loadCountriesFromCSV("WorldIndicators2000.csv");

        // Note: file loading has been moved to its own method (loadCountriesFromCSV).  
        // Method implementation are below the main method to keep it organized.

        // Perform analysis
        System.out.println("=== World Indicators 2000 WorldIndicator2000 Analysis ===");
        System.out.println();
        System.out.println("Total countries loaded: " + countries.size());
        System.out.println();

        // Calculate average birth rate and other statistics using helper methods.
        double averageBirthRate = calculateAverageBirthRate(countries);
        System.out.printf("Average Birth Rate: %.4f%n", averageBirthRate);
        System.out.println();

        int aboveAverageCount = countAboveAverageBirthRate(countries, averageBirthRate);
        System.out.println("Countries with above-average birth rates: " + aboveAverageCount);
        System.out.println();

        System.out.println("Top 5 Countries by Birth Rate:");
        printTopCountriesByBirthRate(countries, 5);
        System.out.println();

        // Print the answer to the guiding question using its own method
        printGuidingQuestion(countries, averageBirthRate, aboveAverageCount);
    }

    /**
     * Calculates the average birth rate across all countries in the WorldIndicator2000set.
     *
     * @param countries the ArrayList of WorldIndicator2000 objects representing countries
     * @return the average birth rate
     */
    public static double calculateAverageBirthRate(ArrayList countries) {
        if (countries.isEmpty()) {
            return 0;
        }

        double sum = 0;
        for (Object o : countries) {
            WorldIndicator2000 country = (WorldIndicator2000) o;
            sum += country.getBirthRate();
        }

        return sum / countries.size();
    }

    /**
     * Counts how many countries have a birth rate above the specified average.
     *
     * @param countries the ArrayList of WorldIndicator2000 objects representing countries
     * @param average the average birth rate to compare against
     * @return the count of countries with above-average birth rates
     */
    public static int countAboveAverageBirthRate(ArrayList countries, double average) {
        int count = 0;
        for (Object o : countries) {
            WorldIndicator2000 country = (WorldIndicator2000) o;
            if (country.getBirthRate() > average) {
                count++;
            }
        }
        return count;
    }

    /**
     * Prints the top N countries sorted by birth rate in descending order.
     *
     * @param countries the ArrayList of WorldIndicator2000 objects representing countries
     * @param topN the number of top countries to display
     */
    public static void printTopCountriesByBirthRate(ArrayList countries, int topN) {
        // Simple bubble sort to order countries by birth rate (descending)
        ArrayList sorted = new ArrayList(countries);
        for (int i = 0; i < sorted.size(); i++) {
            for (int j = 0; j < sorted.size() - 1 - i; j++) {
                WorldIndicator2000 a = (WorldIndicator2000) sorted.get(j);
                WorldIndicator2000 b = (WorldIndicator2000) sorted.get(j + 1);
                if (a.getBirthRate() < b.getBirthRate()) {
                    sorted.set(j, b);
                    sorted.set(j + 1, a);
                }
            }
        }

        // Print top N
        for (int i = 0; i < Math.min(topN, sorted.size()); i++) {
            WorldIndicator2000 d = (WorldIndicator2000) sorted.get(i);
            System.out.printf("%d. %s (Birth Rate: %.4f)%n", i + 1, d.getCountry(), d.getBirthRate());
        }
    }

    /**
     * Reads the CSV file and returns a list of WorldIndicator2000 objects representing the
     * valid countries found in the file.
     * 
     * @param filename the path to the CSV file
     * @return an ArrayList containing WorldIndicator2000 objects (possibly empty)
     */
    public static ArrayList loadCountriesFromCSV(String filename) {
        ArrayList countries = new ArrayList();
        File file = new File(filename);

        try (Scanner scanner = new Scanner(file)) {
            // skip header
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            // Split each line by commas and create WorldIndicator2000 objects for valid entries
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");

                if (parts.length >= 16) {
                    try {
                        // Extract country name, birth rate, and life expectancy from the appropriate columns
                        String country = parts[0].trim();
                        double birthRate = 0;
                        if (!parts[2].trim().isEmpty()) {
                            birthRate = Double.parseDouble(parts[2].trim());
                        }
                        double lifeExpectancy = 0;
                        if (!parts[15].trim().isEmpty()) {
                            lifeExpectancy = Double.parseDouble(parts[15].trim());
                        }
                        if (birthRate > 0 && lifeExpectancy > 0) {
                            // avoid duplicate countries (WorldIndicator2000 contains several years)
                            boolean seen = false;
                            for (Object existing : countries) {
                                WorldIndicator2000 d = (WorldIndicator2000) existing;
                                if (d.getCountry().equals(country)) {
                                    seen = true;
                                    break;
                                }
                            }
                            if (!seen) {
                                countries.add(new WorldIndicator2000(country, birthRate, lifeExpectancy));
                            }
                        }
                    } catch (NumberFormatException e) {
                        // skip rows with invalid numbers
                    }
                }
            }
        } catch (FileNotFoundException e) {
            // Handle the case where the file is not found.
            System.err.println("Error: File not found - " + e.getMessage());
        }
        return countries;
    }

    /**
     * Prints the analysis summary that answers my guiding question.
     * 
     * @param countries the list of WorldIndicator2000 objects representing countries
     * @param averageBirthRate the calculated average birth rate across all countries
     * @param aboveAverageCount the count of countries with above-average birth rates
     */
    public static void printGuidingQuestion(ArrayList countries,
                                            double averageBirthRate,
                                            int aboveAverageCount) {
        System.out.println("=== Answer to Guiding Question ===");
        System.out.println("Question: What is the correlation between countries and birth rate?");
        System.out.println();
        System.out.println("Findings:");
        System.out.println("- Birth rates vary significantly across countries, ranging from very low");
        System.out.println("  (developed nations) to quite high (developing nations).");
        System.out.printf("- The WorldIndicator2000 set shows an average birth rate of %.4f across %d countries.%n",
                averageBirthRate, countries.size());
        System.out.printf("- %d countries (%.1f%%) have birth rates above the average.%n",
                aboveAverageCount, (aboveAverageCount * 100.0 / countries.size()));
        System.out.println("- Higher birth rates are generally associated with developing nations,");
        System.out.println("  while developed nations tend to have lower birth rates.");
    }

}
