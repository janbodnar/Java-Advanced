package com.zetcode;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.StatUtils;


/**
 * MyStatsApp is a simple console application which computes
 * basic statistics of a series of data values. The application takes
 * a file of data as its single argument.
 * 
 * @author janbodnar
 */
public class MyStatsApp {

    /**
     * Runs the application
     *
     * @param args an array of String arguments to be parsed
     */
    public void run(String[] args) {

        CommandLine line = parseArguments(args);

        if (line.hasOption("filename")) {

            System.out.println(line.getOptionValue("filename"));
            String fileName = line.getOptionValue("filename");

            double[] data = readData(fileName);
            calculateAndPrintStats(data);

        } else {
            printAppHelp();
        }
    }

    /**
     * Parses application arguments
     *
     * @param args
     * @return <code>CommandLine</code> which represents a list of application
     * arguments.
     */
    private CommandLine parseArguments(String[] args) {

        Options options = getOptions();
        CommandLine line = null;

        CommandLineParser parser = new DefaultParser();

        try {
            line = parser.parse(options, args);

        } catch (ParseException ex) {

            System.err.println(ex);
            printAppHelp();

            System.exit(1);
        }

        return line;
    }

    /**
     * Reads application data from a file
     *
     * @param fileName
     * @return array of double values
     */
    private double[] readData(String fileName) {

        List<Double> data = new ArrayList();
        double[] mydata = null;

        try (Reader reader = Files.newBufferedReader(Paths.get(fileName));
                CSVReader csvReader = new CSVReaderBuilder(reader).build()) {

            String[] nextLine;

            while ((nextLine = csvReader.readNext()) != null) {

                for (String e : nextLine) {

                    data.add(Double.parseDouble(e));
                }
            }

            mydata = ArrayUtils.toPrimitive(data.toArray(new Double[data.size()]));

        } catch (IOException ex) {

            System.err.println(ex);
            System.exit(1);
        }

        return mydata;
    }

    /**
     * Generates application command line options
     *
     * @return application <code>Options</code>
     */
    private Options getOptions() {

        Options options = new Options();

        options.addOption("f", "filename", true, "file name to load data from");
        return options;
    }

    /**
     * Prints application help
     */
    private void printAppHelp() {

        Options options = getOptions();

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("JavaStatsEx", options, true);
    }

    /**
     * Calculates and prints data statistics
     *
     * @param data input data
     */
    private void calculateAndPrintStats(double[] data) {

        System.out.format("Geometric mean: %f%n", StatUtils.geometricMean(data));
        System.out.format("Arithmetic mean: %f%n", StatUtils.mean(data));
        System.out.format("Max: %f%n", StatUtils.max(data));
        System.out.format("Min: %f%n", StatUtils.min(data));
        System.out.format("Sum: %f%n", StatUtils.sum(data));
        System.out.format("Variance: %f%n", StatUtils.variance(data));
    }
}
