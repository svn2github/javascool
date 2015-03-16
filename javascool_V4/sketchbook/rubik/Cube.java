package org.javascool.proglets.rubik;

import javax.media.j3d.Appearance;
import javax.media.j3d.Group;
import javax.media.j3d.Material;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;


class Cube {
  
  private static final float[] verts = {
    // front face
    1.0f, -1.0f,  1.0f,
    1.0f,  1.0f,  1.0f,
    -1.0f,  1.0f,  1.0f,
    -1.0f, -1.0f,  1.0f,
    // back face
    -1.0f, -1.0f, -1.0f,
    -1.0f,  1.0f, -1.0f,
    1.0f,  1.0f, -1.0f,
    1.0f, -1.0f, -1.0f,
    // right face
    1.0f, -1.0f, -1.0f,
    1.0f,  1.0f, -1.0f,
    1.0f,  1.0f,  1.0f,
    1.0f, -1.0f,  1.0f,
    // left face
    -1.0f, -1.0f,  1.0f,
    -1.0f,  1.0f,  1.0f,
    -1.0f,  1.0f, -1.0f,
    -1.0f, -1.0f, -1.0f,
    // top face
    1.0f,  1.0f,  1.0f,
    1.0f,  1.0f, -1.0f,
    -1.0f,  1.0f, -1.0f,
    -1.0f,  1.0f,  1.0f,
    // bottom face
    -1.0f, -1.0f,  1.0f,
    -1.0f, -1.0f, -1.0f,
    1.0f, -1.0f, -1.0f,
    1.0f, -1.0f,  1.0f,
  };
  
  private static final float[] normals = {
    // front face
    0.0f, 0.0f,  1.0f,
    0.0f, 0.0f,  1.0f,
    0.0f, 0.0f,  1.0f,
    0.0f, 0.0f,  1.0f,
    // back face
    0.0f, 0.0f, -1.0f,
    0.0f,  0.0f, -1.0f,
    0.0f,  0.0f, -1.0f,
    0.0f,  0.0f, -1.0f,
    // right face
    1.0f, 0.0f, 0.0f,
    1.0f,  0.0f, 0.0f,
    1.0f,  0.0f,  0.0f,
    1.0f, 0.0f,  0.0f,
    // left face
    -1.0f, 0.0f, 0.0f,
    -1.0f, 0.0f, 0.0f,
    -1.0f, 0.0f, 0.0f,
    -1.0f, 0.0f, 0.0f,
    // top face
    0.0f,  1.0f,  0.0f,
    0.0f,  1.0f, 0.0f,
    0.0f,  1.0f, 0.0f,
    0.0f,  1.0f,  0.0f,
    // bottom face
    0.0f, -1.0f,  0.0f,
    0.0f, -1.0f, 0.0f,
    0.0f, -1.0f, 0.0f,
    0.0f, -1.0f,  0.0f,
  };
  
  private static float[] colors(Color3f front, Color3f back,
      Color3f left, Color3f right, Color3f top, Color3f bottom) {
    float[] array = new float[6*4*3];
    fill(array,front,0);
    fill(array,back,4*3);
    fill(array,right,2*4*3);
    fill(array,left,3*4*3);
    fill(array,top,4*4*3);
    fill(array,bottom,5*4*3);
    return array;
  }

  private static void fill(float[] array, Color3f color, int index) {
    for(int i=index;i<index+12;i+=3) {
      array[i]=color.x;
      array[i+1]=color.y;
      array[i+2]=color.z;
    }
  }
  
  private static final Appearance appearance = new Appearance();
  static {
    Material material = new Material(); 
    material.setColorTarget(Material.AMBIENT_AND_DIFFUSE);
    material.setShininess(100);
    appearance.setMaterial(material);
  }
  
  static Shape3D buildCube(Color3f front, Color3f back,
      Color3f left, Color3f right, Color3f top, Color3f bottom) {
    QuadArray geometry = new QuadArray(24,QuadArray.COORDINATES|QuadArray.COLOR_3|QuadArray.NORMALS);
    geometry.setColors(0,colors(front,back,left,right,top,bottom));
    geometry.setCoordinates(0, verts);
    geometry.setNormals(0, normals);
    Shape3D shape = new Shape3D();
    shape.addGeometry(geometry);
    shape.setAppearance(appearance);
    return shape;
  }
  
  private static Color3f RED = new Color3f(0.8f,0f,0f);
  private static Color3f YELLOW = new Color3f(1f,1f,0f);
  private static Color3f BLUE = new Color3f(0.3f,0.3f,1f);
  private static Color3f GREEN = new Color3f(0f,1f,0.0f);
  private static Color3f ORANGE = new Color3f(0.8f,0.4f,0f);
  private static Color3f WHITE = new Color3f(1f,1f,1f);
  private static Color3f BLACK = new Color3f(0f,0f,0f);
  
  
  
  static Group createCube(int x,int y,int z) {
    Color3f front = (z==1)?RED:BLACK;
    Color3f back = (z==-1)?YELLOW:BLACK;
    Color3f left = (x==-1)?BLUE:BLACK;
    Color3f right = (x==1)?GREEN:BLACK;
    Color3f top = (y==1)?ORANGE:BLACK;
    Color3f bottom = (y==-1)?WHITE:BLACK;
    Shape3D cube = buildCube(front, back, left, right, top, bottom);
    double d=1.2d;
    TransformGroup tr = new TransformGroup(new Transform3DBuilder().translate(x*d, y*d, z*d).getTransform());
    tr.addChild(cube);
    return tr;
  }
}
