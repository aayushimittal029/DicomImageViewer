package com.dicomimage.api.dao.CoordinateServiceImpl;

import com.dicomimage.api.dao.SessionUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.*;


public class CoordinateDAO {

    public Coordinates addCoordinate(Float x, Float y) {
        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        Coordinates c = new Coordinates(x, y);
        session.save(c);
        tx.commit();
        return c;
    }
    public Coordinates addCoordinate(Coordinates coord) {

        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        session.save(coord);
        tx.commit();
        return coord;
    }
    public List<Coordinates> getAllCoordinatesList(){
        Session session = SessionUtil.getSession();
        Query query = session.createQuery("from Coordinates");
        List<Coordinates> coordinates =  query.list();
        session.close();
        return coordinates;

    }

    //    private Set<Point>[] pointsFormat(List<Coordinates> coordinates) {
//        Set<Point>[] result = new HashSet[coordinates.size()];
//        int i = 0;
//        for (Coordinates c : coordinates) {
//            Set<Point> points = new HashSet<>();
//            points.add(new Point(c.getX1(), c.getY1()));
//            points.add(new Point(c.getX2(), c.getY2()));
//            result[i++] = points;
//            points.clear();
//        }
//        System.out.println("******************" +result);
//        return result;
//    }
    public Coordinates[] getCoordinatesByListOfId(int[] ids) {
        List<Integer> idList = new ArrayList<>();
        for(int i:ids) idList.add(i);

        Coordinates[] result = new Coordinates[ids.length];
        int i=0;
        List<Coordinates> allC = getAllCoordinatesList();
        for(Coordinates c: allC){
            if(idList.contains(c.getId())){
                result[i++]= c;
            }
        }
        return result;
    }
    public Map<String, String> getCoordinatesById(int coordinateId) {
        Session session = SessionUtil.getSession();
        Query query = session.createQuery("from Coordinates where id= :coordinateId");
        query.setInteger("coordinateId", coordinateId);
        List<Coordinates> coordinates =  query.list();
        session.close();
        if(coordinates.size() < 1) {
            throw new NotFoundException(Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Error")
                    .build());
        }
        else {
            Map<String, String> map = new HashMap<>();
            map.put("x", String.valueOf(coordinates.get(0).getX()));
            map.put("y", String.valueOf(coordinates.get(0).getY()));
            return map;
        }
    }
}
