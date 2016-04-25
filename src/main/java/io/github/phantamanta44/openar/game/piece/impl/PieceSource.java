package io.github.phantamanta44.openar.game.piece.impl;

import io.github.phantamanta44.openar.game.beam.Direction;
import io.github.phantamanta44.openar.game.beam.Beam;
import io.github.phantamanta44.openar.game.beam.BeamColor;
import io.github.phantamanta44.openar.game.map.IGameField;
import io.github.phantamanta44.openar.game.piece.IGamePiece;
import io.github.phantamanta44.openar.game.piece.ISourcePiece;
import io.github.phantamanta44.openar.util.math.IntVector;

import java.util.Collection;
import java.util.Collections;

public class PieceSource implements IGamePiece, ISourcePiece {

	@Override
	public String getName() {
		return "Source";
	}

	@Override
	public String getToken() {
		return "S";
	}

	@Override
	public Collection<Beam> getReflections(IGameField field, IntVector coords, int rot, int meta, Beam in) {
		return Collections.emptyList();
	}

	@Override
	public Collection<Beam> getSourceBeams(IGameField field, IntVector coords, int rot, int meta) {
		return Collections.singletonList(new Beam(BeamColor.values()[meta], Direction.fromRotation(rot)));
	}

	@Override
	public String getTexturePath(IGameField field, IntVector coords, int rot, int meta) {
		return "texture/source.png";
	}

	@Override
	public IntVector getTextureOffset(IGameField field, IntVector coords, int rot, int meta) {
		return new IntVector((int)Math.floor((float)(System.currentTimeMillis() % 300L) / 75F) * 32, rot * 32);
	}

}
