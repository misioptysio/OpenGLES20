package com.games;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by piotr.plys on 2015-11-05.
 */
public class GLTriangle
{
	private FloatBuffer vertexBuffer;

	// number of coordinates per vertex in this array
	static final int COORDS_PER_VERTEX = 3;

	private float triangleCoords[] = new float[9];
	private float color[][] = new float[3][4];

	private void Init()
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

	public GLTriangle(float x1, float y1, float z1,  float x2, float y2, float z2,  float x3, float y3, float z3)
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

		Init();
	}

	public void setColor(int i, float r, float g, float b, float a)
	{
		color[i][0] = r;
		color[i][1] = g;
		color[i][2] = b;
		color[i][3] = a;
	}

	public void setColor(int i, float r, float g, float b)
	{
		color[i][0] = r;
		color[i][1] = g;
		color[i][2] = b;
		color[i][3] = 1.0f;
	}

	public void setColors(float r1, float g1, float b1, float a1, float r2, float g2, float b2, float a2,  float r3, float g3, float b3, float a3)
	{
		setColor(0, r1, g1, b1, a1);
		setColor(1, r2, g2, b2, a2);
		setColor(2, r3, g3, b3, a3);
	}

	public void setColors(float r1, float g1, float b1,  float r2, float g2, float b2,  float r3, float g3, float b3)
	{
		setColor(0, r1, g1, b1);
		setColor(1, r2, g2, b2);
		setColor(2, r3, g3, b3);
	}

	private final String vertexShaderCode =
		"attribute vec4 vPosition;" +
			"void main() {" +
			"  gl_Position = vPosition;" +
			"}";

	private final String fragmentShaderCode =
		"precision mediump float;" +
			"uniform vec4 vColor;" +
			"void main() {" +
			"  gl_FragColor = vColor;" +
			"}";
}
