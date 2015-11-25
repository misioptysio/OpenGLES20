package com.games;

/**
 * Created by Ptysio on 2015-11-15.
 */
public final class Globals
{
  private final GLLights glLights;
  private final GLShaders glShaders;
  private final GLTextures glTextures;

  private float[] cameraPosition = new float[3];
  private float[] cameraLookAt = new float[3];
  private float[] cameraUp = new float[3];

  public Globals()
  {
    glLights = new GLLights();
    glShaders = new GLShaders();
    glTextures = new GLTextures();

    cameraPosition = new float[]{0.0f, 0.0f, 10.0f};
    cameraLookAt = new float[]{0.0f, 0.0f, 0.0f};
    cameraUp = new float[]{0.0f, 1.0f, 0.0f};
  }

  public String getVertexShader(int id)
  {
    String res = glShaders.getVertexShader(id);
    res = "const int LIGHT_COUNT = " + glLights.mLightCount + "; " + res;

    return res;
  }

  public String getFragmentShader(int id)
  {
    String res = glShaders.getFragmentShader(id);
    res = "const int LIGHT_COUNT = " + glLights.mLightCount + "; " + res;

    return res;
  }

  public float[] getCameraPosition()
  {
    return cameraPosition;
  }

  public float[] getCameraLookAt()
  {
    return cameraLookAt;
  }

  public GLLights getGlLights()
  {
    return glLights;
  }

  public GLShaders getGlShaders()
  {
    return glShaders;
  }

  public GLTextures getGlTextures()
  {
    return glTextures;
  }

  public float[] getCameraUp()
  {
    return cameraUp;
  }

  public void setCameraPosition(float[] cameraPosition)
  {
    this.cameraPosition = cameraPosition;
  }

  public void setCameraLookAt(float[] cameraLookAt)
  {
    this.cameraLookAt = cameraLookAt;
  }

  public void setCameraUp(float[] cameraUp)
  {
    this.cameraUp = cameraUp;
  }
}
