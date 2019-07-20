package com.zetcode;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

public class MongoModify {

    public static void main(String[] args) {

        try (var mongoClient = new MongoClient("localhost", 27017)) {

            var database = mongoClient.getDatabase("testdb");

            MongoCollection<Document> collection = database.getCollection("cars");

            collection.deleteOne(eq("name", "Skoda"));
            collection.updateOne(new Document("name", "Audi"),
                    new Document("$set", new Document("price", 52000)));

        }
    }
}
