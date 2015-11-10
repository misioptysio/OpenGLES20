package com.games;

import android.opengl.GLES20;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

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

	public void GLES20Renderer()
	{
		Utils.log("GLES20Renderer created");
	}

	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config)
	{
		Utils.log("onSurfaceCreated");
//		mTriangle = new GLTriangle(-1f, -1f, 0f, 1f, -1f, 0f, -1f, 1f, 0f);
//		mTriangle.setColors(1.0f, 0.0f, 0.0f, 1.0f, 0.8f, 0.0f, 0.0f, 0.4f, 1.0f);
	}

	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height)
	{
		Utils.log("onSurfaceChanged BLUE " + width + " x " + height);
		GLES20.glClearColor(0.0f, 0.0f, 0.5f, 1.0f);
		GLES20.glViewport(0, 0, width, height);
	}

	@Override
	public void onCreate(int width, int height, boolean contextLost)
	{
		Utils.log("OpenGL initialized as %dpx x %dpx", width, height);
		GLES20.glClearColor(0.0f, 0.5f, 0.0f, 1.0f);
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
	}

	@Override
	public void onDrawFrame(boolean firstDraw)
	{
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

//		mTriangle.draw();
	}
}

