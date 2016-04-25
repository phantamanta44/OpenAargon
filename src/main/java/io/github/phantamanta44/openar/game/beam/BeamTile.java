package io.github.phantamanta44.openar.game.beam;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeamTile {

	public static final BeamTile EMPTY = new BeamTile() {
		@Override
		public void clear() {
			throw new UnsupportedOperationException("Cannot modify immutable BeamTile!");
		}

		@Override
		public void putOut(Beam beam) {
			throw new UnsupportedOperationException("Cannot modify immutable BeamTile!");
		}

		@Override
		public void putIn(Beam beam) {
			throw new UnsupportedOperationException("Cannot modify immutable BeamTile!");
		}
	};

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
