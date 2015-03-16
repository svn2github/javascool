/*******************************************************************************
* Christophe.Beasse@ac-rennes.fr, Copyright (C) 2011.  All rights reserved.    *
*******************************************************************************/

package org.javascool.proglets.detectionForme;

import static org.javascool.proglets.detectionForme.Functions.*;
import static org.javascool.macros.Macros.*;
import java.awt.Color;

/** Démonstration de la proglet. 
 * @see <a href="Demo.java.html">code source</a>
 */
public class Demo {
  /** Lance la démo de la proglet. */
  public static void start() {

    try {  
      createImage(1,300,300);
      drawFillRect(1, 100, 100, 100, 100 , Color.black);
      rotateImage(1,45);
      showImage(1);   
      sleep(500);   
      copyImage(1,2);
      cutImage(2,75,75,150,150);
      showImage(2);   
      sleep(500);               
      copyImage(2,3,30);
      showImage(3);   
      sleep(500);
      copyImage(3,4); 
      rotateImage(4,-45);        
      sideDetection(4);                        
      showImage(4);   
      sleep(500);        
      showPipImage();  
              
    } catch(Exception e) {
      throw new RuntimeException(e+ " Fct [Demo]");
    }                 
      
  }
}

