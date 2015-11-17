package com.games;

import android.content.Context;

/**
 * Created by Ptysio on 2015-11-15.
 */
public class Globals
{
  public GLLights glLights;
  public GLShaders glShaders;


  public float[] cameraPosition = new float[3];
  public float[] cameraLookAt = new float[3];
  public float[] cameraUp = new float[3];

  public Globals(Context context)
  {
    glLights = new GLLights();
    glShaders = new GLShaders(context);

    cameraPosition = new float[]{0.0f, 0.0f, 10.0f};
    cameraLookAt = new float[]{0.0f, 0.0f, 0.0f};
    cameraUp = new float[]{0.0f, 1.0f, 0.0f};
  }

  public String getVertexShader(int id)
  {
    String res = glShaders.getVertexShader(id);
    res = "const int LIGHT_COUNT = " + glLights.lightCount + "; " + res;

    return res;
  }

  public String getFragmentShader(int id)
  {
    String res = glShaders.getFragmentShader(id);
    res = "const int LIGHT_COUNT = " + glLights.lightCount + "; " + res;

    return res;
  }

  public void resetBufferPositions()
  {
    resetLightPositions();
  }

  public void resetLightPositions()
  {
    glLights.resetLightPositions();
  }

}
