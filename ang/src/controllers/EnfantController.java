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
import models.Enfant;
import etu1789.framework.response.ResponseREST;

@AnnotationScop(scop = "singleton")

public class EnfantController {
    private Connexion_projet dao=new Connexion_projet("empfram", "localhost", "5432", "postgres", "rmirah");

    
    @RestApi(method = "POST", params = true)
@AnnotationUrl(url = "enfant")
public ResponseREST insert(@FromBody Enfant entity) throws Exception{
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
@AnnotationUrl(url = "enfant")
public ResponseREST crudpage() throws Exception{
    ResponseREST response = new ResponseREST();
    Enfant obj = new Enfant();
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
@AnnotationUrl(url = "enfant")
public ResponseREST update(@FromUrl(value="id") int id, @FromBody Enfant entity) throws Exception{
    ResponseREST response = new ResponseREST();
    Enfant where=new Enfant(id);
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
@AnnotationUrl(url = "enfant")
public ResponseREST delete( int id) throws Exception{
    ResponseREST response = new ResponseREST();
    Connection connex=dao.getconnection();
    try{
        Enfant where=new Enfant(id);
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
@AnnotationUrl(url = "enfant")
public ResponseREST getById(int id) throws Exception{
    ResponseREST response = new ResponseREST();
    Connection connex=dao.getconnection();
    try{
        Enfant where=new Enfant(id);
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
@AnnotationUrl(url = "enfantPaging")
public ResponseREST getPaging(@FromUrl(value="limit") int limit, @FromUrl(value="offset") int offset) throws Exception{
    ResponseREST response = new ResponseREST();
    Connection connex=dao.getconnection();
    try{
        Enfant where=new Enfant();
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
@AnnotationUrl(url = "enfantCount")
public ResponseREST getCount() throws Exception{
    ResponseREST response = new ResponseREST();
    Enfant obj = new Enfant();
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

