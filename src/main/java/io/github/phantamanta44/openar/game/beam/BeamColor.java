package io.github.phantamanta44.openar.game.beam;

import java.util.Arrays;

public enum BeamColor {

	RED(0b100, 0.95F, 0F, 0.01F),
	GREEN(0b010, 0.02F, 0.95F, 0.04F),
	BLUE(0b001, 0.01F, 0F, 0.95F),
	CYAN(0b011, 0F, 0.97F, 0.94F),
	MAGENTA(0b101, 0.93F, 0F, 0.92F),
	YELLOW(0b110, 0.96F, 0.9F, 0F),
	WHITE(0b111, 0.99F, 0.99F, 0.99F),
	BLACK(0b000, 0F, 0F, 0F);

	private final int bitmask;
	private final float[] color;

	private BeamColor(int mask, float r, float g, float b) {
		this.bitmask = mask;
		this.color = new float[] {r, g, b};
	}

	public BeamColor merge(BeamColor color) {
		return forMask(color.bitmask | this.bitmask);
	}

	public BeamColor filter(BeamColor filter) {
		return forMask(filter.bitmask & this.bitmask);
	}

	public static BeamColor forMask(int mask) {
		return Arrays.stream(values())
				.filter(c -> c.bitmask == mask)
				.findAny().orElse(null);
	}

	public static BeamColor fromToken(String s) {
		switch (s.toLowerCase()) {
			case "r":
				return RED;
			case "g":
				return GREEN;
			case "b":
				return BLUE;
			case "c":
				return CYAN;
			case "m":
				return MAGENTA;
			case "y":
				return YELLOW;
			case "w":
			case "x":
				return WHITE;
			default:
				return BLACK;
		}
	}

	public float[] getColor() {
		return color;
	}
}
