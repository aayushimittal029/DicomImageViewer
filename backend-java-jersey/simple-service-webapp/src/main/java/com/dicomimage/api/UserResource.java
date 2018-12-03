package com.dicomimage.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.dicomimage.api.dao.UserServiceImpl.User;
import com.dicomimage.api.dao.UserServiceImpl.UserDAO;
/**
 * Root resource (exposed at "rest" path)
 */
@Path("users")
public class UserResource {

    @GET
    @Produces("application/json")
    public List<User> getUsers() {
        UserDAO dao = new UserDAO();
        List<User> users = dao.getUsers();
        return users;
    }

    @GET
    @Path("/{username}")
    @Produces("application/json")
    public Object getUser(@PathParam("username") String username) {
        UserDAO dao = new UserDAO();
        return dao.getUser(username);

    }


    @PUT
    @Path("/update/{id}")
    @Consumes("application/json")
    public Response updateUser(@PathParam("id") int id, User emp){
        UserDAO dao = new UserDAO();
        int count = dao.updateUser(id, emp);
        if(count==0){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.ok().build();
    }

    @DELETE
    @Path("/delete/{id}")
    @Consumes("application/json")
    public Response deleteUser(@PathParam("id") int id){
        UserDAO dao = new UserDAO();
        int count = dao.deleteUser(id);
        if(count==0){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.ok().build();
    }
}
