package com.zetcode.ws;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("hello")
public class HelloResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response hello() {

        var msg = "Hello there";

        return Response.status(200).entity(msg).build();
    }
}
