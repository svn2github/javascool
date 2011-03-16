/*******************************************************************************
 * David.Pichardie@inria.fr, Copyright (C) 2011.           All rights reserved. *
 *******************************************************************************/

package proglet.goglemap;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import org.javascool.Macros;


public class GogleMapCalculChemins {

	private static int distance(GogleMapPanel g,String ville1, String ville2) { 
		if (g.arcs.get(ville1).contains(ville2)) {  
			Macros.assertion(g.latitudes.containsKey(ville1),ville1 + " n'est pas une ville connue");
			Macros.assertion(g.latitudes.containsKey(ville2),ville2 + " n'est pas une ville connue");
			return g.distanceEuclidienne(g.longitudes.get(ville1), g.latitudes.get(ville1),  
					g.longitudes.get(ville2), g.latitudes.get(ville2)); 
		} else return Integer.MAX_VALUE;      
	}	

	private static String PlusProche(List<String> groupe, Map<String,Integer> distMap) {       
		String res = null;      
		int distMin = Integer.MAX_VALUE;  
		for (String ville: groupe) {      
			int distance = distMap.get(ville);       
			if (distance < distMin) {           
				distMin = distance;           
				res = ville;           
			}    
		}
		return res; 
	}


	private static void MiseAjourDistance(GogleMapPanel g, String ville0, Map<String,Integer> distMap,  Map<String,String> pred) {    
		int distance_ville0 = distMap.get(ville0);         
		for (String ville: g.arcs.get(ville0)) {   
			int nouvelle_distance = distance_ville0 + distance(g,ville0,ville); 
			if (nouvelle_distance < distMap.get(ville)) {             
				distMap.put(ville,nouvelle_distance);            
				pred.put(ville,ville0);         
			}  
		}      
	}           

	static List<String> plusCourtChemin(GogleMapPanel g, String depart, String arrivee) {     
		Map<String,Integer> distanceAuDepart = new HashMap<String,Integer>(); 
		List<String> aTraite = new ArrayList<String>(g.latitudes.keySet()); 
		Map<String,String> predecesseur = new HashMap<String,String>(); 
		int nb_ville = aTraite.size();             
		for (String ville: aTraite) {         
			if (ville.equals(depart)) distanceAuDepart.put(ville,0);  
			else if (g.arcs.get(ville).contains(depart)) {           
				distanceAuDepart.put(ville,distance(g,ville,depart));   
				predecesseur.put(ville,depart);          
			} else            
				distanceAuDepart.put(ville,Integer.MAX_VALUE);         
		}         aTraite.remove(depart);       
		//System.out.println("dist = "+distanceAuDepart);  
		for (int i=1; i<nb_ville; i++) {           
			String prochain = PlusProche(aTraite,distanceAuDepart);        
			MiseAjourDistance(g,prochain,distanceAuDepart,predecesseur);   
			aTraite.remove(prochain);    
			//System.out.println("prochain = "+prochain+" dist = "+distanceAuDepart);   
		}
		// construction du plus court chemin 
		List<String> chemin = new ArrayList<String>();   
		String finDuChemin = arrivee;        
		while (!finDuChemin.equals(depart)) {
			Macros.sleep(0);    
			chemin.add(0,finDuChemin);     
			finDuChemin = predecesseur.get(finDuChemin);   
		}    
		chemin.add(0,depart);  
		return chemin;    
	}      
}

