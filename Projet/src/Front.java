import java.io.File;
import java.sql.Connection;
import java.util.Scanner;

import genesis.Constantes;
import genesis.Credentials;
import genesis.Database;
import genesis.Entity;
import genesis.Language;
import genesis.Login;
import genesis.Utility;
import handyman.HandyManUtils;

public class Front {
        public static void main(String[] args) throws Exception {
                Database[] databases = HandyManUtils.fromJson(Database[].class,
                                HandyManUtils.getFileContent(Constantes.DATABASE_JSON));
                Language[] languages = HandyManUtils.fromJson(Language[].class,
                                HandyManUtils.getFileContent(Constantes.LANGUAGE_JSON));
                  Login login = HandyManUtils.fromJson(Login.class,
                                HandyManUtils.getFileContent(Constantes.LOGIN_TABLE_JSON));
              
                                Database database;
                Language language;
                String databaseName, user, pwd, host, hostservice;
                boolean useSSL, allowPublicKeyRetrieval;
                String projectName, entityName;
                Credentials credentials;
                File project, credentialFile;
                Entity[] entities;
                String[] views, services;
                String viewFile, serviceFile, routeFile, sidebarFile, exportFile;
                String routes, importRoutes, sidebar;
                String importComponent, exportComponent;
                 /**** Login   ****/
                 boolean checkLogin = false;
                try (Scanner scanner = new Scanner(System.in)) {
                        System.out.println("Choose a database engine:");
                        /*
                         * for(int i=0;i<databases.length;i++){
                         * System.out.println((i+1)+") "+databases[i].getNom());
                         * }
                         */
                        // System.out.print("> ");
                        // database=databases[scanner.nextInt()-1];
                        database = databases[0];
                        /*
                         * System.out.println("Choose a framework:");
                         * for(int i=0;i<languages.length;i++){
                         * System.out.println((i+1)+") "+languages[i].getNom());
                         * }
                         * System.out.print("> ");
                         * language=languages[scanner.nextInt()-1];
                         */
                        System.out.print("Ajouter de login  (Y/n): ");
                        checkLogin = scanner.next().equalsIgnoreCase("Y");
                        System.out.println(checkLogin);
                        
                        System.out.print("HostService :");
                        System.out.println();
                        hostservice = "http://localhost:8082/u";
                        language = languages[0];
                        System.out.println("Enter your database credentials:");
                        System.out.print("Database name: ");
                        // databaseName=scanner.next();
                        databaseName = "daotest";
                        System.out.print("Username: ");
                        // user = scanner.next();
                        user = "postgres";
                        System.out.print("Password: ");
                        // pwd = scanner.next();
                        pwd = "Hasinjo2";
                        System.out.print("Database host: ");
                        // host = scanner.next();
                        host = "localhost";
                        System.out.print("Use SSL ?(Y/n): ");
                        // useSSL = scanner.next().equalsIgnoreCase("Y");
                        useSSL = true;
                        System.out.print("Allow public key retrieval ?(Y/n): ");
                        // allowPublicKeyRetrieval = scanner.next().equalsIgnoreCase("Y");
                        allowPublicKeyRetrieval = false;
                        System.out.println();
                        System.out.print("Enter your project name: ");
                        // projectName = scanner.next();
                        projectName = "VueJSTEST";
                        System.out.print("Which entities to import ?(* to select all): ");
                        // entityName = scanner.next();
                        entityName = "*";
                        credentials = new Credentials(databaseName, user, pwd, host, useSSL, allowPublicKeyRetrieval);
                        project = new File(projectName);
                        project.mkdir();

                        HandyManUtils.extractDir(
                                        Constantes.DATA_PATH + "/" + Constantes.VUEJS_SKELETON + "."
                                                        + Constantes.VUEJS_EXTENSION,
                                        project.getPath());

                        credentialFile = new File(project.getPath(), Constantes.CREDENTIAL_FILE);
                        credentialFile.createNewFile();
                        HandyManUtils.overwriteFileContent(credentialFile.getPath(),
                                        HandyManUtils.toJson(credentials));
                                        String logout = "";

                        try (Connection connect = database.getConnexion(credentials)) {
                                entities = database.getEntities(connect, credentials, entityName);
                                for (int i = 0; i < entities.length; i++) {
                                        entities[i].initialize(connect, credentials, database, language);
                                }
                                views = new String[entities.length];
                                services = new String[entities.length];
                                routes = "";
                                importRoutes = "";
                                sidebar = "";
                                importComponent = "";
                                exportComponent = "";
                                for (int i = 0; i < views.length; i++) {
                                        /**** service ****/
                                        services[i] = language.generateService(entities[i], hostservice, login);
                                        serviceFile = language.getView().getService().getServiceSavePath().replace(
                                                        "[projectNameMaj]",
                                                        HandyManUtils.majStart(projectName));
                                        serviceFile = serviceFile + "/"
                                                        + language.getView().getService().getServiceName() + "."
                                                        + language.getView().getService().getServiceExtension();
                                        serviceFile = serviceFile.replace("[classNameMaj]",
                                                        HandyManUtils.majStart(entities[i].getClassName()));
                                        Utility.createFile(serviceFile);
                                        Utility.writeFile(serviceFile, services[i]);
                                        /***** Route ****/
                                        routes = language.genereteChildrenRoute(entities[i], routes, login);
                                        importRoutes = language.genereteImportRoutes(entities[i], importRoutes, login, i);
                                        sidebar = language.genereteChildrenSidebar(entities[i], sidebar, login);

                                        /****** vue *****/
                                        views[i] = language.generateView(entities[i], login);
                                        viewFile = language.getView().getViewSavePath().replace(
                                                        "[projectNameMaj]",
                                                        HandyManUtils.majStart(projectName));
                                        viewFile += "/" + language.getView().getViewName() + "."
                                                        + language.getView().getViewExtension();
                                        viewFile = viewFile.replace("[classNameMaj]",
                                                        HandyManUtils.majStart(entities[i].getClassName()));
                                        Utility.createFile(viewFile);
                                        Utility.writeFile(viewFile, views[i]);
                                        viewFile = "";
                                        /**** Creation de logout *** */
                                        if(login.isAddLogin()  && login.getNameTable().equals(HandyManUtils.minStart(entities[i].getTableName()))){
                                                logout = login.genereteLogout(entities[i]);
                                        }

                                        /***** Creation du table ****/
                                        if(!login.getNameTable().equals(HandyManUtils.minStart(entities[i].getTableName()))){
                                                views[i] = language.generateTableView(entities[i], entities);
                                                if(login.isAddLogin()){
                                                        views[i] = views[i].replace("[contrainte]", login.genereteContrainte());
                                                }else{
                                                        views[i] = views[i].replace("[contrainte]", " ");
                                                }
                                                viewFile = language.getView().getViewComponentSavePath().replace(
                                                                "[projectNameMaj]",
                                                                HandyManUtils.majStart(projectName));
                                                viewFile += "/" + language.getView().getTableName() + "."
                                                                + language.getView().getViewExtension();
                                                viewFile = viewFile.replace("[classNameMaj]",
                                                                HandyManUtils.majStart(entities[i].getClassName()));
                                                Utility.createFile(viewFile);
                                                Utility.writeFile(viewFile, views[i]);
                                        }
                                        viewFile = "";

                                        /**** Creation de exportation des component ****/
                                        if(!login.getNameTable().equals(HandyManUtils.minStart(entities[i].getTableName()))){
                                                importComponent = language.genereteImportExport(entities[i], importComponent,
                                                "Table");
                                                exportComponent = language.genereteExportComponent(entities[i], exportComponent,
                                                "Table");   
                                        }
                                                    
                                        importComponent = language.genereteImportExport(entities[i], importComponent,
                                                        "Save");
                                        
                                        exportComponent = language.genereteExportComponent(entities[i],
                                                        exportComponent,
                                                        "Save");

                                        /**** Save Component ***/
                                        views[i] = language.genereteSaveComponent(entities[i], entities, login);
                                        viewFile = language.getView().getViewComponentSavePath().replace(
                                                        "[projectNameMaj]",
                                                        HandyManUtils.majStart(projectName));
                                        viewFile += "/" + language.getView().getSaveName() + "."
                                                        + language.getView().getViewExtension();
                                        viewFile = viewFile.replace("[classNameMaj]",
                                                        HandyManUtils.majStart(entities[i].getClassName()));
                                        Utility.createFile(viewFile);
                                        Utility.writeFile(viewFile, views[i]);
                                        viewFile = "";

                                }
                                
                                if(login.isAddLogin() ){
                                        viewFile = language.getView().getViewSavePath().replace(
                                                        "[projectNameMaj]",
                                                        HandyManUtils.majStart(projectName));
                                        viewFile += "/" + language.getView().getViewName() + "."
                                                        + language.getView().getViewExtension();
                                        viewFile = viewFile.replace("[classNameMaj]",
                                                        HandyManUtils.majStart("Logout"));
                                        Utility.createFile(viewFile);
                                        Utility.writeFile(viewFile, logout);
                                        viewFile = "";
                                }

                                routes = language.genererateRoute(importRoutes, routes, entities[0]);
                                routeFile = language.getView().getRoutes().getRouteSavePath().replace(
                                                "[projectNameMaj]",
                                                HandyManUtils.majStart(projectName));
                                routeFile = routeFile + "/" + language.getView().getRoutes().getRoutesName() + "."
                                                + language.getView().getRoutes().getRouteExtension();
                                Utility.createFile(routeFile);
                                Utility.writeFile(routeFile, routes);
                                /***** Sidebar *****/
                                sidebar = language.genereteSidebar(sidebar);
                                sidebarFile = language.getView().getSidebar().getSidebarSavePath().replace(
                                                "[projectNameMaj]",
                                                HandyManUtils.majStart(projectName));
                                sidebarFile = sidebarFile + "/" + language.getView().getSidebar().getSidebarName() + "."
                                                + language.getView().getSidebar().getSidebarExtension();
                                Utility.createFile(sidebarFile);
                                Utility.writeFile(sidebarFile, sidebar);
                                /***** Generete ExportCompoenent ****/
                               
                                exportComponent = language.genereteComponentExport(importComponent, exportComponent);
                                System.out.println();
                                System.out.println(exportComponent);
                                exportFile = language.getView().getViewExportComponentSavePath().replace(
                                                "[projectNameMaj]",
                                                HandyManUtils.majStart(projectName));
                                exportFile += "/" + language.getView().getViewExportComponentName() + "."
                                                + language.getView().getViewExportComponentExtension();
                                Utility.createFile(exportFile);
                                Utility.writeFile(exportFile, exportComponent);
                        }

                }
        }
}
