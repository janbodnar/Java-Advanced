package com.zetcode;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.IOException;
import java.io.StringReader;

public class GsonStreamApiRead {

    public static void main(String[] args) throws IOException {

        String json_string = "{\"name\":\"chair\",\"quantity\":3}";

        try (JsonReader reader = new JsonReader(new StringReader(json_string))) {
            
            while (reader.hasNext()) {
                
                JsonToken nextToken = reader.peek();
                
                if (JsonToken.BEGIN_OBJECT.equals(nextToken)) {
                    
                    reader.beginObject();
                    
                } else if (JsonToken.NAME.equals(nextToken)) {
                    
                    reader.nextName();
                    
                } else if (JsonToken.STRING.equals(nextToken)) {
                    
                    String value = reader.nextString();
                    System.out.format("%s: ", value);
                    
                } else if (JsonToken.NUMBER.equals(nextToken)) {
                    
                    long value = reader.nextLong();
                    System.out.println(value);
                    
                }
            }
        }
    }
}
