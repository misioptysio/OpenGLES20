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
*** http://blog.shayanjaved.com/2011/03/13/shaders-android/

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
	private GLCube mCube;

	private Globals mGlobals;

	//pass the reference to global Shaders/Lights instance
	public void setGlobals(Globals globals)
	{
		mGlobals = globals;
	}

	public GLRenderer()
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
		float[] mPos1 = {10f, 5f, 40f};
		float[] mPos2 = {20f, -5f, 5f};
		float[] mPos3 = {0f, 50f, 5f};

		float[] mCol1 = {0.5f, 0.5f, 0.5f, 1f};
		float[] mCol2 = {0.5f, 0.5f, 0.8f, 1f};
		float[] mCol3 = {0.8f, 0.5f, 0.5f, 1f};

		mGlobals.glLights.addLight(mPos1, mCol1);
		mGlobals.glLights.addLight(mPos2, mCol2);
		mGlobals.glLights.addLight(mPos3, mCol3);

//		mGlobals.glLights.delLight(0);
//		mGlobals.glLights.delLight(0);

		mGlobals.cameraPosition = new float[] {0.0f, 0.0f, 5.0f};
		mGlobals.cameraLookAt = new float[] {0.0f, 0.0f, 0.0f};
		mGlobals.cameraUp = new float[] {0.0f, 1.0f, 0.0f};

		glClearColor(0.3f, 0.3f, 0.3f, 1.0f);
		// Enable depth test
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		// Accept fragment if it closer to the camera than the former one
		GLES20.glDepthFunc(GLES20.GL_LESS);

		// enable face culling feature
		//GLES20.glEnable(GLES20.GL_CULL_FACE);
		//GLES20.glCullFace(GLES20.GL_BACK);


		glViewport(0, 0, mWidth, mHeight);

		mCube = new GLCube();
		mCube.setGlobals(mGlobals);
		mCube.init();
	}

	public void initFrame()
	{
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glViewport(0, 0, mWidth, mHeight);

		float ratio = (float) mWidth / mHeight;
		if (ratio < 1.0)
		{
			Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 1, 10);
		}
		else
		{
			Matrix.frustumM(mProjectionMatrix, 0, -1, 1, -1.0f/ratio, 1.0f/ratio, 1, 10);
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
		float[] mVPMatrix = new float[16];

//    Utils.log("Frame drawn @" + getFPS() + "FPS");
		initFrame();

		// Set the camera position (View matrix)
		Matrix.setLookAtM(mViewMatrix, 0, mGlobals.cameraPosition[0], mGlobals.cameraPosition[1], mGlobals.cameraPosition[2], mGlobals.cameraLookAt[0], mGlobals.cameraLookAt[1], mGlobals.cameraLookAt[2], mGlobals.cameraUp[0], mGlobals.cameraUp[1], mGlobals.cameraUp[2]);

		long time = SystemClock.uptimeMillis();
		float angle = 0.0600f * ((int) time);

		mCube.setRotation(angle * 1.000000f, 1.0f, 0.0f, 0.0f, false);
		mCube.setRotation(angle * 0.278957f, 0.0f, 1.0f, 0.0f, true);
		mCube.setScale(1.2f, 1.2f, 1.2f);
		//mCube.setTranslation((float) Math.cos(0.028f * angle), (float) Math.sin(0.023f * angle), 0.0f);
		Matrix.multiplyMM(mVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

		mCube.draw(mVPMatrix);
	}

}

