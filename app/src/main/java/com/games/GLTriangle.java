package com.games;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

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
public class GLTriangle
{
	private static final int BYTES_PER_FLOAT = 4;
	private static final int VERTICES_COUNT = 3;

	static final int VALUES_PER_POSITION = 3;
	static final int VALUES_PER_COLOR = 4;
	static final int VALUES_PER_NORMAL = 3;
	static final int POSITION_OFFSET = 0;
	static final int COLOR_OFFSET = POSITION_OFFSET + VALUES_PER_POSITION;
	static final int NORMAL_OFFSET = COLOR_OFFSET + VALUES_PER_COLOR;

	private static final int VALUES_PER_VERTEX = VALUES_PER_POSITION + VALUES_PER_COLOR + VALUES_PER_NORMAL;
	private static final int STRIDE = VALUES_PER_VERTEX * BYTES_PER_FLOAT;
	private final int mProgram;

	private final String vertexShaderCode = "" +
		"uniform mat4 uMVPMatrix;" +
		"attribute vec4 aPosition;" +
		"attribute vec4 aColor;" +
		"varying vec4 vColor;" +
		"void main() {" +
		"  vColor = aColor;" +
		"  gl_Position = uMVPMatrix * aPosition;" +
		"}";

	private final String fragmentShaderCode = "" +
		"precision mediump float;" +
		"varying vec4 vColor;" +
		"void main() {" +
		"  gl_FragColor = vColor;" +
		"}";

	private int uMVPMatrixHandle;
	private FloatBuffer vertexBuffer;
	private int aPositionHandle;
	private int aColorHandle;
	private int uColorHandle;

	public GLTriangle()
	{
		init();
		Utils.log("Triangle byte buffer created ");

		int vertexShader = GLRenderer.loadShader(GL_VERTEX_SHADER, vertexShaderCode);
		int fragmentShader = GLRenderer.loadShader(GL_FRAGMENT_SHADER, fragmentShaderCode);

		mProgram = glCreateProgram();

		glAttachShader(mProgram, vertexShader);
		glAttachShader(mProgram, fragmentShader);

		// Bind attributes
		glBindAttribLocation(mProgram, 0, "aPosition");
		glBindAttribLocation(mProgram, 1, "aColor");

		glLinkProgram(mProgram);
		Utils.log("Shaders linked");
	}

	public void setPosition(int i, float x, float y, float z)
	{
		vertexBuffer.put(i * VALUES_PER_VERTEX + POSITION_OFFSET + 0, x);
		vertexBuffer.put(i * VALUES_PER_VERTEX + POSITION_OFFSET + 1, y);
		vertexBuffer.put(i * VALUES_PER_VERTEX + POSITION_OFFSET + 2, z);
	}

	public void setColor(int i, float r, float g, float b, float a)
	{
		vertexBuffer.put(i * VALUES_PER_VERTEX + COLOR_OFFSET + 0, r);
		vertexBuffer.put(i * VALUES_PER_VERTEX + COLOR_OFFSET + 1, g);
		vertexBuffer.put(i * VALUES_PER_VERTEX + COLOR_OFFSET + 2, b);
		vertexBuffer.put(i * VALUES_PER_VERTEX + COLOR_OFFSET + 3, a);
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

	private void init()
	{
		// initialize vertex byte buffer for shape coordinates
		ByteBuffer bb = ByteBuffer.allocateDirect(VERTICES_COUNT * VALUES_PER_VERTEX * BYTES_PER_FLOAT);
		// use the device hardware's native byte order
		bb.order(ByteOrder.nativeOrder());

		// create a floating point buffer from the ByteBuffer
		vertexBuffer = bb.asFloatBuffer();
		// set the buffer to read the first coordinate
		vertexBuffer.position(0);
	}

	public void draw(float[] mvpMatrix)
	{
		// get handle to vertex shader's vPosition member
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
		glDrawArrays(GL_TRIANGLES, 0, VERTICES_COUNT);

		// Disable vertex array
		glDisableVertexAttribArray(aPositionHandle);
		glDisableVertexAttribArray(aColorHandle);
	}
}
