package com.dicomimage.api.dao.ImageServiceImpl;


import com.dicomimage.api.dao.CoordinateServiceImpl.Coordinates;
import com.dicomimage.api.dao.CoordinateServiceImpl.CoordinateDAO;
import com.dicomimage.api.dao.SessionUtil;
import com.dicomimage.api.dao.AnnotationServiceImpl.AnnotationsDAO;
import com.dicomimage.api.dao.AnnotationServiceImpl.Annotations;
import com.dicomimage.api.dao.UserServiceImpl.User;
import com.dicomimage.api.dao.UserServiceImpl.UserDAO;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.w3c.dom.UserDataHandler;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.lang.reflect.Array;
import java.util.*;

public class ImageDAO {
    AnnotationsDAO annotationsDAO = new AnnotationsDAO();
    CoordinateDAO coordinateDAO = new CoordinateDAO();

    //    public void addImage(Images bean){
//        Session session = SessionUtil.getSession();
//        Transaction tx = session.beginTransaction();
//        addImage(session,bean);
//        tx.commit();
//        session.close();
//    }
//    private void addImage(Session session, Images bean){
//        Images image = new Images();
//        image.setImagename(bean.getImagename());
//        image.setUsername(bean.getUsername());
//        image.setAimname(bean.getAimname());
//        session.save(image);
//    }
    public void addImage(String imagename, String username, String aimname, Map<String, Object[]> c){
        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        User usr = new User();
        usr.setName(username);
        UserDAO uDao = new UserDAO();
        uDao.addUser(usr);
        Images image = new Images();
        image.setImagename(imagename);
        image.setUsername(username);
        AnnotationsDAO aDao = new AnnotationsDAO();
        Object[] coordinateArr = c.get("coordinates");
        aDao.addAnnotation(imagename,username,aimname,coordinateArr);
        image.setAimname(aimname);
        session.save(image);
        tx.commit();
        session.close();
    }

    public List<Images> getAllImages(){
        Session session = SessionUtil.getSession();
        Query query = session.createQuery("from Images");
        List<Images> imgs =  query.list();
        session.close();
        return imgs;
    }

    public Map<String, Object>[] getImageResourceByUsername(String username) {

        Session session = SessionUtil.getSession();
        Query query = session.createQuery("from Images where username = :username");
        query.setString("username", username);
        List<Images> imgs =  query.list();
        Map<String, Object>[] result = new HashMap[imgs.size()];
        int i=0;
        for(Images img:imgs){
            result[i++] = getImageByFileAndUser(img.getImagename(),username);
        }
        return result;
    }

    public Map<String, Object> getImageResourceByFile(String imagename) {
        Map<String, Object> result = new HashMap<>();
        Session session = SessionUtil.getSession();
        Query query = session.createQuery("from Images where imagename = :imagename");
        query.setString("imagename", imagename);
        List<Images> imgs =  query.list();
        session.close();
        if(imgs.size() < 1) {
            throw new NotFoundException(Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Image, " + imagename + ", not found")
                    .build());
        }
        else {
            result.put("filename",imagename);
            Annotations[] annotationsList = annotationsDAO.getAnnotationByImageName(imagename);
            Map<String, Object>[] resultArr = new HashMap[annotationsList.length];
            Set<Annotations> set = new HashSet<>();
            int i=0;
            for(Annotations antn:annotationsList){
                Map<String, Object> annotatinmap = new HashMap<>();
                annotatinmap.put("user",antn.getUsername());
                annotatinmap.put("name",antn.getAimname());
                annotatinmap.put("coordinates",annotationsDAO.getCoordinatesArray(antn.getCoordinateId()));
                resultArr[i++] = annotatinmap;
            }
            result.put("annotations",resultArr);
            return result;
        }
    }

    public Images getImageByFile(String imagename) {
        Session session = SessionUtil.getSession();
        Query query = session.createQuery("from Images where imagename = :imagename");
        query.setString("imagename", imagename);
        List<Images> imgs =  query.list();
        session.close();
        if(imgs.size() < 1) {
            throw new NotFoundException(Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Image, " + imagename + ", not found")
                    .build());
        }
        else return imgs.get(0);
    }

    public Map<String, Object> getImageByFileAndUser(String imagename, String username) {
        Map<String, Object> result = new HashMap<>();
        Session session = SessionUtil.getSession();
        Query query = session.createQuery("from Images where imagename = :imagename and username = :username");
        query.setString("imagename", imagename);
        query.setString("username", username);
        List<Images> imgs =  query.list();
        session.close();
        if(imgs.size() < 1) {
            throw new NotFoundException(Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Image, " + imagename + " for user " + username + " , not found")
                    .build());
        }
        else {
            result.put("filename",imagename);
            Annotations[] annotationsList = annotationsDAO.getAnnotationsByUserFile(username,imagename);
            Map<String, Object>[] resultArr = new HashMap[annotationsList.length];
            Set<Annotations> set = new HashSet<>();
            int i=0;
            for(Annotations antn:annotationsList){
                Map<String, Object> annotatinmap = new HashMap<>();
                annotatinmap.put("user",antn.getUsername());
                annotatinmap.put("name",antn.getAimname());
                annotatinmap.put("coordinates",annotationsDAO.getCoordinatesArray(antn.getCoordinateId()));
                resultArr[i++] = annotatinmap;
            }
            result.put("annotations",resultArr);
            return result;
        }
    }
    public Map<String,Object> getImageByFileUserAnnotation(String imagename, String username, String aimname) {
        Map<String, Object> result = new HashMap<>();
        Session session = SessionUtil.getSession();
        Query query = session.createQuery("from Images where imagename = :imagename and username = :username and aimname = :aimname");
        query.setString("imagename", imagename);
        query.setString("username", username);
        query.setString("aimname", aimname);
        List<Images> imgs =  query.list();
        session.close();
        if(imgs.size() < 1) {
            throw new NotFoundException(Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Image, " + imagename + " for user " + username + " with annotation " + aimname + " , not found")
                    .build());
        }
        else {
            result.put("filename",imagename);
            Annotations antn = annotationsDAO.getAnnotationsByUserFileAimname(username,imagename,aimname);
            result.put("user",antn.getUsername());
            result.put("name",antn.getAimname());
            result.put("annotationname", antn.getAimname());
            result.put("coordinates",annotationsDAO.getCoordinatesArray(antn.getCoordinateId()));
            return result;
        }
    }


    public int deleteImage(int id) {
        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        String hql = "delete from Images where id = :id";
        Query query = session.createQuery(hql);
        query.setInteger("id",id);
        int rowCount = query.executeUpdate();
        System.out.println("Rows affected: " + rowCount);
        tx.commit();
        session.close();
        return rowCount;
    }

    public int updateImage(int id, Images img){
        if(id <=0)
            return 0;
        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        String hql = "update Images set imagename = :imagename, username=:username where id = :id";
        Query query = session.createQuery(hql);
        query.setInteger("id",id);
        query.setString("username",img.getUsername());
        query.setString("imagename",img.getImagename());
        int rowCount = query.executeUpdate();
        System.out.println("Rows affected: " + rowCount);
        tx.commit();
        session.close();
        return rowCount;
    }


}
