package com.zetcode.ws;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("msg")
public class MessageResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response message(@DefaultValue("Guest") @QueryParam("name") String name,
                            @DefaultValue("0") @QueryParam("age") int age) {

        var builder = new StringBuilder();
        builder.append(name).append(" is ")
                .append(age).append(" years old");

        String output = builder.toString();

        return Response.status(200).entity(output).build();
    }
}
