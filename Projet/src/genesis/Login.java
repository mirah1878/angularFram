package genesis;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import handyman.HandyManUtils;

public class Login {
    private boolean addLogin;
    private boolean tableExists;
    private String nameTable;
    private List<String> columnReference;
    private List<String> columnReferenceType;
    private Scripts scripts;
    private String viewServiceContent;
    private String viewContraiteCallContent;
    private String viewImportLogout;
    private String viewLogoutContent;
    private String viewRouteLogin;
    private String viewSidebarContent;
    private String viewLoginPrincipaletemplate;
    private String viewLoginSaveContent;

    public void ExecuteScript(Connection connexion) throws SQLException{
        Statement statement = null;
        try {
            if(!tableExists && addLogin){
                statement = connexion.createStatement();
                for (String script : scripts.getCreateTable()) {
                    statement.executeUpdate(script);
                }
                for (String string : scripts.getInsert()) {
                    statement.executeUpdate(string);
                }
                connexion.commit();
            }
        } catch (Exception e) {
            connexion.rollback();
            throw e;
        }finally{
            if(statement != null) statement.close();
        }
    }
    public String genereteLogout (Entity entity ) throws IOException{
        String content =  HandyManUtils.getFileContent(
            Constantes.DATA_PATH + "/" + getViewLogoutContent() + "."
                    + Constantes.SERVICE_EXTENSION);
                    content = content.replace("[classNameMin]", HandyManUtils.minStart(entity.getClassName()));
                    content = content.replace("[classNameMaj]", HandyManUtils.majStart(entity.getClassName()));            
        
        return content;
    } 

    public String genereteSaveLogin() throws IOException{
        String content = HandyManUtils.getFileContent(
                Constantes.DATA_PATH + "/" + getViewLoginSaveContent() + "."
                        + Constantes.SERVICE_EXTENSION);
                        content = content.replace("[input1Min]", HandyManUtils.minStart((HandyManUtils.minAll(columnReference.get(0)))));
                    content = content.replace("[input2Min]", HandyManUtils.minStart((HandyManUtils.minAll(columnReference.get(1)))));            
                    content = content.replace("[input1Maj]", HandyManUtils.majStart((HandyManUtils.minAll(columnReference.get(0)))));
                    content = content.replace("[input2Maj]", HandyManUtils.majStart((HandyManUtils.minAll(columnReference.get(1)))));            
                   
                    return content;
    }

    public String genereteContrainte() throws IOException{
        String content = HandyManUtils.getFileContent(
            Constantes.DATA_PATH + "/" + getViewContraiteCallContent() + "."
                    + Constantes.SERVICE_EXTENSION);
                    return content;
    }

    public boolean isAddLogin() {
        return addLogin;
    }
    public void setAddLogin(boolean addLogin) {
        this.addLogin = addLogin;
    }
    public boolean isTableExists() {
        return tableExists;
    }
    public void setTableExists(boolean tableExists) {
        this.tableExists = tableExists;
    }
    public String getNameTable() {
        return nameTable;
    }
    public void setNameTable(String nameTable) {
        this.nameTable = nameTable;
    }
    public List<String> getColumnReference() {
        return columnReference;
    }
    public void setColumnReference(List<String> columnReference) {
        this.columnReference = columnReference;
    }
    public List<String> getColumnReferenceType() {
        return columnReferenceType;
    }
    public void setColumnReferenceType(List<String> columnReferenceType) {
        this.columnReferenceType = columnReferenceType;
    }
    public Scripts getScripts() {
        return scripts;
    }
    public void setScripts(Scripts scripts) {
        this.scripts = scripts;
    }

    public String getViewServiceContent() {
        return viewServiceContent;
    }

    public void setViewServiceContent(String viewServiceContent) {
        this.viewServiceContent = viewServiceContent;
    }

    public String getViewContraiteCallContent() {
        return viewContraiteCallContent;
    }

    public void setViewContraiteCallContent(String viewContraiteCallContent) {
        this.viewContraiteCallContent = viewContraiteCallContent;
    }

    public String getViewImportLogout() {
        return viewImportLogout;
    }

    public void setViewImportLogout(String viewImportLogout) {
        this.viewImportLogout = viewImportLogout;
    }

    public String getViewLogoutContent() {
        return viewLogoutContent;
    }

    public void setViewLogoutContent(String viewLogoutContent) {
        this.viewLogoutContent = viewLogoutContent;
    }

    public String getViewRouteLogin() {
        return viewRouteLogin;
    }

    public void setViewRouteLogin(String viewRouteLogin) {
        this.viewRouteLogin = viewRouteLogin;
    }

    public String getViewSidebarContent() {
        return viewSidebarContent;
    }

    public void setViewSidebarContent(String viewSidebarContent) {
        this.viewSidebarContent = viewSidebarContent;
    }

    public String getViewLoginPrincipaletemplate() {
        return viewLoginPrincipaletemplate;
    }

    public void setViewLoginPrincipaletemplate(String viewLoginPrincipaletemplate) {
        this.viewLoginPrincipaletemplate = viewLoginPrincipaletemplate;
    }

    public String getViewLoginSaveContent() {
        return viewLoginSaveContent;
    }

    public void setViewLoginSaveContent(String viewLoginSaveContent) {
        this.viewLoginSaveContent = viewLoginSaveContent;
    }

}
