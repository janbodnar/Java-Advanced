package com.zetcode.wx;

import java.net.URI;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

@Path("urlinfo")
public class UrlInfoRes {

    @Context
    private UriInfo info;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getInfo() {

        URI uri = info.getRequestUri();

        int port = uri.getPort();
        String path = uri.getPath();
        String query = uri.getQuery();
        String host = uri.getHost();
        String scheme = uri.getScheme();

        String fmt = "Scheme: %s%nHost: %s%nPort: %s%nPath: %s%nQuery: %s%n";

        String out = String.format(fmt, scheme, host, port,
                path, query);

        return out;
    }
}
