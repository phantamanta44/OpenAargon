package io.github.phantamanta44.openar.game.piece;

import io.github.phantamanta44.openar.game.beam.Beam;
import io.github.phantamanta44.openar.game.map.GameField;
import io.github.phantamanta44.openar.util.math.IntVector;

import java.util.Collection;

public interface IGamePiece {

	String getName();

	String getToken();

	Collection<Beam> getBeamsOut(GameField field, IntVector coords, int rot, int meta);

	String getTexturePath(GameField field, IntVector coords, int rot, int meta);

	IntVector getTextureOffset(GameField field, IntVector coords, int rot, int meta);

}
