package com.zetcode.ws;

import com.zetcode.model.City;
import com.zetcode.service.ICityService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

// resteasy-jackson-provider depencency required 
// for serialization

@Path("cities")
public class CityResource {

    @Inject
    private ICityService cityService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response message() {

        List<City> cities = cityService.findAll();

        return Response.status(200).entity(cities).build();
    }
}
