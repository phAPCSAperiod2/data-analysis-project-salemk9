/**
 * Represents one row from the World Indicators 2000 dataset.
 *
 * This class encapsulates data about a country including its birth rate
 * and life expectancy, allowing for analysis of global demographic indicators.
 */
public class Data {

    /**
     * The name of the country.
     */
    private String country;

    /**
     * The birth rate of the country (as a decimal, e.g., 0.02 for 2%).
     */
    private double birthRate;

    /**
     * The life expectancy of the country's female population (in years).
     */
    private double lifeExpectancy;

    /**
     * Constructs a Data object with country information.
     *
     * @param country the name of the country
     * @param birthRate the birth rate of the country (as a decimal)
     * @param lifeExpectancy the life expectancy for the country (in years)
     */
    public Data(String country, double birthRate, double lifeExpectancy) {
        this.country = country;
        this.birthRate = birthRate;
        this.lifeExpectancy = lifeExpectancy;
    }

    /**
     * Returns the country name.
     *
     * @return the country name
     */
    public String getCountry() {
        return country;
    }

    /**
     * Returns the birth rate.
     *
     * @return the birth rate as a decimal
     */
    public double getBirthRate() {
        return birthRate;
    }

    /**
     * Returns the life expectancy.
     *
     * @return the life expectancy in years
     */
    public double getLifeExpectancy() {
        return lifeExpectancy;
    }

    /**
     * Returns a string representation of the Data object.
     *
     * @return a formatted string containing country, birth rate, and life expectancy
     */
    @Override
    public String toString() {
        return String.format("%s - Birth Rate: %.3f, Life Expectancy: %.1f years",
                country, birthRate, lifeExpectancy);
    }

}