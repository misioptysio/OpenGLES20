#VERTEX SIMPLE
	attribute vec4 aPosition;

	void main()
	{
		gl_Position = aPosition;
	}

#FRAGMENT SIMPLE
	precision mediump float;

	uniform vec4 uColor;

	void main()
	{
		gl_FragColor = uColor;
	}

#VERTEX MATRIX
	uniform mat4 uMVPMatrix;

	attribute vec4 aPosition;
	attribute vec4 aColor;
	attribute vec4 aNormal;

	varying vec4 vColor;
	varying vec4 vNormal;

	void main()
	{
		vColor = aColor;
		vNormal = aNormal;
		gl_Position = uMVPMatrix * aPosition;
	}

#FRAGMENT MATRIX
	precision mediump float;

	varying vec4 vColor;

	void main()
	{
		gl_FragColor = vColor;
	}

#VERTEX PHONG
	uniform mat4 uCameraMatrix;
	uniform vec3 uCameraPosition;
	uniform vec3 uLightPosition[LIGHT_COUNT];
	uniform mat4 uNormalMatrix;
	uniform mat4 uModelMatrix;

	attribute vec4 aPosition;
	attribute vec4 aColor;
	attribute vec4 aNormal;
	attribute vec2 aTexture;
	attribute vec4 aTangent;
	attribute vec4 aCotangent;

	varying vec4 vColor;
	varying vec3 vNormal;
	varying vec2 vTexture;
	varying vec3 vTangent;
	varying vec3 vCotangent;
	varying vec3 vCameraVector;
	varying vec3 vLightVector[LIGHT_COUNT];

	vec3 mNormal;
	vec4 mPosition;

	void main()
	{
		mPosition = uModelMatrix * aPosition;

		vNormal = mat3(uNormalMatrix) * aNormal.xyz;
		vColor = aColor;
		vTexture = aTexture;
		vTangent = mat3(uNormalMatrix) * aTangent.xyz;
		vCotangent = mat3(uNormalMatrix) * aCotangent.xyz;
		vCameraVector = uCameraPosition - mPosition.xyz;

		for (int i = 0; i < LIGHT_COUNT; i++)
			vLightVector[i] = uLightPosition[i] - mPosition.xyz;

		gl_Position = uCameraMatrix * mPosition;
	}

#FRAGMENT PHONG
	precision mediump float;

	uniform vec4 uLightColor[LIGHT_COUNT];
	uniform sampler2D uTextureColor;
	uniform sampler2D uTextureSpecular;
	uniform sampler2D uTextureNormal;

	varying vec3 vCameraVector;
	varying vec3 vLightVector[LIGHT_COUNT];

	varying vec4 vColor;
	varying vec3 vNormal;
	varying vec2 vTexture;
	varying vec3 vTangent;
	varying vec3 vCotangent;

	float mSpecularDot;
	vec4 mTextureColor;
	vec4 mTextureSpecular;
	vec4 mTextureNormal;

	void main()
	{
		vec4 diffuse = vec4(0.0, 0.0, 0.0, 0.0);
		vec4 specular = vec4(0.0, 0.0, 0.0, 1.0);

		mTextureNormal = texture2D(uTextureNormal, vTexture);
		vec3 normalDir = normalize(vNormal + (2.0*mTextureNormal.r-1.0) * vTangent + (2.0*mTextureNormal.g-1.0) * vCotangent);
		vec3 cameraDir = normalize(vCameraVector);

		for (int i = 0; i < LIGHT_COUNT; i++)
		{
			vec3 lightDir = normalize(vLightVector[i]);
			float mDiffuseDot = dot(normalDir, lightDir);

			diffuse += uLightColor[i] * clamp(mDiffuseDot, 0.0, 1.0);

			vec4 mSpecularColor = uLightColor[i];

			vec3 mHalfAngle = normalize(cameraDir + lightDir);
			mSpecularDot = dot(normalDir, mHalfAngle);

			//vec3 mReflected = 2.0 * normalDir * mDiffuseDot - lightDir;
			//mSpecularDot = dot(cameraDir, mReflected);

			specular += mSpecularColor * pow(clamp(mSpecularDot, 0.0, 1.0), 50.0);
		}

		mTextureColor = texture2D(uTextureColor, vTexture);
		mTextureSpecular = texture2D(uTextureSpecular, vTexture);
		gl_FragColor = clamp(vColor * diffuse * mTextureColor + specular * mTextureSpecular, 0.0, 1.0);
	}

