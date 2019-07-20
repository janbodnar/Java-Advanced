package com.zetcode;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;

public class MongoCollectionFromJSON {

    public static void main(String[] args) {

        try (var mongoClient = MongoClients.create("mongodb://localhost:27017")) {

            var database = mongoClient.getDatabase("testdb");

            MongoCollection<Document> collection = database.getCollection("continents");

            var africa = BasicDBObject.parse("{_id : '" + ObjectId.get() + "', name : 'Africa'}");
            var asia = BasicDBObject.parse("{_id : '" + ObjectId.get() + "', name : 'Asia'}");
            var europe = BasicDBObject.parse("{_id : '" + ObjectId.get() + "', name : 'Europe'}");
            var america = BasicDBObject.parse("{_id : '" + ObjectId.get() + "', name : 'America'}");
            var australia = BasicDBObject.parse("{_id : '" + ObjectId.get() + "', name : 'Australia'}");
            var antarctica = BasicDBObject.parse("{_id : '" + ObjectId.get() + "', name : 'Antarctica'}");

            collection.insertOne(new Document(africa));
            collection.insertOne(new Document(asia));
            collection.insertOne(new Document(europe));
            collection.insertOne(new Document(america));
            collection.insertOne(new Document(australia));
            collection.insertOne(new Document(antarctica));
        }
    }
}
