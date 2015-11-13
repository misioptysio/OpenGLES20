package com.games;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glViewport;

/*
tutorials: http://developer.android.com/training/graphics/opengl/draw.html
http://stackoverflow.com/questions/9371868/android-oncreate-is-not-being-called
http://developer.android.com/reference/android/opengl/GLSurfaceView.Renderer.html

Matrices:
*   http://www.songho.ca/opengl/gl_transform.html

Shaders:
*   http://www.raywenderlich.com/70208/opengl-es-pixel-shaders-tutorial
*** http://joshbeam.com/articles/getting_started_with_glsl/
*** http://www.learnopengles.com/android-lesson-one-getting-started/

Textures:
http://stackoverflow.com/questions/1339136/draw-text-in-opengl-es-android

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
	private GLShader mShader;

	//pass the reference to global Shader instance
	public void setShader(GLShader shader)
	{
		mShader = shader;
	}

	public static int loadShader(int type, String shaderCode)
	{
		int shader = glCreateShader(type);

		glShaderSource(shader, shaderCode);
		glCompileShader(shader);

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
		glClearColor(0.0f, 0.0f, 0.5f, 1.0f);
		// Enable depth test
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		// Accept fragment if it closer to the camera than the former one
		GLES20.glDepthFunc(GLES20.GL_LESS);

		// enable face culling feature
		GLES20.glEnable(GLES20.GL_CULL_FACE);
// specify which faces to not draw
		GLES20.glCullFace(GLES20.GL_BACK);


		glViewport(0, 0, mWidth, mHeight);

//		mTriangle = new GLTriangle(-0.8f, -0.8f, 0f, 0.8f, -0.8f, 0f, -0.8f, 0.8f, 0f);
		mTriangle = new GLTriangle();
		mTriangle.setShader(mShader);
		mTriangle.setPositions(0.0f, 0.622008459f, 0.0f, -0.5f, -0.311004243f, 0.0f, 0.5f, -0.311004243f, 0.0f);
		mTriangle.setColors(1.0f, 0.0f, 1.0f,  1.0f, 1.0f, 0.0f,  0.0f, 1.0f, 1.0f);
		mTriangle.init();
	}

	public void initFrame()
	{
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glViewport(0, 0, mWidth, mHeight);

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
		float[] mRotationMatrix = new float[16];
		float[] scratch = new float[16];

//    Utils.log("Frame drawn @" + getFPS() + "FPS");
		initFrame();

		// Set the camera position (View matrix)
		Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

		// Calculate the projection and view transformation
		Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

		long time = SystemClock.uptimeMillis();
		float angle = 0.080f * ((int) time);
		Matrix.setRotateM(mRotationMatrix, 0, angle, 1.320f, 0.0f, -1.0f);

		Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);

		float sinValue1 = (float) (Math.sin(angle * 0.017f) + 1) * 0.5f;
		float sinValue2 = (float) (Math.sin(angle * 0.027f) + 1) * 0.5f;
		float sinValue3 = (float) (Math.sin(angle * 0.043f) + 1) * 0.5f;

		mTriangle.setColor(0, 1.0f, 0.0f, sinValue1, 1.0f);
		mTriangle.setColor(1, sinValue2, 1.0f, 0.0f, 1.0f);
		mTriangle.setColor(2, 0.0f, sinValue3, 1.0f, 1.0f);

		mTriangle.draw(scratch);
		// Draw shape
//		mTriangle.draw(mMVPMatrix);
	}

}

