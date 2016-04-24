#version 330

out vec4 out_color;
uniform vec4 vert_color;

void main() {
    out_color = vert_color;
}