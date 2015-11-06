package com.games;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.opengl.GLUtils;
import android.graphics.drawable.Drawable;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by piotr.plys on 2015-10-27.
 */
public class GLTexturer
{
	static GL10 gl;
	public int[] textures = new int[10];

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
		gl.glGenTextures(1, textures, 0);
		//...and bind it to our array
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);

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
