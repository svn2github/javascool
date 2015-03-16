package org.javascool.proglets.rubik;

import java.awt.Component;
import java.awt.GraphicsConfiguration;
import java.net.URL;
import java.util.concurrent.LinkedBlockingDeque;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Group;
import javax.media.j3d.PointLight;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;

import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.Viewer;
import com.sun.j3d.utils.universe.ViewingPlatform;

public class RubikAnimator {

  private static LinkedBlockingDeque<Move> actions = new LinkedBlockingDeque<Move>();
  private static ViewCube viewCube = new ViewCube();

  public static void randomMove() throws InterruptedException {
    actions.put(Action.random());
  }
  
  public static void simpleRandomMove() {
    try {
      randomMove();
    } catch (InterruptedException e) {
      throw new IllegalStateException("Main thread interrupted");
    }
  }
  
  /* Instructs UI to rotate a face of the cube */
  public static void turnFace(Face face, boolean direct)
      throws InterruptedException {
    Action action = viewCube.getAction(face);
    if (!direct)
      action = action.opposite();
    actions.put(action);
  }

  /* Instructs UI to rotate a face of the cube */
  public static void simpleTurnFace(Face face, boolean direct) {
    try {
      turnFace(face, direct);
    } catch (InterruptedException e) {
      throw new IllegalStateException("Main thread interrupted");
    }
  }

  public static void bringToFront(Face face, boolean now)
      throws InterruptedException {
    face.turn(viewCube);
    if (now)
      actions.putFirst(face);
    else
      actions.put(face);
  }

  /* Instructs UI to rotate the cube to view it from a another angle */
  static void simpleBringToFront(Face face, boolean now) {
    try {
      bringToFront(face, now);
    } catch (InterruptedException e) {
      throw new IllegalStateException("Main thread interrupted");
    }
  }

  public static void simpleAntiRotate(boolean now) {
    try {
      RubikAnimator.antiRotate(now);
    } catch (InterruptedException e) {
      throw new IllegalStateException("Main thread interrupted");
    }
  }

  public static void antiRotate(boolean now) throws InterruptedException {
    bringToFront(Face.REAR, now);
  }

  public static void simpleRotate(boolean now) {
    try {
      RubikAnimator.rotate(now);
    } catch (InterruptedException e) {
      throw new IllegalStateException("Main thread interrupted");
    }
  }

  public static void rotate(boolean now) throws InterruptedException {
    bringToFront(Face.FRONT, now);
  }

  private static TransformGroup[][][] getCubes(Group parent) {
    TransformGroup[][][] groups = new TransformGroup[2][2][2];
    for (int x = 0; x <= 1; x++)
      for (int y = 0; y <= 1; y++)
        for (int z = 0; z <= 1; z++) {
          TransformGroup transf = new TransformGroup();
          groups[x][y][z] = transf;
          parent.addChild(transf);
          transf.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
          transf.addChild(Cube.createCube(2 * x - 1, 2 * y - 1, 2 * z - 1));
        }
    return groups;
  }

  private static Component panel;

  /**
   * Return the main widget of the UI
   * 
   * @param fps
   *          the displayed frame per second (0 for the fastest)
   */
  public static Component getCube(int fps) {

    if (panel!=null)
      return panel;

    URL url = RubikAnimator.class.getResource("RubikAnimator.class");
    if (url.getProtocol().equals("jar")) {
      String path=url.getPath();
      path=path.substring(path.indexOf(':')+1, path.indexOf('!'));
      path=path.substring(0,path.lastIndexOf('/'));

      String libraryPath = System.getProperty("java.library.path");
      String newPath;
      String pathSeparator = System.getProperty("path.separator");
      if (libraryPath.length() == 0)
        newPath = path;
      else
        newPath = path + pathSeparator + libraryPath;
      System.setProperty("java.library.path", newPath);
    }
    GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
    Canvas3D canvas = new Canvas3D(config);
    canvas.setFocusable(true);

    Viewer viewer = new Viewer(new Canvas3D[] { canvas }, null, null, false);

    ViewingPlatform viewingPlatform = new ViewingPlatform();
    // viewingPlatform.setViewPlatformBehavior(viewBehavior);

    SimpleUniverse universe = new SimpleUniverse(viewingPlatform, viewer);
    BranchGroup group = new BranchGroup();
    Transform3D tr = new Transform3DBuilder().rotX(Math.PI / 6)
        .rotY(Math.PI / 6).scale(0.1).getTransform();
    TransformGroup globtransf = new TransformGroup(tr);

    TransformGroup subGroup = new TransformGroup(); // groupe to change view;
    TransformGroup[][][] transfs = RubikAnimator.getCubes(subGroup);
    subGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    globtransf.addChild(subGroup);

    BoundingSphere bound = new BoundingSphere(new Point3d(new double[] { 0, 0,
        0 }), 40.0);

    AmbientLight light = new AmbientLight(new Color3f(0.4F, 0.4F, 0.4F));
    light.setInfluencingBounds(bound);
    globtransf.addChild(light);

    {
      PointLight dLight = new PointLight(new Color3f(0.06F, 0.06F, 0.1F),
          new Point3f(4.0f, 4.0f, 4.0f), new Point3f(0f, 0f, 0.1f));
      dLight.setInfluencingBounds(bound);
      globtransf.addChild(dLight);
    }

    /*
     * {PointLight dLight = new PointLight(new Color3f(1.0F,1.0F,1.0F),new
     * Point3f(-10.0f,-10.0f,10.0f),new Point3f(0f,0.0f,0.1f));
     * dLight.setInfluencingBounds(bound); globtransf.addChild(dLight);}
     */

    // Alpha alpha = new Alpha(1,msDelay+msPerMove*args.length);
    // Alpha alpha = new Alpha(-1,msDelay,0,msPerMove,0,0);
    // RubikInterpolator interpolator = new
    // RubikInterpolator(fps,msDelay/((float)msDelay+msPerMove*args.length),alpha,transfs,args);
    RubikInterpolator interpolator = new RubikInterpolator(transfs, subGroup,
        viewCube);
    ActionBehavior behavior = new ActionBehavior(interpolator, fps, canvas,
        actions);
    behavior.setSchedulingBounds(bound);
    globtransf.addChild(behavior);

    Background back = new Background(0F, 0.5F, 0.5F);
    back.setApplicationBounds(bound);
    globtransf.addChild(back);

    group.addChild(globtransf);
    group.compile();
    universe.addBranchGraph(group);
    universe.getViewingPlatform().setNominalViewingTransform();

    panel=canvas;
    return canvas;
  }

}
