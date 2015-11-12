package com.games;

import android.content.Context;
import android.content.res.Resources;

/**
 * Created by piotr.plys on 2015-11-12.
 */
public class GLShader
{
	//
	public final int TYPE_VS = 0;
	public final int TYPE_FS = 1;

	public final int SHADER_SIMPLE = 0;
	public final int SHADER_MATRIX = 1;

	private String[][] shaderCode = new String[100][2];
	private final Resources resources;

	public GLShader(Context context)
	{
		resources = context.getResources();

		shaderCode[SHADER_SIMPLE][TYPE_VS] = resources.getString(R.string.vsSimple);
		shaderCode[SHADER_SIMPLE][TYPE_FS] = resources.getString(R.string.fsSimple);

		shaderCode[SHADER_SIMPLE][TYPE_VS] = resources.getString(R.string.vsMatrix);
		shaderCode[SHADER_SIMPLE][TYPE_FS] = resources.getString(R.string.fsMatrix);
	}

	public String getVertexShader(int id)
	{
		return shaderCode[id][TYPE_VS];
	}

	public String getFragmentShader(int id)
	{
		return shaderCode[id][TYPE_FS];
	}
}
