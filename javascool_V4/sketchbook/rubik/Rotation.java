package org.javascool.proglets.rubik;

import javax.media.j3d.Transform3D;

enum Rotation {
  xPlus {
    @Override
    void setTransformBuffer(float alpha) {
      transformBuffer.rotX(Math.PI/2*alpha);
    }
  },xMinus {
    @Override
    void setTransformBuffer(float alpha) {
      transformBuffer.rotX(-Math.PI/2*alpha);;
    }
  },yPlus {
    @Override
    void setTransformBuffer(float alpha) {
      transformBuffer.rotY(Math.PI/2*alpha);
    }
  },yMinus {
    @Override
    void setTransformBuffer(float alpha) {
      transformBuffer.rotY(-Math.PI/2*alpha);
    }
  },zPlus {
    @Override
    void setTransformBuffer(float alpha) {
      transformBuffer.rotZ(Math.PI/2*alpha);
    }
  },zMinus {
    @Override
    void setTransformBuffer(float alpha) {
      transformBuffer.rotZ(-Math.PI/2*alpha);
    }
  };
  void transform(Transform3D transform, float alpha) {
    setTransformBuffer(alpha);
    transform.mul(transformBuffer,transform);
  }
  abstract void setTransformBuffer(float alpha);
  final Transform3D transformBuffer = new Transform3D();
}