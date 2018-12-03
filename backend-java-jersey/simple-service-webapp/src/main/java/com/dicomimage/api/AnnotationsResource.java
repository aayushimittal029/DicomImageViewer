package com.dicomimage.api;

import com.dicomimage.api.dao.AnnotationServiceImpl.Annotations;
import com.dicomimage.api.dao.AnnotationServiceImpl.AnnotationsDAO;
import com.dicomimage.api.dao.CoordinateServiceImpl.CoordinateDAO;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import java.awt.*;
import java.util.Map;


@Path("annotations")
public class AnnotationsResource {

    @GET
    @Produces("application/json")
    public Annotations[] getAllAnnotations() {
        AnnotationsDAO aDao = new AnnotationsDAO();
        Annotations[] annotations = aDao.getAllAnnotations();
        return annotations;
    }

    @GET
    @Path("/{imagename}")
    @Produces("application/json")
    public Annotations[] getAnnotationByImageName(@PathParam("imagename") String imagename) {
        AnnotationsDAO aDao = new AnnotationsDAO();
        Annotations[] annotations = aDao.getAnnotationByImageName(imagename);
        return annotations;
    }

    @GET
    @Path("/coord")
    @Produces("application/json")
    public Response dummy() {
        CoordinateDAO aDao = new CoordinateDAO();
        return Response.status(Response.Status.OK).entity(aDao.getAllCoordinatesList()).build();
    }



}
