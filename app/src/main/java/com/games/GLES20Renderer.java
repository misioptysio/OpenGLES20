package com.games;

import android.opengl.GLES20;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

//tutorials: http://developer.android.com/training/graphics/opengl/draw.html

/**
 * Created by piotr.plys on 2015-10-26.
 */
public class GLES20Renderer extends GLRenderer
{
	private GLTriangle mTriangle;

	public static int loadShader(int type, String shaderCode)
	{
		// create a vertex shader type (GLES20.GL_VERTEX_SHADER)
		// or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
		int shader = GLES20.glCreateShader(type);

		// add the source code to the shader and compile it
		GLES20.glShaderSource(shader, shaderCode);
		GLES20.glCompileShader(shader);

		return shader;
	}

	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config)
	{
		mTriangle = new GLTriangle(-1f, -1f, 0f, 1f, -1f, 0f, -1f, 1f, 0f);
		mTriangle.setColors(1.0f, 0.0f, 0.0f, 1.0f, 0.8f, 0.0f, 0.0f, 0.4f, 1.0f);
	}

	@Override
	public void onCreate(int width, int height, boolean contextLost)
	{
		GLES20.glClearColor(0.0f, 0.5f, 0.0f, 1f);
	}

	@Override
	public void onDrawFrame(boolean firstDraw)
	{
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		//GLES20.glDrawArrays(GLES20.GL_POINTS, );
		GLES20.glFlush();
	}
}

