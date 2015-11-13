package com.games;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glBindAttribLocation;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glDisableVertexAttribArray;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;

/**
 * Created by piotr.plys on 2015-11-05.
 */
public class GLTriangle extends GLObject
{
	private int uMVPMatrixHandle;
	private int aPositionHandle;
	private int aColorHandle;
	private int uColorHandle;

	public GLTriangle()
	{
		vertexCount = 3;
		createVertexBuffer(vertexCount);
	}

	public void setPositions(float x1, float y1, float z1,  float x2, float y2, float z2,  float x3, float y3, float z3)
	{
		setPosition(0, x1, y1, z1);
		setPosition(1, x2, y2, z2);
		setPosition(2, x3, y3, z3);
	}

	public void setColors(float r1, float g1, float b1, float a1, float r2, float g2, float b2, float a2, float r3, float g3, float b3, float a3)
	{
		setColor(0, r1, g1, b1, a1);
		setColor(1, r2, g2, b2, a2);
		setColor(2, r3, g3, b3, a3);
	}

	public void setColors(float r1, float g1, float b1, float r2, float g2, float b2, float r3, float g3, float b3)
	{
		setColor(0, r1, g1, b1, 1.0f);
		setColor(1, r2, g2, b2, 1.0f);
		setColor(2, r3, g3, b3, 1.0f);
	}

	@Override
	public void init()
	{
		initShaders(mShader.SHADER_MATRIX);
		initProgram();

		// Bind attributes
		//glBindAttribLocation(mProgram, 0, "aPosition");
		//glBindAttribLocation(mProgram, 1, "aColor");

		glLinkProgram(mProgram);
	}

	public void draw(float[] mvpMatrix)
	{
		// get handle to vertex shader's aColor & aPosition members
		aColorHandle = glGetAttribLocation(mProgram, "aColor");
		aPositionHandle = glGetAttribLocation(mProgram, "aPosition");

		// Add program to OpenGL ES environment
		glUseProgram(mProgram);

		// Enable a handle to the triangle vertices
		glEnableVertexAttribArray(aPositionHandle);
		glEnableVertexAttribArray(aColorHandle);

		// Pass in the position information
		vertexBuffer.position(POSITION_OFFSET);
		glVertexAttribPointer(aPositionHandle, VALUES_PER_POSITION, GL_FLOAT, false, STRIDE, vertexBuffer);
		glEnableVertexAttribArray(aPositionHandle);

		// Pass in the color information
		vertexBuffer.position(COLOR_OFFSET);
		glVertexAttribPointer(aColorHandle, VALUES_PER_COLOR, GL_FLOAT, false, STRIDE, vertexBuffer);
		glEnableVertexAttribArray(aColorHandle);

		// get handle to fragment shader's vColor member
		//uColorHandle = glGetUniformLocation(mProgram, "uColor");

		// Set color for drawing the triangle
		//glUniform4fv(uColorHandle, 1, color, 0);

		// get handle to shape's transformation matrix
		uMVPMatrixHandle = glGetUniformLocation(mProgram, "uMVPMatrix");

		// Pass the projection and view transformation to the shader
		glUniformMatrix4fv(uMVPMatrixHandle, 1, false, mvpMatrix, 0);

		// Draw the triangle
		glDrawArrays(GL_TRIANGLES, 0, vertexCount);

		// Disable vertex array
		glDisableVertexAttribArray(aPositionHandle);
		glDisableVertexAttribArray(aColorHandle);
	}
}
