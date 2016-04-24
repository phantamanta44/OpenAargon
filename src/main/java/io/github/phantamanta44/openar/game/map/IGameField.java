package io.github.phantamanta44.openar.game.map;

import io.github.phantamanta44.openar.game.beam.BeamTile;
import io.github.phantamanta44.openar.game.piece.IGamePiece;
import io.github.phantamanta44.openar.util.math.IntVector;

public interface IGameField {

	IGamePiece getPiece(IntVector coords);

	int getRotation(IntVector coords);

	int getMeta(IntVector coords);

	BeamTile getBeams(IntVector coords);

	int getMutability(IntVector coords);

}
