package io.github.phantamanta44.openar.game.piece;

import io.github.phantamanta44.openar.game.beam.Beam;
import io.github.phantamanta44.openar.game.map.IGameField;
import io.github.phantamanta44.openar.util.math.IntVector;

import java.util.Collection;

public interface ISourcePiece extends IGamePiece {

	Collection<Beam> getSourceBeams(IGameField field, IntVector coords, int rot, int meta);

}
