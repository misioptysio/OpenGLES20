package com.games;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import static com.games.Utils.*;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;



/*
tutorials:
		http://developer.android.com/training/graphics/opengl/draw.html
		http://stackoverflow.com/questions/9371868/android-oncreate-is-not-being-called
		http://developer.android.com/reference/android/opengl/GLSurfaceView.Renderer.html

Matrices:
*   http://www.songho.ca/opengl/gl_transform.html

Shaders:
*   http://www.raywenderlich.com/70208/opengl-es-pixel-shaders-tutorial
*** http://joshbeam.com/articles/getting_started_with_glsl/
*** http://www.learnopengles.com/android-lesson-one-getting-started/
*** http://blog.shayanjaved.com/2011/03/13/shaders-android/
*   http://codeflow.org/entries/2010/dec/09/minecraft-like-rendering-experiments-in-opengl-4/#texturing
*   https://github.com/antonholmquist/opengl-es-2-0-shaders/tree/master/Shaders

Textures:
		http://stackoverflow.com/questions/1339136/draw-text-in-opengl-es-android

Depth of field / blur:
		http://androidworks-kea.blogspot.com/2013/10/developers-notes-iii-simple-dof-effect.html
		http://artmartinsh.blogspot.com/2010/02/glsl-lens-blur-filter-with-bokeh.html
		http://encelo.netsons.org/2008/03/23/i-love-depth-of-field/

*/

/**
 * Created by piotr.plys on 2015-10-26.
 */
public class GLRenderer implements GLSurfaceView.Renderer
{
	private final float[] mProjectionMatrix = new float[16];
	private final float[] mViewMatrix = new float[16];

	private float mAccelerationX;
	private float mAccelerationY;
	private int mAccelerationIndex = 0;
	private float mAccelerationAngle = 0;
	private float[][] mAcceleration = new float[10][2];

	private boolean mFirstDraw;
	private boolean mSurfaceCreated;
	private int mWidth;
	private int mHeight;
	private long mLastTime;
	private long mStartTime;
	private int mFPS;

	private GLCube mCube;
	private Globals mGlobals;

	public GLRenderer()
	{
		mFirstDraw = true;
		mSurfaceCreated = false;
		mWidth = -1;
		mHeight = -1;
		mStartTime = System.currentTimeMillis();
		mLastTime = mStartTime;
		mFPS = 0;
	}

	public void initOnce()
	{
		float[] mPos1 = {0.0f, 8.0f, 4.0f};
		float[] mPos2 = {0.0f, 0.0f, 4.0f};
		float[] mPos3 = {-8.0f, 0.0f, 2.0f};
		float[] mPos4 = {8.0f, 0.0f, 4.0f};

		float[] mCol1 = {1.0f, 1.0f, 1.0f, 1f};
		float[] mCol2 = {0.2f, 0.2f, 0.2f, 1f};
		float[] mCol3 = {0.4f, 0.2f, 0.2f, 1f};
		float[] mCol4 = {1.0f, 1.0f, 1.0f, 1f};

		GLLights glLights = mGlobals.getGlLights();
		glLights.addLight(mPos1, mCol1);
		glLights.addLight(mPos2, mCol2);
		glLights.addLight(mPos3, mCol3);
		glLights.addLight(mPos4, mCol4);

		mGlobals.setCameraPosition(new float[]{0.0f, 0.0f, 5.0f});
		mGlobals.setCameraLookAt(new float[]{0.0f, 0.0f, 0.0f});
		mGlobals.setCameraUp(new float[]{0.0f, 1.0f, 0.0f});

		glClearColor(0.3f, 0.3f, 0.3f, 1.0f);
		// Enable depth test
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		// Accept fragment if it closer to the camera than the former one
		GLES20.glDepthFunc(GLES20.GL_LESS);

		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

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

	public void setGravityValues(float[] values)
	{
		float x = values[0];
		float y = values[1];
		float tmpX;
		float tmpY;

		if (x < 0.0f)
		{
			tmpX = Utils.mapValues(x, -0.5f, -6.0f,  0.0f, 1.0f, Utils.INTERPOLATE_CUBIC);
		}
		else
		{
			tmpX = Utils.mapValues(x, 0.5f, 6.0f,  0.0f, -1.0f, Utils.INTERPOLATE_CUBIC);
		}

		if (y < 0.0f)
		{
			tmpY = Utils.mapValues(y, -0.1f, -6.0f,  0.0f, 1.0f, Utils.INTERPOLATE_CUBIC_SQUARED);
		}
		else
		{
			tmpY = Utils.mapValues(y, 0.1f, 6.0f,  0.0f, -1.0f, Utils.INTERPOLATE_CUBIC_SQUARED);
		}

		mAccelerationIndex++;
		if (mAccelerationIndex == mAcceleration.length)
			mAccelerationIndex = 0;
		mAcceleration[mAccelerationIndex][0] = tmpX;
		mAcceleration[mAccelerationIndex][1] = tmpY;

		tmpX = 0.0f;
		tmpY = 0.0f;
		for (int i = 0; i < mAcceleration.length; i++)
		{
			tmpX += mAcceleration[i][0];
			tmpY += mAcceleration[i][1];
		}
		mAccelerationX = tmpX / mAcceleration.length;
		mAccelerationY = tmpY / mAcceleration.length;
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		Utils.log("GLRenderer.onSurfaceCreated() Surface created");

		mSurfaceCreated = true;
		mWidth = -1;
		mHeight = -1;

		mGlobals = OpenGLApplication.initSGlobals();
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
//				Utils.log("Acceleration: " + String.format("[x: %2.4f, y: %2.4f]", mAccelerationX, mAccelerationY));
			}
		}

		if (mFirstDraw)
		{
			mFirstDraw = false;
		}
	}

	public void onDrawFrame(boolean firstDraw)
	{
		float[] mCameraMatrix = new float[16];

//    Utils.log("Frame drawn @" + getFPS() + "FPS");
		initFrame();
		float time = (System.currentTimeMillis() - mStartTime) * 0.001f;

		// Set the camera position (View matrix)
		setLookAt(mViewMatrix, mGlobals.getCameraPosition(), mGlobals.getCameraLookAt(), mGlobals.getCameraUp());

//		mCube.setRotation((float) Math.sin(time) * 30.0f, 1.0f, 0.0f, 0.0f, false);
//		mCube.setRotation(time * 50.4773f, 0.0f, 1.0f, 0.0f, true);

		mAccelerationAngle += mAccelerationX * 2.5f;
//		mAccelerationAngle = Math.max(-50.0f, Math.min(50.0f, mAccelerationAngle));
		mCube.setRotation(-mAccelerationY * 70.0f, 1.0f, 0.0f, 0.0f, false);
		mCube.setRotation(mAccelerationAngle, 0.0f, 1.0f, 0.0f, true);
		mCube.setScale(1.3f, 1.3f, 1.3f);
		//mCube.setTranslation((float) Math.cos(0.028f * angle), (float) Math.sin(0.023f * angle), 0.0f);

		Matrix.multiplyMM(mCameraMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

		mCube.draw(mCameraMatrix);
	}

}

// TODO: 2015-11-20 Materials with ambient, specular, diffuse
// TODO: 2015-11-20 Bumpmapping
// TODO: 2015-11-20 Transparency
// TODO: 2015-11-20 Perlin noise implementation

