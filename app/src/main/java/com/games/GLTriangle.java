package com.games;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by piotr.plys on 2015-11-05.
 */
public class GLTriangle
{
	// number of coordinates per vertex in this array
	static final int COORDS_PER_VERTEX = 3;
	private final int mProgram;

	private final String vertexShaderCode = "attribute vec4 vPosition;" +
		"void main() {" +
		"  gl_Position = vPosition;" +
		"}";

	private final String fragmentShaderCode = "precision mediump float;" +
		"uniform vec4 vColor;" +
		"void main() {" +
		"  gl_FragColor = vColor;" +
		"}";
	private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
	private FloatBuffer vertexBuffer;
	private float triangleCoords[] = new float[9];
	private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
	private float color[] = new float[3 * 4];
	private int mPositionHandle;
	private int mColorHandle;

	public GLTriangle(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3)
	{
		triangleCoords[0] = x1;
		triangleCoords[1] = y1;
		triangleCoords[2] = z1;

		triangleCoords[0] = x2;
		triangleCoords[1] = y2;
		triangleCoords[2] = z2;

		triangleCoords[0] = x3;
		triangleCoords[1] = y3;
		triangleCoords[2] = z3;

		init();
		Utils.log("Triangle created " + triangleCoords.toString());

		int vertexShader = GLES20Renderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
		int fragmentShader = GLES20Renderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

		// create empty OpenGL ES Program
		mProgram = GLES20.glCreateProgram();

		// add the shaders to program
		GLES20.glAttachShader(mProgram, vertexShader);
		GLES20.glAttachShader(mProgram, fragmentShader);

		// create OpenGL ES program executable
		GLES20.glLinkProgram(mProgram);
		Utils.log("Shaders linked");
	}

	public void setColor(int i, float r, float g, float b, float a)
	{
		color[i * 4 + 0] = r;
		color[i * 4 + 1] = g;
		color[i * 4 + 2] = b;
		color[i * 4 + 3] = a;
	}

	public void setColor(int i, float r, float g, float b)
	{
		color[i * 4 + 0] = r;
		color[i * 4 + 1] = g;
		color[i * 4 + 2] = b;
		color[i * 4 + 3] = 1.0f;
	}

	public void setColors(float r1, float g1, float b1, float a1, float r2, float g2, float b2, float a2, float r3, float g3, float b3, float a3)
	{
		setColor(0, r1, g1, b1, a1);
		setColor(1, r2, g2, b2, a2);
		setColor(2, r3, g3, b3, a3);
	}

	public void setColors(float r1, float g1, float b1, float r2, float g2, float b2, float r3, float g3, float b3)
	{
		setColor(0, r1, g1, b1);
		setColor(1, r2, g2, b2);
		setColor(2, r3, g3, b3);
	}

	private void init()
	{
		// initialize vertex byte buffer for shape coordinates
		ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoords.length * 4);
		// use the device hardware's native byte order
		bb.order(ByteOrder.nativeOrder());

		// create a floating point buffer from the ByteBuffer
		vertexBuffer = bb.asFloatBuffer();
		// add the coordinates to the FloatBuffer
		vertexBuffer.put(triangleCoords);
		// set the buffer to read the first coordinate
		vertexBuffer.position(0);
	}

	public void draw()
	{
		// Add program to OpenGL ES environment
		GLES20.glUseProgram(mProgram);

		// get handle to vertex shader's vPosition member
		mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

		// Enable a handle to the triangle vertices
		GLES20.glEnableVertexAttribArray(mPositionHandle);

		// Prepare the triangle coordinate data
		GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);

		// get handle to fragment shader's vColor member
		mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

		// Set color for drawing the triangle
		GLES20.glUniform4fv(mColorHandle, 3, color, 0);

		// Draw the triangle
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

		// Disable vertex array
		GLES20.glDisableVertexAttribArray(mPositionHandle);

//		Utils.log("Drawing triangle");
	}
}
