package com.zetcode;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

import java.util.ArrayList;

public class MongoReadAll {

    public static void main(String[] args) {

        try (var mongoClient = MongoClients.create("mongodb://localhost:27017")) {

            var database = mongoClient.getDatabase("testdb");

            MongoCollection<Document> collection = database.getCollection("cars");

            try (MongoCursor<Document> cur = collection.find().iterator()) {

                while (cur.hasNext()) {

                    var doc = cur.next();
                    var cars = new ArrayList<>(doc.values());

                    System.out.printf("%s: %s%n", cars.get(1), cars.get(2));
                }
            }
        }
    }
}
