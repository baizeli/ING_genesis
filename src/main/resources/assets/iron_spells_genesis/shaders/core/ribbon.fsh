#version 150

in vec4 vColor;
in vec2 vUV;

out vec4 fragColor;

uniform float Time;

void main() {

    // ===== 流动效果 =====
    float flow = fract(vUV.y - Time * 0.5);

    // ===== 渐变（中间亮，两边淡）=====
    float edge = abs(vUV.x - 0.5) * 2.0;
    float glow = smoothstep(1.0, 0.0, edge);

    // ===== 亮度控制 =====
    float intensity = glow * (1.0 - flow);

    vec3 color = vec3(0.2, 0.8, 1.0) * intensity;

    fragColor = vec4(color, intensity * vColor.a);
}