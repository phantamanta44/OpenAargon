#version 330

in vec2 tex_coord;
in vec2 position;
out vec2 pass_tex_coord;
uniform mat4 transformation;

void main() {
    gl_Position = transformation * vec4(position, 0.0f, 1.0f);
    pass_tex_coord = tex_coord;
}