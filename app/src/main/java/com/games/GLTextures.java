package com.games;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by piotr.plys on 2015-10-27.
 */
public class GLTextures
{
	public final static int TEXTURE_MAX = 16;
	public final static int TEXTURE_COLOR = 0;
	public final static int TEXTURE_SPECULAR = 1;
	public final static int TEXTURE_NORMAL = 2;

	public final static int[] mTextureHandles = new int[TEXTURE_MAX];
	static GL10 gl;

	public GLTextures(final Context context)
	{
		mTextureHandles[TEXTURE_COLOR] = loadTexture(context, R.raw.texture_color);
		mTextureHandles[TEXTURE_SPECULAR] = loadTexture(context, R.raw.texture_specular);
		mTextureHandles[TEXTURE_NORMAL] = loadTexture(context, R.raw.texture_normal);
	}

	public int getTextureHandle(int index)
	{
		return mTextureHandles[index];
	}

	public static int loadTexture(final Context context, final int resourceID)
	{
		int[] mTextureHandle = new int[1];

		GLES20.glGenTextures(1, mTextureHandle, 0);

		if (mTextureHandle[0] != 0)
		{
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inScaled = false;

			// Read in the resource
			final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceID, options);

			// Bind to the texture in OpenGL
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureHandle[0]);

			// Set filtering
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

			// Load the bitmap into the bound texture.
			GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

			// Recycle the bitmap, since its data has been loaded into OpenGL.
			bitmap.recycle();
		}

		if (mTextureHandle[0] == 0)
		{
//			throw new RuntimeException("Error loading texture.");
		}

		return mTextureHandle[0];
	}

	public void gltDrawText(int x, int y, String text, int size)
	{
		// Create an empty, mutable bitmap
		Bitmap bitmap = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_4444);
		// get a canvas to paint over the bitmap
		Canvas canvas = new Canvas(bitmap);
		bitmap.eraseColor(0);

/*
		// get a background image from resources
		// note the image format must match the bitmap format
		Drawable background = context.getResources().getDrawable(R.drawable.background);
		background.setBounds(0, 0, 256, 256);
		background.draw(canvas); // draw the background to our bitmap
*/
		// Draw the text
		Paint textPaint = new Paint();
		textPaint.setTextSize(size);
		textPaint.setAntiAlias(true);
		textPaint.setARGB(0xff, 0x00, 0x00, 0x00);
		// draw the text centered
		canvas.drawText("Hello World", x, y, textPaint);

		//Generate one texture pointer...
		gl.glGenTextures(1, mTextureHandles, 0);
		//...and bind it to our array
		gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureHandles[0]);

		//Create Nearest Filtered Texture
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

		//Different possible texture parameters, e.g. GL10.GL_CLAMP_TO_EDGE
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);

		//Use the Android GLUtils to specify a two-dimensional texture image from our bitmap
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

		//Clean up
		bitmap.recycle();
	}
}
