package org.javascool.proglets.rubik;

import java.util.EnumMap;

import javax.media.j3d.Transform3D;

public class ViewCube {
  private EnumMap<Face,Action> position = new EnumMap<Face, Action>(Face.class);
  public ViewCube() {
    position.put(Face.TOP, Action.TOP_POSITIVE);
    position.put(Face.BOTTOM, Action.BOTTOM_POSITIVE);
    position.put(Face.LEFT, Action.LEFT_POSITIVE);
    position.put(Face.RIGHT, Action.RIGHT_POSITIVE);
    position.put(Face.REAR, Action.REAR_POSITIVE);
    position.put(Face.FRONT, Action.FRONT_POSITIVE);
  }
  
  /* move Face face to front (it cannot be rear or front)*/
  void bringToFront(Face face) {
    move(Face.FRONT,face,Face.REAR,FACES[face.ordinal()^1]);
  }
  
  
  
  void moveViewPort(Transform3D transform, Face face, float alpha) {
    face.rotation.transform(transform, alpha);
  }
  
  private static Face[] FACES = Face.values();
  
  private void move(Face... faces) {
    Action action = position.get(faces[0]);
    for(int i=0;i<faces.length-1;i++) {
      position.put(faces[i],position.get(faces[i+1]));
    }
    position.put(faces[faces.length-1], action);
  }

  public Action getAction(Face turn) {
    return position.get(turn);
  }

  void rotate() {
      move(Face.LEFT,Face.BOTTOM,Face.RIGHT,Face.TOP);
  }
  
  void antiRotate() {
      move(Face.LEFT,Face.TOP,Face.RIGHT,Face.BOTTOM);
  }
  
}
