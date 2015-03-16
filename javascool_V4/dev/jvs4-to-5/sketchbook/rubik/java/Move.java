package org.javascool.proglets.rubik;

interface Move {
  void end(RubikInterpolator interpolator);
  void step(RubikInterpolator interpolator, float f);
}
