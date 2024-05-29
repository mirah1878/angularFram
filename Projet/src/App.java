import java.io.File;
import java.sql.Connection;
import java.util.Scanner;

import genesis.Constantes;
import genesis.Credentials;
import genesis.CustomChanges;
import genesis.CustomFile;
import genesis.Database;
import genesis.Entity;
import genesis.EntityField;
import genesis.Language;
import genesis.Login;
import handyman.HandyManUtils;

public class App {
        public static void main(String[] args) throws Exception {
                Database[] databases = HandyManUtils.fromJson(Database[].class,
                                HandyManUtils.getFileContent(Constantes.DATABASE_JSON));
                Language[] languages = HandyManUtils.fromJson(Language[].class,
                                HandyManUtils.getFileContent(Constantes.LANGUAGE_JSON));
                                
                Login login = HandyManUtils.fromJson(Login.class,
                                HandyManUtils.getFileContent(Constantes.LOGIN_TABLE_JSON));
                
                Database database;
                Language language;
                String databaseName, user, pwd, host;
                boolean useSSL, allowPublicKeyRetrieval;
                String projectName, entityName;
                Credentials credentials;
                String projectNameTagPath, projectNameTagContent;
                File project, credentialFile;
                String customFilePath, customFileContentOuter;
                Entity[] entities;
                String[] models, controllers;
                String modelFile, controllerFile, customFile;
                String customFileContent;
                String foreignContext;
                String customChanges, changesFile;
                String navLink, navLinkPath;
                /**** Login   ****/
                boolean checkLogin = false;
                try (Scanner scanner = new Scanner(System.in)) {
                        System.out.println("Choix de la base de donnée:");
                        for(int i=0;i<databases.length;i++){
                                System.out.println((i+1)+") "+databases[i].getNom());
                        } 
                        //System.out.print("> ");
                        database=databases[scanner.nextInt()-1];
                        database = databases[0];
                        System.out.println("Choix framework");
                        for(int i=0;i<languages.length;i++){
                                System.out.println((i+1)+") "+languages[i].getNom());
                        }
                        System.out.print("> ");
                        language=languages[scanner.nextInt()-1];
                        
                        language = languages[0];
                        System.out.println("Detail de la base de donnée :");
                        System.out.print("Database name: ");
                        databaseName=scanner.next();
                        //databaseName = "daotest";
                        System.out.print("Username: ");
                        user = scanner.next();
                        //user = "postgres";
                        System.out.print("Password: ");
                        pwd = scanner.next();
                        //pwd = "Hasinjo2";
                        System.out.print("Database host: ");
                        //host = scanner.next();
                        host = "localhost";
                        //System.out.print("Use SSL ?(Y/n): ");
                        // useSSL = scanner.next().equalsIgnoreCase("Y");
                        useSSL = true;
                        //System.out.print("Allow public key retrieval ?(Y/n): ");
                        // allowPublicKeyRetrieval = scanner.next().equalsIgnoreCase("Y");
                        allowPublicKeyRetrieval = false;
                        System.out.println();
                        System.out.print("Enter your project name: ");
                        projectName = scanner.next();
                        //projectName = "daotest";
                        System.out.print("Ajouter de login  (Y/n): ");
                        checkLogin = scanner.next().equalsIgnoreCase("Y");
                        System.out.println(checkLogin);
                        
                        //System.out.print("Which entities to import ?(* to select all): ");
                        // entityName = scanner.next();
                        entityName = "*";
                        credentials = new Credentials(databaseName, user, pwd, host, useSSL, allowPublicKeyRetrieval);
                        project = new File(projectName);
                        project.mkdir();
                        for (CustomFile c : language.getAdditionnalFiles()) {
                                customFilePath = c.getName();
                                customFilePath = customFilePath.replace("[projectNameMaj]",
                                                HandyManUtils.majStart(projectName));
                                HandyManUtils.createFile(customFilePath);
                                customFileContentOuter = HandyManUtils
                                                .getFileContent(Constantes.DATA_PATH + "/" + c.getContent())
                                                .replace("[projectNameMaj]", HandyManUtils.majStart(projectName));
                                HandyManUtils.overwriteFileContent(customFilePath, customFileContentOuter);
                        }
                        HandyManUtils.extractDir(
                                        Constantes.DATA_PATH + "/" + language.getSkeleton() + "."
                                                        + Constantes.SKELETON_EXTENSION,
                                        project.getPath());
                        credentialFile = new File(project.getPath(), Constantes.CREDENTIAL_FILE);
                        credentialFile.createNewFile();
                        HandyManUtils.overwriteFileContent(credentialFile.getPath(), HandyManUtils.toJson(credentials));
                        for (String replace : language.getProjectNameTags()) {
                                projectNameTagPath = replace
                                                .replace("[projectNameMaj]", HandyManUtils.majStart(projectName))
                                                .replace("[projectNameMin]", HandyManUtils.minStart(projectName));
                                projectNameTagContent = HandyManUtils.getFileContent(projectNameTagPath).replace(
                                                "[projectNameMaj]",
                                                HandyManUtils.majStart(projectName));
                                projectNameTagContent = projectNameTagContent.replace("[databaseHost]",
                                                credentials.getHost());
                                projectNameTagContent = projectNameTagContent.replace("[databasePort]",
                                                database.getPort());
                                projectNameTagContent = projectNameTagContent.replace("[databaseName]",
                                                credentials.getDatabaseName());
                                projectNameTagContent = projectNameTagContent.replace("[user]", credentials.getUser());
                                projectNameTagContent = projectNameTagContent.replace("[pwd]", credentials.getPwd());
                                projectNameTagContent = projectNameTagContent.replace("[projectNameMin]",
                                                HandyManUtils.minStart(projectName));
                                HandyManUtils.overwriteFileContent(projectNameTagPath, projectNameTagContent);
                        }
                        try (Connection connect = database.getConnexion(credentials)) {
                                if(checkLogin){
                                       login.ExecuteScript(connect);
                                }
                                entities = database.getEntities(connect, credentials, entityName);
                                for (int i = 0; i < entities.length; i++) {
                                        entities[i].initialize(connect, credentials, database, language);
                                }
                                models = new String[entities.length];
                                controllers = new String[entities.length];
                                navLink = "";
                                for (int i = 0; i < models.length; i++) {
                                        models[i] = language.generateModel(entities[i], projectName);
                                        controllers[i] = language.generateController(entities[i], database, credentials,
                                                        projectName, login);
                                        
                                        modelFile = language.getModel().getModelSavePath().replace("[projectNameMaj]",
                                                        HandyManUtils.majStart(projectName));
                                        controllerFile = language.getController().getControllerSavepath().replace(
                                                        "[projectNameMaj]",
                                                        HandyManUtils.majStart(projectName));
                                        modelFile = modelFile.replace("[projectNameMin]",
                                                        HandyManUtils.minStart(projectName));
                                        controllerFile = controllerFile.replace("[projectNameMin]",
                                                        HandyManUtils.minStart(projectName));
                                        modelFile += "/" + HandyManUtils.majStart(entities[i].getClassName()) + "."
                                                        + language.getModel().getModelExtension();
                                        controllerFile += "/" + HandyManUtils.majStart(entities[i].getClassName())
                                                        + language.getController().getControllerNameSuffix() + "."
                                                        + language.getController().getControllerExtension();
                                        HandyManUtils.createFile(modelFile);
                                        for (CustomFile f : language.getModel().getModelAdditionnalFiles()) {
                                                foreignContext = "";
                                                for (EntityField ef : entities[i].getFields()) {
                                                        if (ef.isForeign()) {
                                                                foreignContext += language.getModel()
                                                                                .getModelForeignContextAttr();
                                                                foreignContext = foreignContext.replace(
                                                                                "[classNameMaj]",
                                                                                HandyManUtils.majStart(ef.getType()));
                                                        }
                                                }
                                                customFile = f.getName().replace("[classNameMaj]",
                                                                HandyManUtils.majStart(entities[i].getClassName()));
                                                customFile = language.getModel().getModelSavePath().replace(
                                                                "[projectNameMaj]",
                                                                HandyManUtils.majStart(projectName)) + "/" + customFile;
                                                customFile = customFile.replace("[projectNameMin]",
                                                                HandyManUtils.minStart(projectName));
                                                customFileContent = HandyManUtils
                                                                .getFileContent(Constantes.DATA_PATH + "/"
                                                                                + f.getContent())
                                                                .replace("[classNameMaj]", HandyManUtils
                                                                                .majStart(entities[i].getClassName()));
                                                customFileContent = customFileContent.replace("[projectNameMin]",
                                                                HandyManUtils.minStart(projectName));
                                                customFileContent = customFileContent.replace("[projectNameMaj]",
                                                                HandyManUtils.majStart(projectName));
                                                customFileContent = customFileContent.replace("[databaseHost]",
                                                                credentials.getHost());
                                                customFileContent = customFileContent.replace("[databaseName]",
                                                                credentials.getDatabaseName());
                                                customFileContent = customFileContent.replace("[user]",
                                                                credentials.getUser());
                                                customFileContent = customFileContent.replace("[pwd]",
                                                                credentials.getPwd());
                                                customFileContent = customFileContent
                                                                .replace("[modelForeignContextAttr]", foreignContext);HandyManUtils.createFile(customFile);
                                                HandyManUtils.overwriteFileContent(customFile, customFileContent);
                                        }
                                        HandyManUtils.createFile(controllerFile);
                                        HandyManUtils.overwriteFileContent(modelFile, models[i]);
                                        HandyManUtils.overwriteFileContent(controllerFile, controllers[i]);
                                        // HandyManUtils.overwriteFileContent(viewFile, views[i]);
                                        navLink += language.getNavbarLinks().getLink();
                                        navLink = navLink.replace("[projectNameMaj]",
                                                        HandyManUtils.majStart(projectName));
                                        navLink = navLink.replace("[projectNameMin]",
                                                        HandyManUtils.minStart(projectName));
                                        navLink = navLink.replace("[classNameMin]",
                                                        HandyManUtils.minStart(entities[i].getClassName()));
                                        navLink = navLink.replace("[classNameMaj]",
                                                        HandyManUtils.majStart(entities[i].getClassName()));
                                        navLink = navLink.replace("[classNameformattedMin]",
                                                        HandyManUtils.minStart(HandyManUtils
                                                                        .formatReadable(entities[i].getClassName())));
                                        navLink = navLink.replace("[classNameformattedMaj]",
                                                        HandyManUtils.majStart(HandyManUtils
                                                                        .formatReadable(entities[i].getClassName())));
                                }
                                navLinkPath = language.getNavbarLinks().getPath().replace("[projectNameMaj]",
                                                HandyManUtils.majStart(projectName));
                                navLinkPath = navLinkPath.replace("[projectNameMin]",
                                                HandyManUtils.minStart(projectName));
                                System.out.println(navLinkPath);
                                HandyManUtils.overwriteFileContent(navLinkPath,
                                                HandyManUtils.getFileContent(navLinkPath).replace("[navbarLinks]",
                                                                navLink));
                                for (CustomChanges c : language.getCustomChanges()) {
                                        customChanges = "";
                                        for (Entity e : entities) {
                                                customChanges += c.getChanges();
                                                customChanges = customChanges.replace("[classNameMaj]",
                                                                HandyManUtils.majStart(e.getClassName()));
                                                customChanges = customChanges.replace("[classNameMin]",
                                                                HandyManUtils.minStart(e.getClassName()));
                                                customChanges = customChanges.replace("[databaseHost]",
                                                                credentials.getHost());
                                                customChanges = customChanges.replace("[user]", credentials.getUser());
                                                customChanges = customChanges.replace("[databaseName]",
                                                                credentials.getDatabaseName());
                                                customChanges = customChanges.replace("[pwd]", credentials.getPwd());
                                        }
                                        if (c.isWithEndComma() == false) {
                                                customChanges = customChanges.substring(0, customChanges.length() - 1);
                                        }
                                        changesFile = c.getPath().replace("[projectNameMaj]",
                                                        HandyManUtils.majStart(projectName));
                                        HandyManUtils.overwriteFileContent(changesFile,
                                                        HandyManUtils.getFileContent(changesFile)
                                                                        .replace("[customChanges]", customChanges));
                                }
                        }
                }
        }
}
