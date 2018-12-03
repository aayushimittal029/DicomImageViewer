package com.dicomimage.api.dao.AnnotationServiceImpl;


import com.dicomimage.api.dao.CoordinateServiceImpl.Coordinates;
import com.dicomimage.api.dao.CoordinateServiceImpl.CoordinateDAO;
import com.dicomimage.api.dao.SessionUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AnnotationsDAO {

    public void addAnnotation(Annotations bean){
        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        addAnnotation(session,bean);
        tx.commit();
        session.close();

    }

    public void addAnnotation(String imagename, String username, String aimname, Object[] c) {
        Session session = SessionUtil.getSession();
        CoordinateDAO cDao = new CoordinateDAO();
        Transaction tx = session.beginTransaction();
        Annotations annotation = new Annotations();
        annotation.setAimname(aimname);
        annotation.setImagename(imagename);
        annotation.setUsername(username);
        StringBuilder sb = new StringBuilder();

        for(Object obj: c) {
            HashMap<String,String> map = (HashMap<String,String>)obj;
            Coordinates temp = cDao.addCoordinate(Float.parseFloat(map.get("x")),Float.parseFloat(map.get("y")));
            if (sb.length() > 0) sb.append(',');
            sb.append(String.valueOf(temp.getId()));
        }
        annotation.setCoordinateId(sb.toString());
        session.save(annotation);
        tx.commit();

    }

    private void addAnnotation(Session session, Annotations bean){
        Annotations annotation = new Annotations();
        annotation.setAimname(bean.getAimname());
        annotation.setImagename(bean.getImagename());
        annotation.setUsername(bean.getUsername());
        annotation.setCoordinateId(bean.getCoordinateId());
        session.save(annotation);
    }

    public Annotations[] getAnnotationsByUserFile(String username, String imagename){
        Session session = SessionUtil.getSession();
        Query query = session.createQuery("from Annotations where username = :username and imagename = :imagename");
        query.setString("username", username);
        query.setString("imagename", imagename);
        List<Annotations> annotations =  query.list();
        session.close();
        Annotations[] result = new Annotations[annotations.size()];
        int i=0;
        for(Annotations antn:annotations){
            result[i++] = antn;
        }
        return result;
    }
    public Annotations getAnnotationsByUserFileAimname(String username, String imagename, String aimname){
        Session session = SessionUtil.getSession();
        Query query = session.createQuery("from Annotations where username = :username and imagename = :imagename and aimname= :aimname");
        query.setString("username", username);
        query.setString("imagename", imagename);
        query.setString("aimname", aimname);

        List<Annotations> annotations =  query.list();
        session.close();
        if(annotations.size() < 1) {
            throw new NotFoundException(Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Annotation, " + aimname + ", not found")
                    .build());
        }
        else return annotations.get(0);
    }

    public List<Annotations> getAnnotationsByName(String aimname){
        Session session = SessionUtil.getSession();
        Query query = session.createQuery("from Annotations where aimname = :aimname");
        query.setString("aimname", aimname);
        List<Annotations> annotations =  query.list();
        session.close();
        return annotations;
    }


    public Annotations[] getAllAnnotations() {

        Session session = SessionUtil.getSession();
        Query query = session.createQuery("from Annotations");
        List<Annotations> annotations =  query.list();
        Annotations[] result = new Annotations[annotations.size()];
        int i=0;
        for(Annotations antn:annotations){
            result[i++] = antn;
        }
        session.close();
        return result;
    }

    public Annotations[] getAnnotationByImageName(String imagename) {
        Session session = SessionUtil.getSession();
        Query query = session.createQuery("from Annotations where imagename = :imagename");
        query.setString("imagename", imagename);
        List<Annotations> annotations =  query.list();
        session.close();
        Annotations[] result = new Annotations[annotations.size()];
        int i=0;
        for(Annotations antn:annotations){
            result[i++] = antn;
        }
        return result;
    }

    public Object getCoordinatesArray(String coordinateId) {
        CoordinateDAO cDao = new CoordinateDAO();
        Object[] theArr = new Object[coordinateId.split(",").length];
        int i=0;
        for(String cId : coordinateId.split(",")){
            theArr[i++] = cDao.getCoordinatesById(Integer.parseInt(cId));
        }
        return theArr;
    }
}
