package org.javascool.proglets.rubik;

import javax.media.j3d.Transform3D;
import javax.vecmath.Vector3d;


class Transform3DBuilder {
  Transform3D transform = new Transform3D();
  Transform3DBuilder rotX(double angle) {
    Transform3D rot = new Transform3D();
    rot.rotX(angle);
    transform.mul(rot);
    return this;
  }
  Transform3DBuilder mul(Transform3D tr) {
    transform.mul(tr);
    return this;
  }
  Transform3DBuilder antimul(Transform3D tr) {
    transform.mul(tr,transform);
    return this;
  }
  
  Transform3DBuilder rotY(double angle) {
    Transform3D rot = new Transform3D();
    rot.rotY(angle);
    transform.mul(rot);
    return this;
  }
  Transform3DBuilder rotZ(double angle) {
    Transform3D rot = new Transform3D();
    rot.rotZ(angle);
    transform.mul(rot);
    return this;
  }
  Transform3DBuilder scale(double scale) {
    Transform3D rot = new Transform3D();
    rot.setScale(scale);
    transform.mul(rot);
    return this;
  }
  Transform3DBuilder translate(double x,double y,double z) {
    Transform3D rot = new Transform3D();
    rot.setTranslation(new Vector3d(x,y,z));
    transform.mul(rot);
    return this;
  }
  Transform3D getTransform() {
    return new Transform3D(transform);
  }
  
}