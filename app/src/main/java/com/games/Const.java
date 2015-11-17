package com.games;

/**
 * Created by Ptysio on 2015-11-15.
 */
public final class Const
{
  //indices of buffers in GLObject
  protected static final byte V_POSITION = 0;
  protected static final byte V_COLOR = 1;
  protected static final byte V_NORMAL = 2;
  protected static final byte V_TEXTURE = 3;

  //size of common data types
  protected static final int BYTES_PER_FLOAT = 4;
  protected static final short BYTES_PER_SHORT = 2;

  //count of float per specific attribute
  protected static final int VALUES_PER_V_POSITION = 3;
  protected static final int VALUES_PER_V_COLOR = 4;
  protected static final int VALUES_PER_V_NORMAL = 3;
  protected static final int VALUES_PER_V_TEXTURE = 2;

  public static final float[] zeroMatrix = new float[]{
    0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f
  };

  public static final float[] identityMatrix = new float[]{
    1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f
  };
}
