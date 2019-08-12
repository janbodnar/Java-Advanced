package com.zetcode.ws;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/sayhello")
public class MessageResource {

    @Named("hello")
    @Inject
    private String message;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getMessage(){
        return message;
    }
}
