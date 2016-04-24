package io.github.phantamanta44.openar.game.beam;

import io.github.phantamanta44.openar.game.Direction;

public class Beam {

	private BeamColor color;
	private Direction dir;

	public Beam(BeamColor color, Direction dir) {
		this.color = color;
		this.dir = dir;
	}

	public BeamColor getColor() {
		return color;
	}

	public void setColor(BeamColor color) {
		this.color = color;
	}

	public Direction getDir() {
		return dir;
	}

	public void setDir(Direction dir) {
		this.dir = dir;
	}

	public void merge(Beam beam) {
		if (beam.dir == dir) {
			color = beam.getColor().merge(color);
			beam.color = color;
		}
	}

	@Override
	public Beam clone() {
		return new Beam(color, dir);
	}


}
