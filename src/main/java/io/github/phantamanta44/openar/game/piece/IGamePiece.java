package io.github.phantamanta44.openar.game.piece;

import io.github.phantamanta44.openar.game.beam.Beam;
import io.github.phantamanta44.openar.game.map.IGameField;
import io.github.phantamanta44.openar.util.math.IntVector;

import java.util.Collection;

public interface IGamePiece {

	String getName();

	String getToken();

	Collection<Beam> getReflections(IGameField field, IntVector coords, int rot, int meta, Beam in);

	String getTexturePath(IGameField field, IntVector coords, int rot, int meta);

	IntVector getTextureOffset(IGameField field, IntVector coords, int rot, int meta);

}
