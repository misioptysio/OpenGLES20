package com.games;

import android.opengl.GLES20;

/*
tutorials: http://developer.android.com/training/graphics/opengl/draw.html
http://stackoverflow.com/questions/9371868/android-oncreate-is-not-being-called
*/

/**
 * Created by piotr.plys on 2015-10-26.
 */
public class GLES20Renderer extends GLRenderer
{
//	private GLTriangle mTriangle;

  //shader type GLES20.GL_VERTEX_SHADER or GLES20.GL_FRAGMENT_SHADER
  public static int loadShader(int type, String shaderCode)
  {
    int shader = GLES20.glCreateShader(type);

    GLES20.glShaderSource(shader, shaderCode);
    GLES20.glCompileShader(shader);

    return shader;
  }

  public void initOnce()
  {
    GLES20.glClearColor(0.0f, 0.0f, 0.5f, 1.0f);
//		mTriangle = new GLTriangle(-1f, -1f, 0f, 1f, -1f, 0f, -1f, 1f, 0f);
//		mTriangle.setColors(1.0f, 0.0f, 0.0f, 1.0f, 0.8f, 0.0f, 0.0f, 0.4f, 1.0f);
  }

  public void initFrame()
  {
    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
  }

  public void GLES20Renderer()
  {
    Utils.log("GLES20Renderer constructor");
  }

  @Override
  public void onCreate(int width, int height, boolean contextLost)
  {
    Utils.log("GLES20Renderer.onCreate() as %dpx x %dpx", width, height);
    initOnce();
    GLES20.glViewport(0, 0, width, height);
  }

  @Override
  public void onDrawFrame(boolean firstDraw)
  {
//    Utils.log("Frame drawn @" + getFPS() + "FPS");
    initFrame();
//		mTriangle.draw();
  }
}

