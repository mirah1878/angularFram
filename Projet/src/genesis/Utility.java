package genesis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Utility {
    public static void createFile(String path) throws Exception {
        File file = new File(path);
        try {
            boolean success = file.createNewFile();
            if (success) {
                System.out.println("Fichier UtilisateursService.js créé avec succès.");
            } else {
                System.out.println("Le fichier UtilisateursService.js existe déjà.");
            }
        } catch (IOException e) {
            throw new Exception("Erreur lors de la création du fichier : " + e.getMessage());
        }
    }

    public static void writeFile(String path, String content) throws Exception {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(content);
            writer.close();
            System.out.println("Texte écrit avec succès dans le fichier UtilisateursService.js.");
        } catch (IOException e) {
            throw new Exception("Erreur lors de l'écriture dans le fichier : " + e.getMessage());
        }
    }

    public static Entity searchEntity(Entity[] entities, String className) throws Exception {
        Entity entity = null;
        for (Entity ent : entities) {
            if (ent.getClassName().equals(className)) {
                entity = ent;
                break;
            }
        }
        if (entity == null)
            throw new Exception("table " + className + " non trouvé");
        return entity;
    }

    public static String getStringFirst(Entity entity) {
        for (EntityColumn column : entity.getColumns()) {
            if (column.getType().equals("character varying"))
                return column.getName();
        }
        return "";
    }
}
