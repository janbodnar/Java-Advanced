package com.zetcode;

import com.mongodb.MongoCommandException;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;

public class MongoCreateCollection {

    public static void main(String[] args) {

        try (var mongoClient = MongoClients.create("mongodb://localhost:27017")) {

            var database = mongoClient.getDatabase("testdb");

            try {

                database.createCollection("cars");
            } catch (MongoCommandException e) {

                database.getCollection("cars").drop();
            }

            var docs = new ArrayList<Document>();

            MongoCollection<Document> collection = database.getCollection("cars");

            var d1 = new Document("_id", 1);
            d1.append("name", "Audi");
            d1.append("price", 52642);
            docs.add(d1);

            var d2 = new Document("_id", 2);
            d2.append("name", "Mercedes");
            d2.append("price", 57127);
            docs.add(d2);

            var d3 = new Document("_id", 3);
            d3.append("name", "Skoda");
            d3.append("price", 9000);
            docs.add(d3);

            var d4 = new Document("_id", 4);
            d4.append("name", "Volvo");
            d4.append("price", 29000);
            docs.add(d4);

            var d5 = new Document("_id", 5);
            d5.append("name", "Bentley");
            d5.append("price", 350000);
            docs.add(d5);

            var d6 = new Document("_id", 6);
            d6.append("name", "Citroen");
            d6.append("price", 21000);
            docs.add(d6);

            var d7 = new Document("_id", 7);
            d7.append("name", "Hummer");
            d7.append("price", 41400);
            docs.add(d7);

            var d8 = new Document("_id", 8);
            d8.append("name", "Volkswagen");
            d8.append("price", 21600);
            docs.add(d8);

            collection.insertMany(docs);
        }
    }
}
