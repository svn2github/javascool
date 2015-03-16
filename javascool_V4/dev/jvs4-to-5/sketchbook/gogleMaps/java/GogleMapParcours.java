/*******************************************************************************
* David.Pichardie@inria.fr, Copyright (C) 2011.           All rights reserved. *
*******************************************************************************/

package org.javascool.proglets.gogleMaps;

import static org.javascool.proglets.gogleMaps.Functions.latitudes;
import static org.javascool.proglets.gogleMaps.Functions.longitudes;

import java.util.Set;
import java.util.HashSet;
import java.util.Queue;
import java.util.LinkedList;
import org.javascool.macros.Macros;

class GogleMapParcours {
  private static int numVisite;

  private static void afficheVille(GogleMapPanel g, String s) {
    double latitude = latitudes.get(s);
    double longitude = longitudes.get(s);
    g.affichePoint(longitude, latitude);
  }
  // affiche une ville de nom s en indiquant le num�ro num
  static void afficheVilleAvecNumero(GogleMapPanel g, String s, int num) {
    double latitude = latitudes.get(s);
    double longitude = longitudes.get(s);
    g.affichePoint(longitude, latitude, num);
  }
  static void afficheRouteDirecte(GogleMapPanel g, String ville1, String ville2) {
    g.afficheRoute(longitudes.get(ville1),
                   latitudes.get(ville1),
                   longitudes.get(ville2),
                   latitudes.get(ville2),
                   1);
  }
  // Affiche toutes les routes directes
  static void afficheToutesRoutesDirectes(GogleMapPanel g) {
    for(String depart : g.arcs.keySet())
      for(String arrivee : g.arcs.get(depart))
        if(depart.compareTo(arrivee) > 0) {
          afficheRouteDirecte(g, depart, arrivee);
        }
  }
  static void parcoursRec(GogleMapPanel g, Set<String> vu, String ville1) {
    // Macros.sleep(500);
    vu.add(ville1);
    afficheVilleAvecNumero(g, ville1, numVisite++);
    for(String ville2 : g.arcs.get(ville1))
      afficheVille(g, ville2);
    Macros.sleep(500);
    for(String ville2 : g.arcs.get(ville1))
      if(!vu.contains(ville2)) {
        parcoursRec(g, vu, ville2);
      }
  }
  static void parcoursProfondeur(GogleMapPanel g, String depart) {
    Set<String> vu = new HashSet<String>();
    numVisite = 1;
    parcoursRec(g, vu, depart);
  }
  static void parcoursLargeur(GogleMapPanel g, String depart) {
    Set<String> vu = new HashSet<String>();
    Queue<String> aVoir = new LinkedList<String>();
    aVoir.offer(depart);
    int countVisite = 1;
    while(!aVoir.isEmpty()) {
      String ville1 = aVoir.remove();
      vu.add(ville1);
      afficheVilleAvecNumero(g, ville1, countVisite++);
      Macros.sleep(500);
      for(String ville2 : g.arcs.get(ville1))
        if(!vu.contains(ville2)) {
          afficheVille(g, ville2);
          if(!aVoir.contains(ville2)) {
            aVoir.offer(ville2);
          }
        }
    }
  }
}

