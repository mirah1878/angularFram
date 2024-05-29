package controllers;
import etu1789.framework.annotation.AnnotationScop;
import etu1789.framework.annotation.AnnotationUrl;
import etu1789.framework.annotation.FromBody;
import etu1789.framework.annotation.FromHeader;
import etu1789.framework.annotation.FromUrl;
import etu1789.framework.annotation.RestApi;
import etu1789.framework.upload.FileUpload;
import etu1789.dao.connexion.Connexion_projet;
import java.sql.Connection;
import models.Avion;
import etu1789.framework.response.ResponseREST;

@AnnotationScop(scop = "singleton")

public class AvionController {
    private Connexion_projet dao=new Connexion_projet("avion", "localhost", "5432", "postgres", "rmirah");

    
    @RestApi(method = "POST", params = true)
@AnnotationUrl(url = "avion")
public ResponseREST insert(@FromBody Avion entity) throws Exception{
    ResponseREST response = new ResponseREST();
    Connection connex=dao.getconnection();
    try{
        entity.create(connex);
        response.setMessage("save success");
        connex.commit();
    }catch (Exception e) {
        connex.rollback();
        response.setMessage(e.getMessage());
    }finally{
        connex.close();
    }
    return response;
}
@RestApi(method = "GET", params = false)
@AnnotationUrl(url = "avion")
public ResponseREST crudpage() throws Exception{
    ResponseREST response = new ResponseREST();
    Avion obj = new Avion();
    Connection connex=dao.getconnection();
    try{
       response.setData(obj.findAll(connex));
        response.setMessage("success");
    }catch (Exception e) {
        response.setMessage(e.getMessage());
    }finally{
        connex.close();
    }
    return response;
}
@RestApi(method = "PUT", params = true)
@AnnotationUrl(url = "avion")
public ResponseREST update(@FromUrl(value="id") int id, @FromBody Avion entity) throws Exception{
    ResponseREST response = new ResponseREST();
    Avion where=new Avion(id);
    Connection connex=dao.getconnection();
    try{
        where.update(connex, entity);
        response.setMessage("update success");
        connex.commit();
    }catch (Exception e) {
        connex.rollback();
        response.setMessage(e.getMessage());
    }finally{
        connex.close();
    }
    return response;
}
@RestApi(method = "DELETE", params = true)
@AnnotationUrl(url = "avion")
public ResponseREST delete( int id) throws Exception{
    ResponseREST response = new ResponseREST();
    Connection connex=dao.getconnection();
    try{
        Avion where=new Avion(id);
        where.delete(connex);
        response.setMessage("Delete success");
        connex.commit();
    }catch (Exception e) {
        connex.rollback();
        response.setMessage(e.getMessage());
    }finally{
        connex.close();
    }
    return response;
}
@RestApi(method = "GET", params = true)
@AnnotationUrl(url = "avion")
public ResponseREST getById(int id) throws Exception{
    ResponseREST response = new ResponseREST();
    Connection connex=dao.getconnection();
    try{
        Avion where=new Avion(id);
        response.setData(where.findAll(connex).get(0));
        response.setMessage("success");
    }catch (Exception e) {
         response.setMessage(e.getMessage());
    }finally{
        connex.close();
    }
    return response;
}
@RestApi(method = "GET", params = true)
@AnnotationUrl(url = "avionPaging")
public ResponseREST getPaging(@FromUrl(value="limit") int limit, @FromUrl(value="offset") int offset) throws Exception{
    ResponseREST response = new ResponseREST();
    Connection connex=dao.getconnection();
    try{
        Avion where=new Avion();
        response.setData(where.findPaging(connex, limit, offset));
        response.setMessage("success");
    }catch (Exception e) {
         response.setMessage(e.getMessage());
    }finally{
        connex.close();
    }
    return response;
}
@RestApi(method = "GET", params = false)
@AnnotationUrl(url = "avionCount")
public ResponseREST getCount() throws Exception{
    ResponseREST response = new ResponseREST();
    Avion obj = new Avion();
    Connection connex=dao.getconnection();
    try{
       response.setData(obj.getCount(connex));
        response.setMessage("success");
    }catch (Exception e) {
        response.setMessage(e.getMessage());
    }finally{
        connex.close();
    }
    return response;
}

}

