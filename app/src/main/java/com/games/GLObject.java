package com.games;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCreateProgram;

/**
 * Created by piotr.plys on 2015-11-12.
 */
public abstract class GLObject
{
	protected static final int BYTES_PER_FLOAT = 4;
	protected static final short BYTES_PER_SHORT = 2;

	protected static final int VALUES_PER_POSITION = 3;
	protected static final int VALUES_PER_COLOR = 4;
	protected static final int VALUES_PER_NORMAL = 3;
	protected static final int VALUES_PER_TEXTURE = 2;
	protected static final int POSITION_OFFSET = 0;
	protected static final int COLOR_OFFSET = POSITION_OFFSET + VALUES_PER_POSITION;
	protected static final int NORMAL_OFFSET = COLOR_OFFSET + VALUES_PER_COLOR;

	protected static final int VALUES_PER_VERTEX = VALUES_PER_POSITION + VALUES_PER_COLOR + VALUES_PER_NORMAL;
	protected static final int STRIDE = VALUES_PER_VERTEX * BYTES_PER_FLOAT;

	protected int vertexCount = 3;

	protected GLShader mShader;
	protected GLMaterial mMaterial;
	protected FloatBuffer vertexBuffer;
	protected ShortBuffer drawListBuffer;

	protected int mProgram;
	protected int mVertexShader;
	protected int mFragmentShader;

	public void setShader(GLShader shader)
	{
		mShader = shader;
	};

	public void initShaders(int shaderID)
	{
		mVertexShader = GLRenderer.loadShader(GL_VERTEX_SHADER, mShader.getVertexShader(shaderID));
		mFragmentShader = GLRenderer.loadShader(GL_FRAGMENT_SHADER, mShader.getFragmentShader(shaderID));
	}

	public void initProgram()
	{
		mProgram = glCreateProgram();

		glAttachShader(mProgram, mVertexShader);
		glAttachShader(mProgram, mFragmentShader);
	}

	public void createVertexBuffer(int vertexCount)
	{
		// initialize vertex byte buffer for shape coordinates
		ByteBuffer bb = ByteBuffer.allocateDirect(vertexCount * VALUES_PER_VERTEX * BYTES_PER_FLOAT);
		// use the device hardware's native byte order
		bb.order(ByteOrder.nativeOrder());

		// create a floating point buffer from the ByteBuffer
		vertexBuffer = bb.asFloatBuffer();
		// set the buffer to read the first coordinate
		vertexBuffer.position(0);
	}

	public void createDrawListBuffer(int drawListCount)
	{
		// initialize vertex byte buffer for shape coordinates
		ByteBuffer bb = ByteBuffer.allocateDirect(drawListCount * VALUES_PER_VERTEX * BYTES_PER_SHORT);
		// use the device hardware's native byte order
		bb.order(ByteOrder.nativeOrder());

		// create a floating point buffer from the ByteBuffer
		vertexBuffer = bb.asFloatBuffer();
		// set the buffer to read the first coordinate
		vertexBuffer.position(0);
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

	public abstract void init();
}
