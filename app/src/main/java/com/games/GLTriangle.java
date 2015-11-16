package com.games;

import static android.opengl.GLES20.*;
import static com.games.Const.*;

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
		createBuffer(vertexCount, V_POSITION);
		createBuffer(vertexCount, V_COLOR);
	}

	public void setTrianglePositions(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3)
	{
		setPosition(0, x1, y1, z1);
		setPosition(1, x2, y2, z2);
		setPosition(2, x3, y3, z3);
	}

	public void setTriangleColors(float r1, float g1, float b1, float a1, float r2, float g2, float b2, float a2, float r3, float g3, float b3, float a3)
	{
		setColor(0, r1, g1, b1, a1);
		setColor(1, r2, g2, b2, a2);
		setColor(2, r3, g3, b3, a3);
	}

	public void setTriangleColors(float r1, float g1, float b1, float r2, float g2, float b2, float r3, float g3, float b3)
	{
		setColor(0, r1, g1, b1, 1.0f);
		setColor(1, r2, g2, b2, 1.0f);
		setColor(2, r3, g3, b3, 1.0f);
	}

	@Override
	public void init()
	{
		initShaders(mGlobals.glShaders.SHADER_MATRIX);
		initProgram();

		// Bind attributes
		glBindAttribLocation(mProgram, 0, "aPosition");
		glBindAttribLocation(mProgram, 1, "aColor");

		glLinkProgram(mProgram);
	}

	public void draw(float[] mvpMatrix)
	{
		aColorHandle = glGetAttribLocation(mProgram, "aColor");
		aPositionHandle = glGetAttribLocation(mProgram, "aPosition");

		glUseProgram(mProgram);

		glEnableVertexAttribArray(aPositionHandle);
		glEnableVertexAttribArray(aColorHandle);

		// Pass in the position information
		positionBuffer.position(0);
		glVertexAttribPointer(aPositionHandle, VALUES_PER_V_POSITION, GL_FLOAT, false, 0, positionBuffer);
		glEnableVertexAttribArray(aPositionHandle);

		// Pass in the color information
		colorBuffer.position(0);
		glVertexAttribPointer(aColorHandle, VALUES_PER_V_COLOR, GL_FLOAT, false, 0, colorBuffer);
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
