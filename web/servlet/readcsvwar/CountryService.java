package com.zetcode.service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.zetcode.bean.Country;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

public class CountryService {

    public static Optional<List<Country>> getListOfCountries() throws IOException {

        List<Country> countries;

        try (InputStream is = CountryService.class.getClassLoader()
                .getResourceAsStream("countries.csv")) {

            if (is == null) {

                return Optional.empty();
            }

            HeaderColumnNameMappingStrategy<Country> strategy
                    = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(Country.class);

            try (var br = new BufferedReader(
                    new InputStreamReader(is, StandardCharsets.UTF_8))) {

                CsvToBean<Country> csvToBean = new CsvToBeanBuilder<Country>(br)
                        .withType(Country.class)
                        .withMappingStrategy(strategy)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();

                 countries = csvToBean.parse();
            }

//            try (CSVReader reader = new CSVReader(new InputStreamReader(is))) {
//
//                String[] nextLine;
//                reader.readNext();
//
//                while ((nextLine = reader.readNext()) != null) {
//
//                    var newCountry = new Country(nextLine[0],
//                            Integer.parseInt(nextLine[1]));
//                    countries.add(newCountry);
//                }
//            }

        }

        return Optional.of(countries);
    }
}
