package com.zetcode;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.function.Consumer;

public class MongoReadGreaterThan {

    public static void main(String[] args) {

        try (var mongoClient = MongoClients.create("mongodb://localhost:27017")) {

            var database = mongoClient.getDatabase("testdb");

            MongoCollection<Document> collection = database.getCollection("cars");

            var query = new BasicDBObject("price",
                    new BasicDBObject("$gt", 30000));

            collection.find(query).forEach((Consumer<Document>) doc
                    -> System.out.println(doc.toJson()));
        }
    }
}
