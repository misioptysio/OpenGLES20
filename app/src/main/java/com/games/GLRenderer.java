package com.games;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/*
tutorials: http://developer.android.com/training/graphics/opengl/draw.html
http://stackoverflow.com/questions/9371868/android-oncreate-is-not-being-called
http://developer.android.com/reference/android/opengl/GLSurfaceView.Renderer.html
*/

/**
 * Created by piotr.plys on 2015-10-26.
 */
public class GLRenderer implements GLSurfaceView.Renderer
{
	private final float[] mMVPMatrix = new float[16];
	private final float[] mProjectionMatrix = new float[16];
	private final float[] mViewMatrix = new float[16];
	private boolean mFirstDraw;
	private boolean mSurfaceCreated;
	private int mWidth;
	private int mHeight;
	private long mLastTime;
	private int mFPS;
	private GLTriangle mTriangle;

	public static int loadShader(int type, String shaderCode)
	{
		int shader = GLES20.glCreateShader(type);

		GLES20.glShaderSource(shader, shaderCode);
		GLES20.glCompileShader(shader);

		return shader;
	}

	public void GLRenderer()
	{
		mFirstDraw = true;
		mSurfaceCreated = false;
		mWidth = -1;
		mHeight = -1;
		mLastTime = System.currentTimeMillis();
		mFPS = 0;
	}

	public void initOnce()
	{
		GLES20.glClearColor(0.0f, 0.0f, 0.5f, 1.0f);

		GLES20.glViewport(0, 0, mWidth, mHeight);

//		mTriangle = new GLTriangle(-0.8f, -0.8f, 0f, 0.8f, -0.8f, 0f, -0.8f, 0.8f, 0f);
		mTriangle = new GLTriangle(0.0f, 0.622008459f, 0.0f, -0.5f, -0.311004243f, 0.0f, 0.5f, -0.311004243f, 0.0f);
		mTriangle.setColors(1.0f, 0.0f, 0.0f, 1.0f, 0.8f, 0.0f, 0.0f, 0.4f, 1.0f);
	}

	public void initFrame()
	{
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		GLES20.glViewport(0, 0, mWidth, mHeight);

		float ratio = (float) mWidth / mHeight;
		if (ratio < 1.0)
		{
			Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 2, 7);
		}
		else
		{
			Matrix.frustumM(mProjectionMatrix, 0, -1, 1, -1.0f/ratio, 1.0f/ratio, 2, 7);
		}
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		Utils.log("GLRenderer.onSurfaceCreated() Surface created");

		mSurfaceCreated = true;
		mWidth = -1;
		mHeight = -1;
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height)
	{
		if (!mSurfaceCreated && width == mWidth && height == mHeight)
		{
			Utils.log("GLRenderer.onSurfaceChanged() Surface changed but already handled");
			return;
		}
		// Android honeycomb has an option to keep the context.
		String msg = String.format("GLRenderer.onSurfaceChanged() Surface changed to %dpx x %dpx", width, height);
		if (mSurfaceCreated)
		{
			msg += " (context lost)";
		}
		Utils.log(msg);

		mWidth = width;
		mHeight = height;

		initOnce();

		mSurfaceCreated = false;
	}

	@Override
	public void onDrawFrame(GL10 gl)
	{
		onDrawFrame(mFirstDraw);

		if (Utils.DEBUG)
		{
			mFPS++;
			long currentTime = System.currentTimeMillis();
			if (currentTime - mLastTime >= 1000)
			{
//				Utils.log("FPS " + mFPS);
				mFPS = 0;
				mLastTime = currentTime;
			}
		}

		if (mFirstDraw)
		{
			mFirstDraw = false;
		}
	}


	public void onDrawFrame(boolean firstDraw)
	{
//    Utils.log("Frame drawn @" + getFPS() + "FPS");
		initFrame();

		// Set the camera position (View matrix)
		Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

		// Calculate the projection and view transformation
		Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

		// Draw shape
		mTriangle.draw(mMVPMatrix);
//    mTriangle.draw();
	}

}

