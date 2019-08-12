package com.zetcode.ws;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("info")
public class InfoResource {

    @Context
    private UriInfo info;

    @Context
    private HttpServletRequest servletRequest;

    @Context
    private ServletContext servletContext;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response message() {

        var builder = new StringBuilder();

        String baseUri = info.getBaseUri().toASCIIString();
        String method = servletRequest.getMethod();
        String serverInfo = servletContext.getServerInfo();

        builder.append("Base uri: ").append(baseUri)
                .append("; HTTP method: ").append(method)
                .append("; Server info: ").append(serverInfo);

        var output = builder.toString();

        return Response.status(200).entity(output).build();
    }
}
