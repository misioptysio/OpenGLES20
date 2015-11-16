package com.games;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import static com.games.Const.*;
/**
 * Created by Ptysio on 2015-11-15.
 */
public class GLLights
{
  public int lightCount = 0;
  public float[] AMBIENT = {0.2f, 0.2f, 0.2f, 1.0f};
  public static final float MAX_DIST = 2.5f;
  public static final float MAX_DIST_SQUARED = MAX_DIST * MAX_DIST;

  public FloatBuffer lightPosition;
  public FloatBuffer lightColor;

  public GLLights()
  {
    lightPosition = ByteBuffer.allocateDirect(lightCount * VALUES_PER_V_POSITION * BYTES_PER_FLOAT).asFloatBuffer();
    lightPosition.position(0);

    lightColor = ByteBuffer.allocateDirect(lightCount * VALUES_PER_V_COLOR * BYTES_PER_FLOAT).asFloatBuffer();
    lightColor.position(0);
  }

  public void resetLightPositions()
  {
    lightColor.position(0);
    lightPosition.position(0);
  }

  //returns index of newly added light
  public int addLight(float[] position, float[] color)
  {
    FloatBuffer newLightPosition;
    FloatBuffer newLightColor;

    lightCount++;

    newLightPosition = ByteBuffer.allocateDirect(lightCount * VALUES_PER_V_POSITION * BYTES_PER_FLOAT).asFloatBuffer();
    lightPosition.position(0);
    newLightPosition.put(lightPosition);
    newLightPosition.put(position);

    newLightColor = ByteBuffer.allocateDirect(lightCount * VALUES_PER_V_COLOR * BYTES_PER_FLOAT).asFloatBuffer();
    lightColor.position(0);
    newLightColor.put(lightColor);
    newLightColor.put(color);

    lightPosition = newLightPosition;
    lightColor = newLightColor;

    return (lightCount-1);
  }

  //sets light at specific index
  public void setLight(int index, float[] position, float[] color)
  {
    if ((index >= 0) && (index < lightCount))
    {
      lightPosition.position(index * VALUES_PER_V_POSITION);
      lightPosition.put(position, 0, position.length);

      lightColor.position(index *  VALUES_PER_V_COLOR);
      lightColor.put(color, 0, position.length);
    }
  }

  public void getLight(int index, float[] position, float[] color)
  {
    if ((index >= 0) && (index < lightCount))
    {
      lightPosition.position(index * VALUES_PER_V_POSITION);
      lightPosition.get(position, 0, VALUES_PER_V_POSITION);

      lightColor.position(index * VALUES_PER_V_COLOR);
      lightColor.get(color, 0, VALUES_PER_V_COLOR);
    }
  }

  //define delLight(int index)
}
