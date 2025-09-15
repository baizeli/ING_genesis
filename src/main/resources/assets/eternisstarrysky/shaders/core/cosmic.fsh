#version 150

#define M_PI 3.1415926535897932384626433832795

// #moj_import <fog.glsl>

const int cosmiccount = 10;
const int cosmicoutof = 101;
const float lightmix = 0.15f;

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;

uniform float time;

uniform float yaw;
uniform float pitch;
uniform float externalScale;

uniform float opacity;
uniform int useType; // 0=紫色, 1=紫金配色, 2=极光配色, 3=蓝色

uniform mat2 cosmicuvs[cosmiccount];
uniform int currentTime;

in float vertexDistance;
in vec4 vertexColor;
in vec2 texCoord0;
in vec4 normal;
in vec3 fPos;

out vec4 fragColor;


vec4 linear_fog(vec4 inColor, float vertexDistance, float fogStart, float fogEnd, vec4 fogColor) {
	if (vertexDistance <= fogStart) {
		return inColor;
	}

	float fogValue = vertexDistance < fogEnd ? smoothstep(fogStart, fogEnd, vertexDistance) : 1.0;
	return vec4(mix(inColor.rgb, fogColor.rgb, fogValue * fogColor.a), inColor.a);
}

mat4 rotationMatrix(vec3 axis, float angle) {
	axis = normalize(axis);
	float s = sin(angle);
	float c = cos(angle);
	float oc = 1.0 - c;

	return mat4(
		oc * axis.x * axis.x + c,           oc * axis.x * axis.y - axis.z * s,  oc * axis.z * axis.x + axis.y * s,  0.0,
		oc * axis.x * axis.y + axis.z * s,  oc * axis.y * axis.y + c,           oc * axis.y * axis.z - axis.x * s,  0.0,
		oc * axis.z * axis.x - axis.y * s,  oc * axis.y * axis.z + axis.x * s,  oc * axis.z * axis.z + c,           0.0,
		0.0,                                0.0,                                0.0,                                1.0
	);
}

vec3 starGradient(float intensity, float pulse, int useType)
{
	vec3 color;

	if (useType == 0) // 紫色方案保持不变
	{
		const vec3 basePurple = vec3(0.4, 0.1, 0.6);
		const vec3 lightLavender = vec3(0.8, 0.7, 1.0);
		const vec3 whitePurple = vec3(0.95, 0.92, 1.0);

		if (intensity < 0.5)
		{
			color = mix(basePurple, lightLavender, intensity * 2.0);
		}
		else
		{
			color = mix(lightLavender, whitePurple, (intensity - 0.5) * 2.0);
		}

		// 脉动效果
		float pulseFactor = 0.6 + 0.4 * sin(pulse * M_PI * 4.0);
		color *= pulseFactor;
	}
	else if (useType == 1) // 紫金配色方案
	{
		const vec3 deepPurple = vec3(0.3, 0.1, 0.5);    // 深紫色
		const vec3 royalPurple = vec3(0.6, 0.2, 0.8);   // 皇家紫
		const vec3 brightGold = vec3(1.0, 0.8, 0.3);    // 明亮金色
		const vec3 pureGold = vec3(1.0, 0.9, 0.4);      // 纯金色

		// 创建从深紫到金色的渐变
		if (intensity < 0.25)
		{
			color = mix(deepPurple, royalPurple, intensity * 4.0);
		}
		else if (intensity < 0.6)
		{
			color = mix(royalPurple, brightGold, (intensity - 0.25) * 2.86);
		}
		else
		{
			color = mix(brightGold, pureGold, (intensity - 0.6) * 2.5);
		}

		// 脉动效果
		float pulseFactor = 0.6 + 0.4 * sin(pulse * M_PI * 4.0);
		color *= pulseFactor;
	}
	else if(useType == 3) //蓝色
	{
		const vec3 auroraBlue = vec3(0.3, 0.4, 1.0);
		const vec3 lightLavender = vec3(0.2, 0.7, 0.4);
		const vec3 whitePurple = vec3(0.45, 0.92, 1.0);

		if (intensity < 0.5)
		{
			color = mix(auroraBlue, lightLavender, auroraBlue);
		}
		else
		{
			color = mix(lightLavender, whitePurple, (intensity - 0.5) * 2.0);
		}

		// 脉动效果
		float pulseFactor = 0.6 + 0.4 * sin(pulse * M_PI * 4.0);
		color *= pulseFactor;
	}
	else if (useType == 2) // 极光配色方案
	{
		// 极光颜色定义
		const vec3 auroraGreen = vec3(0.2, 1.0, 0.4);     // 亮绿色
		const vec3 auroraCyan = vec3(0.1, 0.8, 1.0);      // 青色
		const vec3 auroraBlue = vec3(0.3, 0.4, 1.0);      // 蓝色
		const vec3 auroraPurple = vec3(0.8, 0.3, 1.0);    // 紫色
		const vec3 auroraWhite = vec3(0.9, 1.0, 0.95);    // 白色

		// 基于时间的动态颜色混合
		float timeOffset = time * 0.02 + pulse * 0.5;
		float colorPhase = mod(timeOffset, 4.0);

		// 在不同极光颜色间循环
		if (colorPhase < 1.0)
		{
			color = mix(auroraGreen, auroraCyan, colorPhase);
		}
		else if (colorPhase < 2.0)
		{
			color = mix(auroraCyan, auroraBlue, colorPhase - 1.0);
		}
		else if (colorPhase < 3.0)
		{
			color = mix(auroraBlue, auroraPurple, colorPhase - 2.0);
		}
		else
		{
			color = mix(auroraPurple, auroraGreen, colorPhase - 3.0);
		}

		// 根据强度添加白色光芒
		if (intensity > 0.7)
		{
			float whiteAmount = (intensity - 0.7) * 3.33;
			color = mix(color, auroraWhite, whiteAmount);
		}

		// 动态亮度变化（不是呼吸灯，而是流动效果）
		float flowFactor = 0.7 + 0.3 * sin(timeOffset * 2.0);
		color *= flowFactor;
	}
	else if (useType == 4)
	{
		color = vec3(1, 1, 1);
	}

	return color;
}

void main(void)
{
	vec4 mask = texture(Sampler0, texCoord0.xy);
	float oneOverExternalScale = 1.0 / externalScale;
	int uvtiles = 16;

	// === 背景渲染 ===
	vec4 col = vec4(0.0);

	if (useType == 1)
	{
		// 紫金背景 - 深紫到暗金的渐变
		float gradFactor = clamp(fPos.y * 0.1 + 0.3, 0.0, 1.0);
		vec3 darkPurple = vec3(0.15, 0.05, 0.25);    // 深紫色背景
		vec3 darkGold = vec3(0.4, 0.25, 0.1);        // 暗金色背景
		col.rgb = mix(darkPurple, darkGold, pow(gradFactor, 1.8));
	}
	else if (useType == 2)
	{
		// 极光背景 - 动态的极光天空
		vec3 auroraBase1 = vec3(0.15, 0.25, 0.4);     // 深蓝基色
		vec3 auroraBase2 = vec3(0.1, 0.4, 0.3);       // 深绿基色
		vec3 auroraBase3 = vec3(0.25, 0.15, 0.4);     // 深紫基色

		// 基于时间和位置的动态混合
		float timeFlow = time * 0.008;
		float positionFactor = (fPos.x * 0.02 + fPos.z * 0.015);
		float heightFactor = clamp(fPos.y * 0.08 + 0.4, 0.0, 1.0);

		// 创建波动效果
		float wave1 = sin(timeFlow + positionFactor) * 0.5 + 0.5;
		float wave2 = cos(timeFlow * 1.3 + positionFactor * 0.7) * 0.5 + 0.5;
		float wave3 = sin(timeFlow * 0.8 + positionFactor * 1.2) * 0.5 + 0.5;

		// 混合不同的极光基色
		vec3 mixed1 = mix(auroraBase1, auroraBase2, wave1);
		vec3 mixed2 = mix(mixed1, auroraBase3, wave2);

		// 添加高度渐变
		vec3 skyTop = vec3(0.3, 0.5, 0.7);  // 更亮的天空顶部
		col.rgb = mix(mixed2, skyTop, pow(heightFactor, 2.0));

		// 添加微妙的发光效果
		col.rgb += vec3(0.05, 0.08, 0.1) * wave3;
	}
	else if (useType == 3)
	{
		// 紫金背景 - 深紫到暗金的渐变
		float gradFactor = clamp(fPos.y * 0.1 + 0.3, 0.0, 1.0);
		vec3 darkPurple = vec3(0.05, 0.3, 0.2);    // 深紫色背景
		vec3 darkGold = vec3(0.05, 0.4, 0.3);        // 暗金色背景
		col.rgb = mix(darkPurple, darkGold, pow(gradFactor, 1.8));
	}
	else if (useType == 0)
	{
		col.rgb = vec3(0.08, 0.02, 0.12); // 紫色背景保持不变
	}
	else if (useType == 4)
	{
		/*
		int distance = abs(12000 - currentTime);
		float delta = distance / 12000.0F;
		float r = 0.3;
		float g = 0.3;
		float b = 1;
		r += (0.5 - 0.3) * delta;
		g -= 0.3 * delta;
		b -= delta;
		col.rgb = vec3(r, g, b);
		*/
		int dayTime = (currentTime + 18000) % 24000;
		if (dayTime >= 12000)
			col.rgb = vec3(0.5, 0, 0);
		else
			col.rgb = vec3(0.3, 0.3, 1);
	}

	col.a = 1.0;

	// 获取光线方向
	vec4 dir = normalize(vec4(-fPos, 0));

	// 应用旋转
	float sb = sin(pitch);
	float cb = cos(pitch);
	dir = normalize(vec4(dir.x, dir.y * cb - dir.z * sb, dir.y * sb + dir.z * cb, 0));

	float sa = sin(-yaw);
	float ca = cos(-yaw);
	dir = normalize(vec4(dir.z * sa + dir.x * ca, dir.y, dir.z * ca - dir.x * sa, 0));

	vec4 ray;

	// 绘制星星层
	for (int i = 0; i < 32; i++)
	{
		int mult = 32 - i;

		// 伪随机参数
		int j = i + 7;
		float rand1 = (j * j * 4321 + j * 8) * 2.0;
		int k = j + 1;
		float rand2 = (k * k * k * 239 + k * 37) * 3.6;
		float rand3 = rand1 * 347.4 + rand2 * 63.4;

		// 随机旋转
		vec3 axis = normalize(vec3(sin(rand1), sin(rand2), cos(rand3)));
		ray = dir * rotationMatrix(axis, mod(rand3, 2.0 * M_PI));

		// 计算UV
		float rawu = 0.5 + (atan(ray.z, ray.x) / (2.0 * M_PI));
		float rawv = 0.5 + (asin(ray.y) / M_PI);

		// UV缩放和时间偏移
		float scale = mult * 0.5 + 2.75;
		float u = rawu * scale * externalScale;
		float v = (rawv + time * 0.0002 * oneOverExternalScale) * scale * 0.6 * externalScale;

		vec2 tex = vec2(u, v);

		// 平铺位置
		int tu = int(mod(floor(u * uvtiles), uvtiles));
		int tv = int(mod(floor(v * uvtiles), uvtiles));

		// 伪随机变体
		int position = ((171 * tu) + (489 * tv) + (303 * (i + 31)) + 17209) ^ 10086;
		int symbol = int(mod(position, cosmicoutof));
		int rotation = int(mod(pow(tu, float(tv)) + tu + 3 + tv * i, 8));
		bool flip = false;
		if (rotation >= 4)
		{
			rotation -= 4;
			flip = true;
		}

		// 如果是星星图标
		if (symbol >= 0 && symbol < cosmiccount)
		{
			vec2 cosmictex = vec2(1.0, 1.0);
			vec4 tcol = vec4(1.0);

			// 获取平铺内的UV
			float ru = clamp(mod(u, 1.0) * uvtiles - tu, 0.0, 1.0);
			float rv = clamp(mod(v, 1.0) * uvtiles - tv, 0.0, 1.0);

			if (flip)
			{
				ru = 1.0 - ru;
			}

			float oru = ru;
			float orv = rv;

			// 旋转UV
			if (rotation == 1)
			{
				oru = 1.0 - rv;
				orv = ru;
			}
			else if (rotation == 2)
			{
				oru = 1.0 - ru;
				orv = 1.0 - rv;
			}
			else if (rotation == 3)
			{
				oru = rv;
				orv = 1.0 - ru;
			}

			// 获取图标UV
			float umin = cosmicuvs[symbol][0][0];
			float umax = cosmicuvs[symbol][1][0];
			float vmin = cosmicuvs[symbol][0][1];
			float vmax = cosmicuvs[symbol][1][1];

			cosmictex.x = umin * (1.0 - oru) + umax * oru;
			cosmictex.y = vmin * (1.0 - orv) + vmax * orv;

			tcol = texture(Sampler0, cosmictex);

			// 计算透明度
			float a = tcol.r * (0.8 + (1.0 / mult) * 1.0) * (1.0 - smoothstep(0.1, 0.4, abs(rawv - 0.5)));

			// 每颗星星独特的脉动节奏
			float starPulse = mod(time * 0.3 + rand3 * 0.1, 1.0);

			// 根据星星大小调整渐变强度
			float starSizeFactor = clamp(1.0 / (mult * 0.15), 0.3, 1.0);

			// 应用渐变 (传入useType)
			vec3 starColor = starGradient(starSizeFactor, starPulse, useType);

			// 添加叠加效果 - 增强发光效果
			col.rgb = mix(col.rgb, starColor, a * 0.9);
			col.rgb += starColor * a * 0.8; // 增加发光强度
		}
	}

	// 应用光照
	vec3 shade = vertexColor.rgb * lightmix + vec3(1.0 - lightmix);
	col.rgb *= shade;

	// 应用遮罩和透明度
	col.a *= mask.r * opacity;
	col = clamp(col, 0.0, 1.0);

	// 最终输出（带雾效）
	fragColor = linear_fog(col, vertexDistance, FogStart, FogEnd, FogColor);
}
