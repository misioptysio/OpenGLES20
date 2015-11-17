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
		float[] mPos1 = {-10f, 30f, 40f};
		float[] mPos2 = {20f, 60f, 0f};
		float[] mPos3 = {0f, -50f, 3f};

		float[] mCol1 = {1f, 1f, 1f, 1f};
		float[] mCol2 = {1f, 0f, 0f, 1f};
		float[] mCol3 = {0f, 0f, 1f, 1f};

		mGlobals.glLights.addLight(mPos1, mCol1);
//		mGlobals.glLights.addLight(mPos2, mCol2);
//		mGlobals.glLights.addLight(mPos3, mCol3);

		mGlobals.cameraPosition = new float[] {0.0f, 0.0f, 3.0f};
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

//		mTriangle = new GLTriangle(-0.8f, -0.8f, 0f, 0.8f, -0.8f, 0f, -0.8f, 0.8f, 0f);
/*
		mTriangle = new GLTriangle();
		mTriangle.setGlobals(mGlobals);
		mTriangle.setTrianglePositions(0.0f, 0.577350269f, 0.0f, -0.5f, -0.28867513459f, 0.0f, 0.5f, -0.28867513459f, 0.0f);
		mTriangle.setTriangleColors(1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f);
		mTriangle.init();
*/

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
		float[] mRotMatrixX = new float[16];
		float[] mRotMatrixY = new float[16];
		float[] scratch1 = new float[16];
		float[] scratch2 = new float[16];
		float[] scratch3 = new float[16];
		float[] scratch4 = new float[16];

//    Utils.log("Frame drawn @" + getFPS() + "FPS");
		initFrame();

		// Set the camera position (View matrix)
		Matrix.setLookAtM(mViewMatrix, 0,
			mGlobals.cameraPosition[0], mGlobals.cameraPosition[1], mGlobals.cameraPosition[2],
			mGlobals.cameraLookAt[0], mGlobals.cameraLookAt[1], mGlobals.cameraLookAt[2],
			mGlobals.cameraUp[0], mGlobals.cameraUp[1], mGlobals.cameraUp[2]);

		// Calculate the projection and view transformation
		Matrix.multiplyMM(scratch1, 0, mProjectionMatrix, 0, mViewMatrix, 0);

		long time = SystemClock.uptimeMillis();
		float angle = 0.0150f * ((int) time);
		Matrix.setRotateM(mRotMatrixX, 0, angle * 1.000000f, 1.0f, 0.0f, 0.0f);
		Matrix.setRotateM(mRotMatrixY, 0, angle * 0.678957f, 0.0f, 1.0f, 0.0f);

		Matrix.multiplyMM(scratch2, 0, scratch1, 0, mRotMatrixX, 0);
		Matrix.multiplyMM(scratch3, 0, scratch2, 0, mRotMatrixY, 0);
		Matrix.scaleM(scratch4, 0, scratch3, 0, 0.4f, 0.4f, 0.4f);

/*
		float sinValue1 = (float) (Math.sin(angle * 0.017f) + 1) * 0.5f;
		float sinValue2 = (float) (Math.sin(angle * 0.027f) + 1) * 0.5f;
		float sinValue3 = (float) (Math.sin(angle * 0.043f) + 1) * 0.5f;

		mTriangle.setColor(0, 1.0f, 0.0f, sinValue1, 1.0f);
		mTriangle.setColor(1, sinValue2, 1.0f, 0.0f, 1.0f);
		mTriangle.setColor(2, 0.0f, sinValue3, 1.0f, 1.0f);
*/
		mCube.draw(scratch4);
		//mTriangle.draw(scratch4);

		// Draw shape
		//mTriangle.draw(mMVPMatrix);
	}

}

