package genesis;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import handyman.HandyManUtils;

public class Language {
    private int id;
    private String nom;
    private HashMap<String, String> syntax;
    private HashMap<String, String> types, typeParsers;
    private String skeleton;
    private String[] projectNameTags;
    private CustomFile[] additionnalFiles;
    private Model model;
    private Controller controller;
    private View view;
    private CustomChanges[] customChanges;
    private NavbarLink navbarLinks;

    public NavbarLink getNavbarLinks() {
        return navbarLinks;
    }

    public void setNavbarLinks(NavbarLink navbarLinks) {
        this.navbarLinks = navbarLinks;
    }

    public CustomChanges[] getCustomChanges() {
        return customChanges;
    }

    public void setCustomChanges(CustomChanges[] customChanges) {
        this.customChanges = customChanges;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public HashMap<String, String> getSyntax() {
        return syntax;
    }

    public void setSyntax(HashMap<String, String> syntax) {
        this.syntax = syntax;
    }

    public HashMap<String, String> getTypes() {
        return types;
    }

    public void setTypes(HashMap<String, String> types) {
        this.types = types;
    }

    public HashMap<String, String> getTypeParsers() {
        return typeParsers;
    }

    public void setTypeParsers(HashMap<String, String> typeParsers) {
        this.typeParsers = typeParsers;
    }

    public String getSkeleton() {
        return skeleton;
    }

    public void setSkeleton(String skeleton) {
        this.skeleton = skeleton;
    }

    public String[] getProjectNameTags() {
        return projectNameTags;
    }

    public void setProjectNameTags(String[] projectNameTags) {
        this.projectNameTags = projectNameTags;
    }

    public CustomFile[] getAdditionnalFiles() {
        return additionnalFiles;
    }

    public void setAdditionnalFiles(CustomFile[] additionnalFiles) {
        this.additionnalFiles = additionnalFiles;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public String generateModel(Entity entity, String projectName) throws IOException, Exception {
        String content = HandyManUtils.getFileContent(
                Constantes.DATA_PATH + "/" + getModel().getModelTemplate() + "." + Constantes.MODEL_TEMPLATE_EXT);
        content = content.replace("[namespace]", getSyntax().get("namespace"));
        content = content.replace("[namespaceStart]", getSyntax().get("namespaceStart"));
        content = content.replace("[namespaceEnd]", getSyntax().get("namespaceEnd"));
        content = content.replace("[package]", getModel().getModelPackage());
        String imports = "";
        for (String i : getModel().getModelImports()) {
            imports += i + "\n";
        }
        content = content.replace("[imports]", imports);
        String annotes = "";
        for (String a : getModel().getModelAnnotations()) {
            annotes += a + "\n";
        }
        content = content.replace("[classAnnotations]", annotes);
        content = content.replace("[extends]", getModel().getModelExtends());
        String constructors = "";
        for (String c : getModel().getModelConstructors()) {
            constructors += c + "\n";
        }
        content = content.replace("[constructors]", constructors);
        String fields = "", fieldAnnotes;
        for (int i = 0; i < entity.getFields().length; i++) {
            fieldAnnotes = "";
            if (entity.getFields()[i].isPrimary()) {
                for (String primAnnote : getModel().getModelPrimaryFieldAnnotations()) {
                    fieldAnnotes += primAnnote + "\n";
                }
            } else if (entity.getFields()[i].isForeign()) {

                int j = 0;
                for (String forAnnote : getModel().getModelForeignFieldAnnotations()) {
                    forAnnote = forAnnote + "\n" + getModel().getModelFieldAnnotations()[j];
                    j++;
                    fieldAnnotes += forAnnote + "\n";
                    fieldAnnotes = fieldAnnotes.replace("[columnNameForeign]",
                            HandyManUtils.toCamelCase(entity.getColumns()[i].getReferencedColumn()));
                    fieldAnnotes = fieldAnnotes.replace("[referencedFieldNameMin]",
                            HandyManUtils.minStart(entity.getFields()[i].getReferencedField()));
                    fieldAnnotes = fieldAnnotes.replace("[referencedFieldNameMaj]",
                            HandyManUtils.majStart(entity.getFields()[i].getReferencedField()));
                }
            } else if (!entity.getFields()[i].isPrimary() || entity.getFields()[i].isForeign()) {
                for (String fa : getModel().getModelFieldAnnotations()) {
                    fieldAnnotes += fa + "\n";
                }
            }

            fields += fieldAnnotes;
            fields += getModel().getModelFieldContent() + "\n";
            fields += getModel().getModelGetter() + "\n";
            fields += getModel().getModelSetter() + "\n";
            fields = fields.replace("[columnName]", entity.getColumns()[i].getName());
            fields = fields.replace("[fieldType]", entity.getFields()[i].getType());
            fields = fields.replace("[modelFieldCase]", getModel().getModelFieldCase());
            fields = fields.replace("[fieldNameMin]", HandyManUtils.minStart(entity.getFields()[i].getName()));
            fields = fields.replace("[fieldNameMaj]", HandyManUtils.majStart(entity.getFields()[i].getName()));
        }
        content = content.replace("[fields]", fields);
        content = content.replace("[projectNameMin]", HandyManUtils.minStart(projectName));
        content = content.replace("[projectNameMaj]", HandyManUtils.majStart(projectName));
        content = content.replace("[classNameMaj]", HandyManUtils.majStart(entity.getClassName()));
        content = content.replace("[modelFieldCase]", getModel().getModelFieldCase());
        content = content.replace("[primaryFieldType]", entity.getPrimaryField().getType());
        content = content.replace("[primaryFieldNameMin]", HandyManUtils.minStart(entity.getPrimaryField().getName()));
        content = content.replace("[primaryFieldNameMaj]", HandyManUtils.majStart(entity.getPrimaryField().getName()));
        content = content.replace("[tableName]", entity.getTableName());
        return content;
    }

    public String generateController(Entity entity, Database database, Credentials credentials, String projectName, Login login)
            throws IOException, Exception {
        String content = HandyManUtils.getFileContent(Constantes.DATA_PATH + "/"
                + getController().getControllerTemplate() + "." + Constantes.CONTROLLER_TEMPLATE_EXT);
        content = content.replace("[namespace]", getSyntax().get("namespace"));
        content = content.replace("[namespaceStart]", getSyntax().get("namespaceStart"));
        content = content.replace("[namespaceEnd]", getSyntax().get("namespaceEnd"));
        content = content.replace("[package]", getController().getControllerPackage());
        String imports = "";
        for (String i : getController().getControllerImports()) {
            imports += i + "\n";
        }
        content = content.replace("[imports]", imports);
        String annotes = "";
        for (String a : getController().getControllerAnnotations()) {
            annotes += a + "\n";
        }
        content = content.replace("[controllerAnnotations]", annotes);
        content = content.replace("[extends]", getController().getControllerExtends());
        String fields = "", fieldAnnotes;
        for (ControllerField cf : getController().getControllerFields()) {
            fieldAnnotes = "";
            for (String a : cf.getControllerFieldAnnotations()) {
                fieldAnnotes += a + "\n";
            }
            fields += fieldAnnotes;
            fields += cf.getControllerFieldContent() + "\n";
        }
        for (EntityField ef : entity.getFields()) {
            if (ef.isForeign() && getController().getControllerFieldsForeign() != null) {
                fieldAnnotes = "";
                for (String a : getController().getControllerFieldsForeign().getControllerFieldAnnotations()) {
                    fieldAnnotes += a + "\n";
                }
                fields += fieldAnnotes;
                fields += getController().getControllerFieldsForeign().getControllerFieldContent() + "\n";
                fields = fields.replace("[foreignNameMaj]", HandyManUtils.majStart(ef.getType()));
                fields = fields.replace("[foreignNameMin]", HandyManUtils.minStart(ef.getType()));
            }
        }
        content = content.replace("[fields]", fields);
        String constructors = "";
        String constructorParameter, foreignInstanciation;
        for (String c : getController().getControllerConstructors()) {
            constructorParameter = "";
            foreignInstanciation = "";
            for (EntityField ef : entity.getFields()) {
                if (ef.isForeign()) {
                    constructorParameter = constructorParameter + ","
                            + getController().getControllerForeignContextParam();
                    constructorParameter = constructorParameter.replace("[foreignNameMaj]",
                            HandyManUtils.majStart(ef.getName()));
                    foreignInstanciation += getController().getControllerForeignContextInstanciation();
                    foreignInstanciation = foreignInstanciation.replace("[foreignNameMaj]",
                            HandyManUtils.majStart(ef.getName())) + "\n";
                }
            }
            constructors += c + "\n";
            constructors = constructors.replace("[controllerForeignContextParam]", constructorParameter);
            constructors = constructors.replace("[controllerForeignContextInstanciation]", foreignInstanciation);
        }
        content = content.replace("[constructors]", constructors);
        String methods = "", methodAnnotes, methodParameters;
        String changeInstanciation, whereInstanciation, foreignList, foreignInclude;
        for (ControllerMethod m : getController().getControllerMethods()) {
            if(!entity.getTableName().equals(login.getNameTable()) && m.getControllerMethodContent().equals("flamework/controller/flameworkLoginFunction")){
                continue;
            }
            methodAnnotes = "";
            for (String ma : m.getControllerMethodAnnotations()) {
                methodAnnotes += ma + "\n";
            }
            methods += methodAnnotes;
            methodParameters = "";
            for (EntityField ef : entity.getFields()) {
                methodParameters += m.getControllerMethodParameter();
                if (methodParameters.isEmpty() == false) {
                    methodParameters += ",";
                }
                methodParameters = methodParameters.replace("[fieldType]", ef.getType());
                methodParameters = methodParameters.replace("[fieldNameMin]", HandyManUtils.minStart(ef.getName()));
            }
            if (methodParameters.isEmpty() == false) {
                methodParameters = methodParameters.substring(0, methodParameters.length() - 1);
            }
            methods += HandyManUtils.getFileContent(Constantes.DATA_PATH + "/" + m.getControllerMethodContent() + "."
                    + Constantes.CONTROLLER_TEMPLATE_EXT);
            methods = methods.replace("[controllerMethodParameter]", methodParameters);
            changeInstanciation = "";
            foreignList = "";
            foreignInclude = "";
            for (int i = 0; i < entity.getFields().length; i++) {
                if (entity.getFields()[i].isPrimary()) {
                    continue;
                } else if (entity.getFields()[i].isForeign()) {
                    changeInstanciation += getController().getControllerForeignInstanciation().get("template");
                    changeInstanciation = changeInstanciation.replace("[content]", getTypeParsers()
                            .get(getTypes().get(database.getTypes().get(entity.getColumns()[i].getType()))));
                    changeInstanciation = changeInstanciation.replace("[value]",
                            getController().getControllerForeignInstanciation().get("value"));
                    changeInstanciation = changeInstanciation.replace("[fieldNameMin]",
                            HandyManUtils.minStart(entity.getFields()[i].getName()));
                    changeInstanciation = changeInstanciation.replace("[fieldNameMaj]",
                            HandyManUtils.majStart(entity.getFields()[i].getName()));
                    changeInstanciation = changeInstanciation.replace("[foreignType]", entity.getFields()[i].getType());
                    changeInstanciation = changeInstanciation.replace("[referencedFieldNameMaj]",
                            HandyManUtils.majStart(entity.getFields()[i].getReferencedField()));
                    changeInstanciation = changeInstanciation.replace("[foreignNameMin]",
                            HandyManUtils.minStart(entity.getFields()[i].getName()));
                    foreignList += getController().getControllerForeignList();
                    foreignList = foreignList.replace("[foreignType]", entity.getFields()[i].getType());
                    foreignList = foreignList.replace("[foreignNameMin]",
                            HandyManUtils.minStart(entity.getFields()[i].getName()));
                    foreignInclude += getController().getControllerForeignInclude();
                    foreignInclude = foreignInclude.replace("[foreignNameMaj]",
                            HandyManUtils.majStart(entity.getFields()[i].getName()));
                    continue;
                }
                changeInstanciation += getController().getControllerChangeInstanciation().get("template");

                changeInstanciation = changeInstanciation.replace("[content]",
                        getTypeParsers().get(entity.getFields()[i].getType()));
                changeInstanciation = changeInstanciation.replace("[value]",
                        getController().getControllerChangeInstanciation().get("value"));
                changeInstanciation = changeInstanciation.replace("[fieldNameMin]",
                        HandyManUtils.minStart(entity.getFields()[i].getName()));
                changeInstanciation = changeInstanciation.replace("[fieldNameMaj]",
                        HandyManUtils.majStart(entity.getFields()[i].getName()));
            }
            whereInstanciation = "";
            whereInstanciation += getController().getControllerWhereInstanciation().get("template");
            whereInstanciation = whereInstanciation.replace("[content]",
                    getTypeParsers().get(entity.getPrimaryField().getType()));
            whereInstanciation = whereInstanciation.replace("[value]",
                    getController().getControllerWhereInstanciation().get("value"));
            methods = methods.replace("[primaryParse]",
                    getTypeParsers().get(entity.getPrimaryField().getType()).replace("[value]", "[primaryNameMin]"));
            methods = methods.replace("[controllerChangeInstanciation]", changeInstanciation);
            methods = methods.replace("[controllerWhereInstanciation]", whereInstanciation);
            methods = methods.replace("[controllerForeignList]", foreignList);
            methods = methods.replace("[controllerForeignInclude]", foreignInclude);
            methods = methods.replace("[classNameMin]", HandyManUtils.minStart(entity.getClassName()));
            methods = methods.replace("[classNameMaj]", HandyManUtils.majStart(entity.getClassName()));
            methods = methods.replace("[primaryNameMaj]", HandyManUtils.majStart(entity.getPrimaryField().getName()));
            methods = methods.replace("[primaryType]", entity.getPrimaryField().getType());
            methods = methods.replace("[primaryNameMin]", HandyManUtils.minStart(entity.getPrimaryField().getName()));
            methods = methods.replace("[databaseDriver]", database.getDriver());
            methods = methods.replace("[databaseSgbd]", database.getNom());
            methods = methods.replace("[databaseHost]", credentials.getHost());
            methods = methods.replace("[databasePort]", database.getPort());
            methods = methods.replace("[databaseName]", credentials.getDatabaseName());
            methods = methods.replace("[user]", credentials.getUser());
            methods = methods.replace("[pwd]", credentials.getPwd());
            methods = methods.replace("[databaseUseSSL]", String.valueOf(credentials.isUseSSL()));
            methods = methods.replace("[databaseAllowKey]", String.valueOf(credentials.isAllowPublicKeyRetrieval()));
        }
        content = content.replace("[methods]", methods);
        content = content.replace("[controllerNameMaj]", getController().getControllerName());
        content = content.replace("[classNameMaj]", HandyManUtils.majStart(entity.getClassName()));
        content = content.replace("[classNameMin]", HandyManUtils.minStart(entity.getClassName()));
        content = content.replace("[projectNameMin]", HandyManUtils.minStart(projectName));
        content = content.replace("[projectNameMaj]", HandyManUtils.majStart(projectName));
        content = content.replace("[databaseDriver]", database.getDriver());
        content = content.replace("[databaseSgbd]", database.getNom());
        content = content.replace("[databaseHost]", credentials.getHost());
        content = content.replace("[databaseName]", credentials.getDatabaseName());
        content = content.replace("[databasePort]", database.getPort());
        content = content.replace("[databaseID]", String.valueOf(database.getId()));
        content = content.replace("[user]", credentials.getUser());
        content = content.replace("[pwd]", credentials.getPwd());
        content = content.replace("[databaseUseSSL]", String.valueOf(credentials.isUseSSL()));
        content = content.replace("[databaseAllowKey]", String.valueOf(credentials.isAllowPublicKeyRetrieval()));
        return content;
    }

    public String generateView(Entity entity, Login login) throws IOException, Exception {
        String content = "";
                        if(login.isAddLogin() && login.getNameTable().equals(HandyManUtils.minStart(entity.getTableName()))){
                            content = HandyManUtils.getFileContent(
                                Constantes.DATA_PATH + "/" + login.getViewLoginPrincipaletemplate() + "."
                                        + Constantes.SERVICE_EXTENSION);
                        }else{
                            content = HandyManUtils.getFileContent(
                                Constantes.DATA_PATH + "/" + getView().getViewContent() + "."
                                        + Constantes.SERVICE_EXTENSION);
                        }
        content = content.replace("[classNameMin]", HandyManUtils.minStart(entity.getClassName()));
        content = content.replace("[classNameMaj]", HandyManUtils.majStart(entity.getClassName()));
        
       
        return content;
    }

    public String genereteModalModif(Entity entity, Entity[] entites) throws Exception {
        String content = HandyManUtils.getFileContent(
                Constantes.DATA_PATH + "/" + getView().getViewModalModifContent() + "."
                        + Constantes.SERVICE_EXTENSION);
        content = content.replace("[ListInput]", genereteInputSAveComponent(entity, entites));
        return content;
    }

    private String genereteValueUpdate(Entity entity, Entity[] entites) throws Exception {
        String content = "";
        Entity foreign = null;
        String contentTemplate = "";
        int count = 0;
        for (EntityField field : entity.getFields()) {
            if (field.isForeign()) {
                foreign = Utility.searchEntity(entites, field.getType());
                content += "this." + HandyManUtils.minStart(field.getName()) + " = values."
                        + HandyManUtils.minStart(field.getName()) + "."
                        + HandyManUtils.minStart(foreign.getPrimaryField().getName());
            } else {
                content += "this." + HandyManUtils.minStart(field.getName()) + " = values."
                        + HandyManUtils.minStart(field.getName());
            }
            content += ";\n";
        }
        return content;
    }

    public String generateTableView(Entity entity, Entity[] entites) throws Exception {
        String content = HandyManUtils.getFileContent(
                Constantes.DATA_PATH + "/" + getView().getViewTableContent() + "."
                        + Constantes.SERVICE_EXTENSION);
                        
        String line = "";
        String imports = "";
        int compte = 0;
        for (EntityField field : entity.getFields()) {
            line += getView().getViewTableLine() + "\n";
            if (field.isForeign()) {
                line = line.replace("[fieldNameMin]", HandyManUtils.minStart(field.getName()) + "."
                        + Utility.getStringFirst(Utility.searchEntity(entites, field.getType())));
                line = line.replace("[Lable]", HandyManUtils.majStart(field.getName()));
                imports += getView().getViewImportService() + "\n";
                imports = imports.replace("[classNameMaj]",
                        HandyManUtils.majStart(Utility.searchEntity(entites, field.getType()).getClassName()));
            } else {
                line = line.replace("[fieldNameMin]", HandyManUtils.minStart(field.getName()));
                line = line.replace("[Lable]", HandyManUtils.majStart(field.getName()));
            }
            if (compte == 0) {
                imports += getView().getViewImportService() + "\n";
                imports = imports.replace("[classNameMaj]", HandyManUtils.majStart(entity.getClassName()));
                compte++;
            }
        }

        content = content.replace("[Line]", line);
        content = content.replace("[modalModif]", genereteModalModif(entity, entites));
        content = content.replace("[imports]", imports);
        content = content.replace("[classNameMaj]", HandyManUtils.majStart(entity.getClassName()));
        content = content.replace("[classNameMin]", HandyManUtils.minStart(entity.getClassName()));
        content = content.replace("[PrimaryKey]", HandyManUtils.minStart(entity.getPrimaryField().getName()));
        content = content.replace("[listAttribut]", genereteFieldSaveComponent(entity, entites));
        Entity foreign = null;
        int count = 0;
        String contentTemplate = "";
        for (EntityField field : entity.getFields()) {
            if (field.isForeign()) {
                foreign = Utility.searchEntity(entites, field.getType());
                contentTemplate += getView().getViewAppleForeignKey() + "\n";
                contentTemplate = contentTemplate.replace("[classNameMaj]",
                        HandyManUtils.majStart(foreign.getClassName()));
                contentTemplate = contentTemplate.replace("[classNameMin]",
                        HandyManUtils.minStart(foreign.getClassName()));
                count++;
            }
        }
        if (count == 0)
            content.replace("[ListCallForeignkey]", "");
        content = content.replace("[ListCallForeignkey]", contentTemplate);
        content = content.replace("[Listvalues]", genereteValueUpdate(entity, entites));
        content = content.replace("[Listvalue]", genereteListValueInsert(entity, entites));
       
        return content;
    }

    public String genereteImportExport(Entity entity, String content, String type) throws IOException {
        content += getView().getViewExportComponentImport() + "\n";
        ;
        content = content.replace("[importComponent]", (type + HandyManUtils.majStart(entity.getClassName())));
        return content;
    }

    public String genereteExportComponent(Entity entity, String content, String type) {
        content += (type + HandyManUtils.majStart(entity.getClassName())) + ", \n";
        return content;
    }

    public String genereteComponentExport(String importContenue, String export) throws IOException {
        String content = HandyManUtils.getFileContent(
                Constantes.DATA_PATH + "/" + getView().getViewExportComponentContent() + "."
                        + Constantes.SERVICE_EXTENSION);
        content = content.replace("[imports]", importContenue);
        content = content.replace("[exports]", export);
        return content;
    }

    public String generateService(Entity entity,
            String hostservice, Login login)
            throws IOException {
        String content = HandyManUtils.getFileContent(
                Constantes.DATA_PATH + "/" + getView().getService().getServiceContent() + "."
                        + Constantes.SERVICE_EXTENSION);
                        
        content = content.replace("[classNameMin]", HandyManUtils.minStart(entity.getClassName()));
        content = content.replace("[HostService]", hostservice);
        if(login.isAddLogin() && login.getNameTable().equals(HandyManUtils.minStart(entity.getTableName()))){
            content = content.replace("[ServiceLogin]", HandyManUtils.getFileContent(Constantes.DATA_PATH +"/"+login.getViewServiceContent() + "."
            + Constantes.SERVICE_EXTENSION ));
        }
        content = content.replace("[ServiceLogin]", " ");
         return content;
    }

    public String genereteChildrenRoute(Entity entity, String content, Login login) throws IOException {
        String newChildren = "";     
                        if(login.isAddLogin()){
                            if(login.getNameTable().equals(HandyManUtils.minStart(entity.getTableName()))){ 
                                newChildren =  HandyManUtils.getFileContent(
                                    Constantes.DATA_PATH + "/" + login.getViewRouteLogin() + "."
                                            + Constantes.SERVICE_EXTENSION);   
                            }else{
                                newChildren =  HandyManUtils.getFileContent(
                                    Constantes.DATA_PATH + "/" + getView().getRoutes().getRouteContentChildren() + "."
                                            + Constantes.SERVICE_EXTENSION);
                            }
                        }else{
                            newChildren =  HandyManUtils.getFileContent(
                                Constantes.DATA_PATH + "/" + getView().getRoutes().getRouteContentChildren() + "."
                                        + Constantes.SERVICE_EXTENSION);
                        }
                                           
        content += newChildren;

        content = content.replace("[classNameMin]", HandyManUtils.minStart(entity.getClassName()));
        content = content.replace("[classNameMaj]", HandyManUtils.majStart(entity.getClassName()));
         return content;
    }

    public String genereteImportRoutes(Entity entity, String content, Login login, int count) throws IOException {
        String newImport = HandyManUtils.getFileContent(
                Constantes.DATA_PATH + "/" + getView().getRoutes().getRouteContentImport() + "."
                        + Constantes.SERVICE_EXTENSION);
        content += newImport;
        if(login.isAddLogin() && count == 0){
            content += login.getViewImportLogout() +" \n";
        }
        content = content.replace("[classNameMaj]", HandyManUtils.majStart(entity.getClassName()));
        return content;
    }

    public String genererateRoute(String imports, String children, Entity entity) throws IOException {
        String content = HandyManUtils.getFileContent(
                Constantes.DATA_PATH + "/" + getView().getRoutes().getRouteContent() + "."
                        + Constantes.SERVICE_EXTENSION);
        content = content.replace("[children]", children);
        content = content.replace("[imports]", imports);
        content = content.replace("[classNameMin]", HandyManUtils.minStart(entity.getClassName()));
       
        return content;
    }

    public String genereteChildrenSidebar(Entity entity, String content, Login login) throws IOException {
        String newChildren = "";

                        if(login.isAddLogin()){
                            if(login.getNameTable().equals(HandyManUtils.minStart(entity.getTableName()))){  
                                newChildren =  HandyManUtils.getFileContent(
                                    Constantes.DATA_PATH + "/" + login.getViewSidebarContent() + "."
                                            + Constantes.SERVICE_EXTENSION);
                            }else{
                                newChildren =  HandyManUtils.getFileContent(
                                    Constantes.DATA_PATH + "/" + getView().getSidebar().getSidebarContentChildren() + "."
                                            + Constantes.SERVICE_EXTENSION);
                            }
                        }else{
                            newChildren =  HandyManUtils.getFileContent(
                                Constantes.DATA_PATH + "/" + getView().getSidebar().getSidebarContentChildren() + "."
                                        + Constantes.SERVICE_EXTENSION);
                        }
                            
        content += newChildren;


        content = content.replace("[classNameMin]", HandyManUtils.minStart(entity.getClassName()));
        content = content.replace("[classNameMaj]", HandyManUtils.majStart(entity.getClassName()));
        return content;
    }

    public String genereteSidebar(String content) throws IOException {
        content = (HandyManUtils.getFileContent(
                Constantes.DATA_PATH + "/" + getView().getSidebar().getSidebarContent() + "."
                        + Constantes.SERVICE_EXTENSION))
                .replace("[children]", content);
        return content;
    }

    private String genereteInputSAveComponent(Entity entity, Entity[] entities) throws Exception {
        String content = "";
        String template = "";
        String type = "";
        Entity foreign = null;
        String option = "";
        for (EntityField field : entity.getFields()) {
            if (field.isForeign() && !field.isPrimary()) {
                foreign = Utility.searchEntity(entities, field.getType());
                template = HandyManUtils.getFileContent(
                        Constantes.DATA_PATH + "/" + getView().getTemplateInsert().getListComponent() + "."
                                + Constantes.SERVICE_EXTENSION);
                option += getView().getViewListOption();
                option = option.replace("[primaryKey]", HandyManUtils.minStart(foreign.getPrimaryField().getName()));
                option = option.replace("[fieldMin]", HandyManUtils.minStart(Utility.getStringFirst(foreign)));
                template = template.replace("[ListOption]", option);
                content += template;
                content = content.replace("[Label]", HandyManUtils.majStart(field.getName()));
                content = content.replace("[fieldMin]", HandyManUtils.minStart(field.getName()));
                content = content.replace("[classNameMin]", HandyManUtils.minStart(foreign.getClassName()));
            } else if (!field.isPrimary()) {
                type = getView().getTemplateInsert().getTypes().get(field.getType());
                template = getView().getTemplateInsert().getTypesComponent().get(field.getType());
                if (template == null) {
                    type = "text";
                    template = getView().getTemplateInsert().getTypesComponent().get("String");
                }
                template = HandyManUtils.getFileContent(
                        Constantes.DATA_PATH + "/" + template + "."
                                + Constantes.SERVICE_EXTENSION);
                content += template;
                content = content.replace("[Label]", HandyManUtils.majStart(field.getName()));
                content = content.replace("[fieldMin]", HandyManUtils.minStart(field.getName()));
                content = content.replace("[type]", type);
            }
        }

         return content;

    }

    private String genereteCallForeignkey(Entity entity, Entity[] entities) throws Exception {
        String contentTemplate = HandyManUtils.getFileContent(
                Constantes.DATA_PATH + "/" + getView().getViewCallForeignkeyComponent() + "."
                        + Constantes.SERVICE_EXTENSION);
        String content = "";
        Entity foreign = null;
        int compte = 0;
        for (EntityField field : entity.getFields()) {
            if (field.isForeign()) {
                foreign = Utility.searchEntity(entities, field.getType());
                content += getView().getViewAppleForeignKey() + "\n";
                content = content.replace("[classNameMaj]", HandyManUtils.majStart(foreign.getClassName()));
                content = content.replace("[classNameMin]", HandyManUtils.minStart(foreign.getClassName()));
                compte++;
            }
        }
        if (compte == 0)
            return "";
        content = contentTemplate.replace("[ListCallForeignkey]", content);
        return content;
    }

    private String genereteSaveImport(Entity entity, Entity[] entities) throws Exception {
        String content = getView().getViewImportService() + "\n";
        content = content.replace("[classNameMaj]", HandyManUtils.majStart(entity.getClassName()));
        Entity foreign = null;
        for (EntityField field : entity.getFields()) {
            if (field.isForeign()) {
                content += getView().getViewImportService();
                foreign = Utility.searchEntity(entities, field.getType());
                content = content.replace("[classNameMaj]", HandyManUtils.majStart(foreign.getClassName()));
            }
        }
        return content;
    }

    private String genereteFieldSaveComponent(Entity entity, Entity[] entities) throws Exception {
        String content = "";
        Entity foreign = null;
        for (EntityField field : entity.getFields()) {
            if (field.isForeign()) {
                foreign = Utility.searchEntity(entities, field.getType());
                content += HandyManUtils.minStart(foreign.getClassName()) + "Foreignkey: [],\n";
            }
            content += HandyManUtils.minStart(field.getName()) + ": null ,\n";
        }
        return content;
    }

    private String genereteListValueInsert(Entity entity, Entity[] entities) throws Exception {
        String content = "";
        Entity foreign = null;
        int count = 0;
        for (EntityField field : entity.getFields()) {
            if (field.isForeign() && !field.isPrimary()) {
                foreign = Utility.searchEntity(entities, field.getType());
                content += HandyManUtils.minStart(field.getName()) + ": { \n"
                        + "\t" + HandyManUtils.minStart(foreign.getPrimaryField().getName()) + " : this."
                        + HandyManUtils.minStart(field.getName()) + " \n }";
            } else if (!field.isPrimary()) {
                content += HandyManUtils.minStart(field.getName()) + " : this."
                        + HandyManUtils.minStart(field.getName());
            }
            if (count == entity.getFields().length - 1) {
                content += "\n";
            } else if (count != 0) {
                content += ",\n";
            }
            count++;
        }
        return content;
    }

    public String genereteSaveComponent(Entity entity, Entity[] entities, Login login) throws Exception {
        String content = HandyManUtils.getFileContent(
                Constantes.DATA_PATH + "/" + getView().getViewSaveComponent() + "."
                        + Constantes.SERVICE_EXTENSION);
        if(login.isAddLogin() && login.getNameTable().equals(HandyManUtils.minStart(entity.getTableName()))){
            content = login.genereteSaveLogin();
            
        }                 
        content = content.replace("[ListInput]", genereteInputSAveComponent(entity, entities));
        content = content.replace("[foreignkey]", genereteCallForeignkey(entity, entities));
        content = content.replace("[imports]", genereteSaveImport(entity, entities));
        content = content.replace("[listAttribut]", genereteFieldSaveComponent(entity, entities));
        content = content.replace("[Listvalue]", genereteListValueInsert(entity, entities));

        content = content.replace("[classNameMin]", HandyManUtils.minStart(entity.getClassName()));
        content = content.replace("[classNameMaj]", HandyManUtils.majStart(entity.getClassName()));
        if(login.isAddLogin()){
            content = content.replace("[contrainte]", login.genereteContrainte());
        }else{
            content = content.replace("[contrainte]", " ");
        }
        return content;
    }


    
    
    

}
