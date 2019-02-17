package com.zetcode;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.zetcode.bean.Customer;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.Version;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

// The application loads customers from the CSV file and 
// generates emails using Freemaker template engine

public class Application {

    public static void main(String[] args) throws IOException, TemplateException {

        var cfg = new Configuration(new Version("2.3.23"));

        cfg.setClassForTemplateLoading(Application.class, "/");
        cfg.setDefaultEncoding("UTF-8");

        var template = cfg.getTemplate("emails.ftl");

        var templateData = new HashMap<String, Object>();

        var fileName = "src/main/resources/customers.csv";
        var myPath = Paths.get(fileName);

        try (BufferedReader br = Files.newBufferedReader(myPath,
                StandardCharsets.UTF_8)) {

            var strategy = new HeaderColumnNameMappingStrategy<Customer>();
            strategy.setType(Customer.class);

            var csvToBean = new CsvToBeanBuilder<Customer>(br)
                    .withType(Customer.class)
                    .withMappingStrategy(strategy)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            var customers = csvToBean.parse();

            customers.forEach(System.out::println);

            templateData.put("customers", customers);
        }

        try (StringWriter out = new StringWriter()) {

            template.process(templateData, out);
            System.out.println(out.getBuffer().toString());

            out.flush();
        }
    }
}
