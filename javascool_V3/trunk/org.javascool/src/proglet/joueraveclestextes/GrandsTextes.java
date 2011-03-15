package proglet.joueraveclestextes;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collections;
import java.util.Comparator;
import java.io.BufferedReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class GrandsTextes implements org.javascool.Proglet {
  private  GrandsTextes () {}



  private final static int lignesParPages = 25;
  private final static String changementDePage = "<NOUVELLE PAGE>";
  
  private static void filtre(String filename) {
    if (!filename.equals("La_Marseillaise.txt") &&
        !filename.equals("Les_Miserables.txt")) 
      throw new RuntimeException("Le texte "+filename+" est inconnue !");
  }
        
  
  public static List<String> lireGrandTexte(String filename) {
    filtre(filename);
    String encoding = "UTF8";
    String line = null;
    List<String> records = new ArrayList<String>();
    int compteur = 0;
    try {
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("proglet/joueraveclestextes/doc-files/"+filename), encoding));
      while ((line = bufferedReader . readLine()) != null) {
        compteur++;
        String[] words = line . split("[[\\s+]\\p{Punct}]");
        for (String word : words) if ( ! word . equals("")) records . add(word . toLowerCase());
        //records . add("\n");
        if (compteur>=lignesParPages) {
          records.add(changementDePage);
          compteur =0; 
        }
      }
      bufferedReader . close();
    }
    catch(FileNotFoundException e) {
      System . out . println("Erreur : fichier " + filename + " introuvable");
    }
    catch(IOException e) {
      System . out . println("Erreur durant la lecture du fichier " + filename);
    }
    ;
    return records;
  }

    
  public static Map<String,Integer> trierMapParValeursDecroissantes(Map<String,Integer> table) {
    List<Map.Entry<String,Integer>> entries = new ArrayList<Map.Entry<String,Integer>>(table.entrySet());
    Collections.sort(entries, new Comparator<Map.Entry<String,Integer>>() {
      public int compare(Map.Entry<String,Integer> o1, Map.Entry<String,Integer> o2) {
        return o2.getValue() - o1.getValue();
      }});   
    Map<String,Integer> res = new LinkedHashMap<String,Integer>();
    for (Map.Entry<String,Integer> e: entries)
      res.put(e.getKey(),e.getValue());
    return res;
  }
  
  
}




