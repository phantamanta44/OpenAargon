#version 330

in vec2 pass_tex_coord;
out vec4 out_color;
uniform sampler2D tex;

void main() {
    out_color = texture2D(tex, pass_tex_coord);
}