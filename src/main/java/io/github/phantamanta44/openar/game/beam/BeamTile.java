package io.github.phantamanta44.openar.game.beam;

import io.github.phantamanta44.openar.game.Direction;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeamTile {

	private Map<Direction, Beam> in, out;

	public BeamTile() {
		in = new ConcurrentHashMap<>();
		out = new ConcurrentHashMap<>();
	}

	public Beam getIn(Direction dir) {
		return in.get(dir);
	}

	public Beam getOut(Direction dir) {
		return out.get(dir);
	}

	public void putIn(Beam beam) {
		in.put(beam.getDir(), beam);
	}

	public void putOut(Beam beam) {
		out.put(beam.getDir(), beam);
	}

	public void clear() {
		in.clear();
		out.clear();
	}
}