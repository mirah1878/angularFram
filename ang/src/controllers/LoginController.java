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
import models.Login;
import etu1789.framework.response.ResponseREST;

@AnnotationScop(scop = "singleton")

public class LoginController {
    private Connexion_projet dao=new Connexion_projet("empfram", "localhost", "5432", "postgres", "rmirah");

    
    @RestApi(method = "POST", params = true)
@AnnotationUrl(url = "login")
public ResponseREST insert(@FromBody Login entity) throws Exception{
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
@AnnotationUrl(url = "login")
public ResponseREST crudpage() throws Exception{
    ResponseREST response = new ResponseREST();
    Login obj = new Login();
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
@AnnotationUrl(url = "login")
public ResponseREST update(@FromUrl(value="id") int id, @FromBody Login entity) throws Exception{
    ResponseREST response = new ResponseREST();
    Login where=new Login(id);
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
@AnnotationUrl(url = "login")
public ResponseREST delete( int id) throws Exception{
    ResponseREST response = new ResponseREST();
    Connection connex=dao.getconnection();
    try{
        Login where=new Login(id);
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
@AnnotationUrl(url = "login")
public ResponseREST getById(int id) throws Exception{
    ResponseREST response = new ResponseREST();
    Connection connex=dao.getconnection();
    try{
        Login where=new Login(id);
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
@AnnotationUrl(url = "loginPaging")
public ResponseREST getPaging(@FromUrl(value="limit") int limit, @FromUrl(value="offset") int offset) throws Exception{
    ResponseREST response = new ResponseREST();
    Connection connex=dao.getconnection();
    try{
        Login where=new Login();
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
@AnnotationUrl(url = "loginCount")
public ResponseREST getCount() throws Exception{
    ResponseREST response = new ResponseREST();
    Login obj = new Login();
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
@RestApi(method = "POST", params = true)
@AnnotationUrl(url = "loginVue")
public ResponseREST login(@FromBody Login entity) throws Exception{
    ResponseREST response = new ResponseREST();
    Connection connex=dao.getconnection();
    try{
        if((entity.findAll(connex)).size() != 0){
            response.setData((entity.findAll(connex)).get(0));
        }else{
            response.setData(false);   
        }
        response.setMessage("success");
    }catch (Exception e) {
        connex.rollback();
        response.setMessage(e.getMessage());
    }finally{
        connex.close();
    }
    return response;
}

}

