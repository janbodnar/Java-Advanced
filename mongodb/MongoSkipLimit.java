package com.zetcode;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.function.Consumer;

public class MongoSkipLimit {

    public static void main(String[] args) {

        try (var mongoClient = MongoClients.create("mongodb://localhost:27017")) {

            var database = mongoClient.getDatabase("testdb");

            MongoCollection<Document> collection = database.getCollection("cars");
            FindIterable<Document> fit = collection.find().skip(2).limit(5);

            fit.forEach((Consumer<Document>) System.out::println);
        }
    }
}
