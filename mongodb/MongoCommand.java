package com.zetcode;

import com.mongodb.client.MongoClients;
import org.bson.Document;

import java.util.Map;

public class MongoCommand {

    public static void main(String[] args) {

        try (var mongoClient = MongoClients.create("mongodb://localhost:27017")) {

            var database = mongoClient.getDatabase("testdb");

            var stats = database.runCommand(new Document("dbstats", 1));

            for (Map.Entry<String, Object> set : stats.entrySet()) {

                System.out.format("%s: %s%n", set.getKey(), set.getValue());
            }
        }
    }
}
