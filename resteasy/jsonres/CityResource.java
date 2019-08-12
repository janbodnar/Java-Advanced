package com.zetcode.ws;

import com.zetcode.model.City;
import com.zetcode.service.ICityService;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("cities")
public class CityResource {

    @Inject
    private ICityService cityService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response message() {

        List<City> cities = cityService.findAll();

        GenericEntity<List<City>> myEntity = new GenericEntity<>(cities) {};

        return Response.status(200).entity(myEntity).build();
    }
}
