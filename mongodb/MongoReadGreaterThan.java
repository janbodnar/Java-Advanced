package com.zetcode;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class MongoReadGreaterThan {

    public static void main(String[] args) {

        try (var mongoClient = MongoClients.create("mongodb://localhost:27017")) {

            var database = mongoClient.getDatabase("testdb");

            MongoCollection<Document> collection = database.getCollection("cars");

            var query = new BasicDBObject("price",
                    new BasicDBObject("$gt", 30000));

            try (var cursor = collection.find(query).iterator()) {

                while (cursor.hasNext()) {
                    System.out.println(cursor.next().toJson());
                }
            }

// This is deprecated
//            collection.find(query).forEach(new Block<Document>() {
//                @Override
//                public void apply(final Document document) {
//                    System.out.println(document.toJson());
//                }
//            });
        }
    }
}
