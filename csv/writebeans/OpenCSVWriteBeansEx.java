package com.zetcode;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.zetcode.bean.Car;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OpenCSVWriteBeansEx {

    public static void main(String[] args) {

        String fileName = "src/main/resources/cars.csv";
        Path myPath = Paths.get(fileName);

        List<Car> cars = new ArrayList<>();
        cars.add(new Car(1, "Audi", 52642));
        cars.add(new Car(2, "Mercedes", 57127));
        cars.add(new Car(3, "Skoda", 9000));
        cars.add(new Car(4, "Volvo", 29000));

        try (BufferedWriter writer = Files.newBufferedWriter(myPath,
                StandardCharsets.UTF_8)) {

            StatefulBeanToCsv<Car> beanToCsv = new StatefulBeanToCsvBuilder(writer)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .build();
            beanToCsv.write(cars);

        } catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException |
                IOException ex) {
            Logger.getLogger(OpenCSVWriteBeansEx.class.getName()).log(
                    Level.SEVERE, ex.getMessage(), ex);
        }
    }
}
