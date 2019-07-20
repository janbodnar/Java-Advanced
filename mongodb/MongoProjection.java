package com.zetcode;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;

import static com.mongodb.client.model.Projections.excludeId;

public class MongoProjection {

    public static void main(String[] args) {

        try (var mongoClient = MongoClients.create("mongodb://localhost:27017")) {

            var database = mongoClient.getDatabase("testdb");

            MongoCollection<Document> collection = database.getCollection("cars");

            FindIterable it = collection.find().projection(excludeId());

            var docs = new ArrayList<Document>();

            it.into(docs);

            for (Document doc : docs) {

                System.out.println(doc);
            }
        }
    }
}
