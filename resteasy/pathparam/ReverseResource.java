package com.zetcode.wx;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("reverse")
public class ReverseResource {

    @GET
    @Path("/{word}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getMsg(@PathParam("word") String msg) {

        var builder = new StringBuilder(msg);
        var output = builder.reverse().toString();

        return Response.status(200).entity(output).build();
    }
}
