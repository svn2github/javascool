package org.javascool.proglets.rubik;

import org.javascool.proglets.rubik.Face;
import org.javascool.proglets.rubik.RubikAnimator;

public class Functions {
  
   // @factory
   private Functions() {}

  public static void front() {
    RubikAnimator.simpleTurnFace(Face.FRONT, false);
  }
  public static void antifront() {
    RubikAnimator.simpleTurnFace(Face.FRONT, true);
  }
  
  public static void left() {
    RubikAnimator.simpleTurnFace(Face.LEFT, false);
  }
  public static void antileft() {
    RubikAnimator.simpleTurnFace(Face.LEFT, true);
  }
  
  public static void right() {
    RubikAnimator.simpleTurnFace(Face.RIGHT, false);
  }
  public static void antiright() {
    RubikAnimator.simpleTurnFace(Face.RIGHT, true);
  }

  public static void up() {
    RubikAnimator.simpleTurnFace(Face.TOP, false);
  }
  public static void antiup() {
    RubikAnimator.simpleTurnFace(Face.TOP, true);
  }

  public static void back() {
    RubikAnimator.simpleTurnFace(Face.REAR, false);
  }
  public static void antiback() {
    RubikAnimator.simpleTurnFace(Face.REAR, true);
  }

  public static void down() {
    RubikAnimator.simpleTurnFace(Face.BOTTOM, false);
  }
  public static void antidown() {
    RubikAnimator.simpleTurnFace(Face.BOTTOM, true);
  }
  
  public static void randomturn() {
    RubikAnimator.simpleRandomMove();
  }
  
  public static void movedown() {
    RubikAnimator.simpleBringToFront(Face.TOP, false);
  }
  public static void moveup() {
    RubikAnimator.simpleBringToFront(Face.BOTTOM, false);
  }
  public static void moveleft() {
    RubikAnimator.simpleBringToFront(Face.RIGHT, false);
  }
  public static void moveright() {
    RubikAnimator.simpleBringToFront(Face.LEFT, false);
  }
  
  public static void rotate() {
    RubikAnimator.simpleRotate(false);
  }
  public static void antirotate() {
    RubikAnimator.simpleAntiRotate(false);
  }
}
