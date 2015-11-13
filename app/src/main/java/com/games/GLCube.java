package com.games;

import static android.opengl.GLES20.*;

/**
 * Created by piotr.plys on 2015-11-13.
 */
public class GLCube extends GLObject
{
	public GLCube()
	{
		vertexCount = 8;
		createVertexBuffer(vertexCount);
		createDrawListBuffer(12);

		float[] vertexArray =
		{
			-1.0f, -1.0f, 1.0f, //0
			1.0f, -1.0f, 1.0f, //1
			-1.0f, -1.0f, -1.0f, //2
			1.0f, -1.0f, -1.0f, //3

			-1.0f, 1.0f, 1.0f, //4
			1.0f, 1.0f, 1.0f, //5
			-1.0f, 1.0f, -1.0f, //6
			1.0f, 1.0f, -1.0f //7
		};

		int offset;
		for (int i=0; i < vertexCount; i++)
		{
			offset = i * VALUES_PER_POSITION;
			setPosition(i, vertexArray[offset + 0], vertexArray[offset + 0], vertexArray[offset + 2]);
		};

		short drawListArray[] =
		{
			0, 1, 5,  0, 5, 4, //+Z
			1, 3, 7,  1, 7, 5, //+X
			3, 2, 6,  3, 6, 7, //-Z
			2, 0, 4,  2, 4, 6, //-X
			2, 3, 1,  2, 1, 0, //-Y
			5, 7, 6,  5, 6, 4  //+Y
		};
		drawListBuffer.put(drawListArray);
	}

	@Override
	public void init()
	{
		initShaders(mShader.SHADER_MATRIX);
		initProgram();
	}

	public void draw(float[] mvpMatrix)
	{
		int uMVPMatrixHandle;
		int aPositionHandle;
		int aColorHandle;

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
		glDrawElements(GL_TRIANGLES, 0, vertexCount);
		glDrawElements(GL_TRIANGLES, drawListBuffer.array().length/BYTES_PER_SHORT, GL_UNSIGNED_SHORT, drawListBuffer);

		// Disable vertex array
		glDisableVertexAttribArray(aPositionHandle);
		glDisableVertexAttribArray(aColorHandle);
	}
}
