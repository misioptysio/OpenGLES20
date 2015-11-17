package com.games;

import java.util.Arrays;

import static com.games.Const.VALUES_PER_V_COLOR;
import static com.games.Const.VALUES_PER_V_POSITION;

/**
 * Created by Ptysio on 2015-11-15.
 */
public class GLLights
{
  public int mLightCount = 0;
  public static final int LIGHT_MAX_COUNT = 16;
  public float[] AMBIENT = {0.2f, 0.2f, 0.2f, 1.0f};
  public static final float MAX_DIST = 2.5f;
  public static final float MAX_DIST_SQUARED = MAX_DIST * MAX_DIST;

  public float[] mLightPosition;
  public float[] mLightColor;

  public GLLights()
  {
    mLightPosition = new float[VALUES_PER_V_POSITION * LIGHT_MAX_COUNT];
    mLightColor = new float[VALUES_PER_V_COLOR * LIGHT_MAX_COUNT];
  }

  //returns index of newly added light
  public int addLight(float[] position, float[] color)
  {
    for (int i = 0; i < VALUES_PER_V_POSITION; i++)
    {
      mLightPosition[mLightCount * VALUES_PER_V_POSITION + i] = position[i];
    }

    for (int i = 0; i < VALUES_PER_V_COLOR; i++)
    {
      mLightColor[mLightCount * VALUES_PER_V_COLOR + i] = color[i];
    }
    mLightCount++;

    return (mLightCount - 1);
  }

  //sets light at specific index
  public void setLight(int index, float[] position, float[] color)
  {
    if ((index >= 0) && (index < mLightCount))
    {
      for (int i = 0; i < VALUES_PER_V_POSITION; i++)
      {
        mLightPosition[index * VALUES_PER_V_POSITION + i] = position[i];
      }

      for (int i = 0; i < VALUES_PER_V_COLOR; i++)
      {
        mLightColor[index * VALUES_PER_V_COLOR + i] = color[i];
      }
    }
  }

  public void getLight(int index, float[] position, float[] color)
  {
    if ((index >= 0) && (index < mLightCount))
    {
      int tempIndex;

      tempIndex = index * VALUES_PER_V_POSITION;
      position = Arrays.copyOfRange(mLightPosition, tempIndex, tempIndex + VALUES_PER_V_POSITION - 1);

      tempIndex = index * VALUES_PER_V_COLOR;
      color = Arrays.copyOfRange(mLightColor, tempIndex, tempIndex + VALUES_PER_V_COLOR - 1);
    }
  }

  //deletes ligth at given index
  public void delLight(int index)
  {
    int indexEnd;
    int indexStart;

    if ((index >= 0) && (index < mLightCount))
    {
      mLightCount--;

      indexStart = index * VALUES_PER_V_POSITION;
      indexEnd = mLightCount * VALUES_PER_V_POSITION;
      for (int i = indexStart; i < indexEnd; i++)
      {
        mLightPosition[i] = mLightPosition[i + VALUES_PER_V_POSITION];
      }
      Arrays.fill(mLightPosition, indexEnd, indexEnd + VALUES_PER_V_POSITION - 1, 0.0f);

      indexStart = index * VALUES_PER_V_COLOR;
      indexEnd = mLightCount * VALUES_PER_V_COLOR;
      for (int i = indexStart; i < indexEnd; i++)
      {
        mLightColor[i] = mLightColor[i + VALUES_PER_V_COLOR];
      }
      Arrays.fill(mLightColor, indexEnd, indexEnd + VALUES_PER_V_COLOR - 1, 0.0f);
    }
  }
}
