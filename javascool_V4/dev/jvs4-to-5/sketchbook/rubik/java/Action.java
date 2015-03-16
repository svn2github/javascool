package org.javascool.proglets.rubik;

import java.util.Random;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;


enum Action implements Move {
  
  LEFT_NEGATIVE(0,Position.position,Position.first,Position.second,Rotation.xPlus) {
    @Override
    Action opposite() {
      return LEFT_POSITIVE;
    }
  },
  LEFT_POSITIVE(0,Position.position,Position.second,Position.first,Rotation.xMinus) {
    @Override
    Action opposite() {
      return LEFT_NEGATIVE;
    }
  },
  RIGHT_NEGATIVE(1,Position.position,Position.second,Position.first,Rotation.xMinus) {
    @Override
    Action opposite() {
      return RIGHT_POSITIVE;
    }
  },
  RIGHT_POSITIVE(1,Position.position,Position.first,Position.second,Rotation.xPlus) {
    @Override
    Action opposite() {
      return RIGHT_NEGATIVE;
    }
  },
  BOTTOM_NEGATIVE(0,Position.second,Position.position,Position.first,Rotation.yPlus) {
    @Override
    Action opposite() {
      return BOTTOM_POSITIVE;
    }
  },
  BOTTOM_POSITIVE(0,Position.first,Position.position,Position.second,Rotation.yMinus) {
    @Override
    Action opposite() {
      return BOTTOM_NEGATIVE;
    }
  },
  TOP_NEGATIVE(1,Position.first,Position.position,Position.second,Rotation.yMinus) {
    @Override
    Action opposite() {
      return TOP_POSITIVE;
    }
  },
  TOP_POSITIVE(1,Position.second,Position.position,Position.first,Rotation.yPlus) {
    @Override
    Action opposite() {
      return TOP_NEGATIVE;
    }
  },
  REAR_NEGATIVE(0,Position.first,Position.second,Position.position,Rotation.zPlus) {
    @Override
    Action opposite() {
      return REAR_POSITIVE;
    }
  },
  REAR_POSITIVE(0,Position.second,Position.first,Position.position,Rotation.zMinus) {
    @Override
    Action opposite() {
      return REAR_NEGATIVE;
    }
  },
  FRONT_NEGATIVE(1,Position.second,Position.first,Position.position,Rotation.zMinus) {
    @Override
    Action opposite() {
      return FRONT_POSITIVE;
    }
  },
  FRONT_POSITIVE(1,Position.first,Position.second,Position.position,Rotation.zPlus) {
    @Override
    Action opposite() {
      return FRONT_NEGATIVE;
    }
  };

  
  abstract Action opposite();
  
  static final int[] diagTurn = {0,0, 0,1, 1,1, 1,0};
  //static final int[] edgeTurn = {0,1, 1,2, 2,1, 1,0 };
  
  static enum Position {
    position {
      @Override
      int val(Action action,int[] value, int index) {
        return action.position;
      }
      
      @Override
      boolean test(int value, Action action) {
        return value==action.position;
      }
    },
    first {
      @Override
      int val(Action action,int[] value, int index) {
        return value[(2*index)%8];
      }
    },
    second {
      @Override
      int val(Action action,int[] value, int index) {
        return value[(2*index+1)%8];
      }      
    };
    abstract int val(Action action,int[] value, int index);
    boolean test(int value, Action action) {
      return true;
    }
  }

  
  final int position;
  private final Position xVal,yVal,zVal;
  private final Rotation rotation;
  
  
  Action(int position, Position xVal, Position yVal, Position zVal, Rotation rotation) {
    this.position = position;
    this.xVal = xVal;
    this.yVal = yVal;
    this.zVal = zVal;
    this.rotation = rotation;
  }
  
  
  private <T> void moveGroups(T[][][] groups,int[] turn) {
    int i=0;
    T cube = groups[xVal.val(this,turn,i)][yVal.val(this,turn,i)][zVal.val(this,turn,i)];
    for(;i<3;i++) {
      groups[xVal.val(this,turn,i)][yVal.val(this,turn,i)][zVal.val(this,turn,i)]=
        groups[xVal.val(this,turn,i+1)][yVal.val(this,turn,i+1)][zVal.val(this,turn,i+1)];
    }
    groups[xVal.val(this,turn,i)][yVal.val(this,turn,i)][zVal.val(this,turn,i)]=cube;
  }
  
  <T> void moveGroups(T[][][] groups) {
    moveGroups(groups,diagTurn);
    //moveGroups(groups,edgeTurn);
  }
  
  boolean moves(int x, int y, int z) {
    return xVal.test(x,this) && yVal.test(y,this) && zVal.test(z,this);
  }
  
  void transform(Transform3D transform, float alpha) {
    rotation.transform(transform,alpha);
  }
  
  void transform(TransformGroup group, Transform3D buffer, float alpha) {
    group.getTransform(buffer);
    rotation.transform(buffer,alpha);
    group.setTransform(buffer);
  }
  
  @Override
  public void step(RubikInterpolator interpolator, float f) {
    interpolator.stepAction(this, f);
  }
  
  @Override
  public void end(RubikInterpolator interpolator) {
    interpolator.endAction(this);
  }
  
  private static final Random RND = new Random(666);
  private static final Action[] VALUES = values();
  
  public static Action random() {
    return VALUES[1+RND.nextInt(VALUES.length-1)];
  }
}
