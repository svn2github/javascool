package org.javascool.proglets.rubik;

enum Face implements Move {
  TOP(Rotation.xPlus),BOTTOM(Rotation.xMinus),LEFT(Rotation.yPlus),RIGHT(Rotation.yMinus),
  /* as action, represents anticlockwise rotation */
  REAR(Rotation.zPlus) {
    @Override
    public void end(RubikInterpolator interpolator) {
      interpolator.endRotation(this);
    }
    @Override
    public void step(RubikInterpolator interpolator, float f) {
      interpolator.stepFace(this,f);
    }
    public void turn(ViewCube cube) {
      cube.antiRotate();
    }
  },
  FRONT(Rotation.zMinus) {
    @Override
    public void end(RubikInterpolator interpolator) {
      interpolator.endRotation(this);
    }
    @Override
    public void step(RubikInterpolator interpolator, float f) {
      interpolator.stepFace(this,f);
    }
    public void turn(ViewCube cube) {
      cube.rotate();
    }
  };
  Face() {
    rotation=null;
  }
  Face(Rotation rotation) {
    this.rotation = rotation;
  }
  final Rotation rotation;
  @Override
  public void end(RubikInterpolator interpolator) {
    interpolator.endFace(this);
  }
  @Override
  public void step(RubikInterpolator interpolator, float f) {
    interpolator.stepFace(this, f);
  }
  public void turn(ViewCube cube) {
    cube.bringToFront(this);
  }
}