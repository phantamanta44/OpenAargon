#version 330

in vec2 position;
out vec4 pass_color;
uniform mat4 transformation;

void main() {
    gl_Position = transformation * vec4(position, 0.0f, 1.0f);
}