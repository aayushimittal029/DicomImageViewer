package com.dicomimage.api;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;

import com.dicomimage.api.dao.CoordinateServiceImpl.Coordinates;
import com.dicomimage.api.dao.ImageServiceImpl.Images;
import com.dicomimage.api.dao.ImageServiceImpl.ImageDAO;

import java.util.Map;


/*

GET /images/:filename

Sample output with two annotations:
{  "filename":":finename",
   "annotations":[
      {  "name":"annotation1",
         "user":"usera",
         "coordinates":[
            {  "x":"101.34", "y":"24.57" },
            {  "x":"102.40",  "y":"34.17" }
         ]
      },
      {  "name":"annotation2",
         "user":"userc",
         "coordinates":[
            {  "x":"10.34", y":"657.57" },
            { "x":"106.36", "y":"17.12" },
            { "x":"16.39", "y":"66.12" }
         ]
      }
   ]
}

GET /images/:filename/users/:username

Sample output:
{  "filename":":finename",
   "annotations":[
      {  "name":"annotation1",
         "user":":username ",
         "coordinates":[
            {  "x":"107.41", "y":"17.54" },
            {  "x":"18.40",  "y":"4.17" }77
         ]
      }
    ]
}

GET, POST  /images/:filename/users/:username/annotations/:aimname
Sample GET output:
{  "filename":":finename",
    "annotationname":":aimname",
     "user":":username ",
     "coordinates":[
            {  "x":"107.41", "y":"17.54" },
            {  "x":"18.40",  "y":"4.17" }
     ]
}

POST  /images/:filename/users/:username/annotations/:aimname
Sample POST input:
{  "coordinates":[
            {  "x":"107.41", "y":"17.54" },
            {  "x":"18.40",  "y":"4.17" }
     ]
}

* */

@Path("images")
public class ImageResource {

    @GET
    @Produces("application/json")
    public List<Images> getAllImages() {
        ImageDAO dao = new ImageDAO();
        List<Images> imgs = dao.getAllImages();
        return imgs;
    }

    @GET
    @Path("/{filename}")
    @Produces("application/json")
    public Map<String,Object> getImage(@PathParam("filename") String filename) {
        ImageDAO dao = new ImageDAO();
        Map<String,Object>  map= dao.getImageResourceByFile(filename);
        return map;
    }

    @GET
    @Path("/{filename}/users/{username}")
    @Produces("application/json")
    public Map<String,Object>getImage(@PathParam("filename") String filename, @PathParam("username") String username){
        ImageDAO dao = new ImageDAO();
        Map<String,Object> img = dao.getImageByFileAndUser(filename, username);
        return img;
    }

    @GET
    @Path("/{filename}/users/{username}/annotations/{aimname}")
    @Produces("application/json")
    public Map<String,Object> getImage(@PathParam("filename") String filename, @PathParam("username") String username,  @PathParam("aimname") String aimname){
        ImageDAO dao = new ImageDAO();
        Map<String,Object> img = dao.getImageByFileUserAnnotation(filename, username, aimname);
        return img;
    }

    @POST
    @Path("/{filename}/users/{username}/annotations/{aimname}")
    @Consumes("application/json")
    public Response addImage(@PathParam("filename") String filename, @PathParam("username") String username,  @PathParam("aimname") String aimname, Map<String,Object[]> c ){
        ImageDAO dao = new ImageDAO();
        dao.addImage(filename,username,aimname,c);
        return Response
                .status(Response.Status.OK)
                .entity(c)
                .build();
    }
}
