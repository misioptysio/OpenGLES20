package com.games;

import android.content.Context;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import static com.games.Const.*;

/**
 * Created by Ptysio on 2015-11-15.
 */
public class Globals
{
  public GLLights glLights;
  public GLShaders glShaders;
  public FloatBuffer cameraPosition;
  public FloatBuffer cameraLookAt;
  public FloatBuffer cameraUp;

  public Globals(Context context)
  {
    glLights = new GLLights();
    glShaders = new GLShaders(context);

    cameraPosition = ByteBuffer.allocateDirect(VALUES_PER_V_POSITION * BYTES_PER_FLOAT).asFloatBuffer().put(new float[]{0.0f, 0.0f, 10.0f});
    cameraLookAt = ByteBuffer.allocateDirect(VALUES_PER_V_POSITION * BYTES_PER_FLOAT).asFloatBuffer().put(new float[]{0.0f, 0.0f, 0.0f});
    cameraUp = ByteBuffer.allocateDirect(VALUES_PER_V_POSITION * BYTES_PER_FLOAT).asFloatBuffer().put(new float[]{0.0f, 1.0f, 0.0f});

    resetCameraPositions();
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
    resetCameraPositions();
  }

  public void resetLightPositions()
  {
    glLights.resetLightPositions();
  }

  public void resetCameraPositions()
  {
    cameraPosition.position(0);
    cameraLookAt.position(0);
    cameraUp.position(0);
  }
}
