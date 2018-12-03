package com.dicomimage.api.dao.UserServiceImpl;

import com.dicomimage.api.dao.ImageServiceImpl.ImageDAO;
import com.dicomimage.api.dao.SessionUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDAO {

    public void addUser(User bean){
        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        addUser(session,bean);
        tx.commit();
        session.close();

    }

    private void addUser(Session session, User bean){
        User user = new User();
        user.setName(bean.getName());
        session.save(user);
    }

    public List<User> getUsers(){
        Session session = SessionUtil.getSession();
        Query query = session.createQuery("from User");
        List<User> users =  query.list();
        session.close();
        return users;
    }

    public Map<String,Object> getUser(String username){
        Session session = SessionUtil.getSession();
        Query query = session.createQuery("from User where name = :username");
        query.setString("username", username);
        List<User> usr =  query.list();
        session.close();
        if(usr.size() < 1) {
            throw new NotFoundException(Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("User, " + username + ", not found")
                    .build());
        }
        else {
            Map<String, Object> result = new HashMap<>();
            ImageDAO iDao = new ImageDAO();
            result.put("username",username);
            result.put("files" ,iDao.getImageResourceByUsername(username));
            return result;
        }
    }

    public int deleteUser(int id) {
        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        String hql = "delete from User where id = :id";
        Query query = session.createQuery(hql);
        query.setInteger("id",id);
        int rowCount = query.executeUpdate();
        System.out.println("Rows affected: " + rowCount);
        tx.commit();
        session.close();
        return rowCount;
    }

    public int updateUser(int id, User emp){
        if(id <=0)
            return 0;
        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        String hql = "update User set name = :name, email=:email where id = :id";
        Query query = session.createQuery(hql);
        query.setInteger("id",id);
        query.setString("name",emp.getName());
        int rowCount = query.executeUpdate();
        System.out.println("Rows affected: " + rowCount);
        tx.commit();
        session.close();
        return rowCount;
    }
}
