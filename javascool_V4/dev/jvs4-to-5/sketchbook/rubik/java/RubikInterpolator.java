package org.javascool.proglets.rubik;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;

class RubikInterpolator {
  
  private final TransformGroup[][][] groups;
  private final Transform3D baseTransform = new Transform3D();
  private final TransformGroup platform;
  //private final ViewCube cube;
  
  
  RubikInterpolator(TransformGroup[][][] targets, TransformGroup platform, ViewCube cube) {
    this.groups = targets;
    this.platform = platform;
    //this.cube = cube;
  }
  
  private final Transform3D transformBuffer = new Transform3D();
  private final Transform3D[][][] transforms = new Transform3D[2][2][2];
  {
    for(int x=0;x<=1;x++)
      for(int y=0;y<=1;y++)
        for(int z=0;z<=1;z++) {
          transforms[x][y][z] = new Transform3D();
        }
  }
  
  public void endAction(Action action) {
    for(int x=0;x<=1;x++)
      for(int y=0;y<=1;y++)
        for(int z=0;z<=1;z++)
          if (action.moves(x, y, z))
            action.transform(transforms[x][y][z],1f);
    action.moveGroups(transforms);
    action.moveGroups(groups);
    for(int x=0;x<=1;x++)
      for(int y=0;y<=1;y++)
        for(int z=0;z<=1;z++)
            groups[x][y][z].setTransform(transforms[x][y][z]);
  }
  
  public void endFace(Face face) {
    face.rotation.transform(baseTransform, 1f);
    platform.setTransform(baseTransform);
  }
  
  public void stepFace(Face face, float f) {
    transformBuffer.set(baseTransform);
    face.rotation.transform(transformBuffer, f);
    platform.setTransform(transformBuffer);
  }
  
  public void stepAction(Action action, float alphaValue) {   
    for(int x=0;x<=1;x++)
      for(int y=0;y<=1;y++)
        for(int z=0;z<=1;z++)
          if (action.moves(x, y, z)) {
            transformBuffer.set(transforms[x][y][z]);
            action.transform(transformBuffer,alphaValue);
            groups[x][y][z].setTransform(transformBuffer);
          }
    
  }

  public void endRotation(Face face) {
    face.rotation.transform(baseTransform, 1f);
    platform.setTransform(baseTransform);
  }
  
}
