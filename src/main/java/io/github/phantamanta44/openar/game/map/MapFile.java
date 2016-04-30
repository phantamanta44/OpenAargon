package io.github.phantamanta44.openar.game.map;

import io.github.phantamanta44.openar.Aargon;
import io.github.phantamanta44.openar.game.beam.BeamColor;
import io.github.phantamanta44.openar.game.piece.IGamePiece;
import io.github.phantamanta44.openar.game.piece.PieceRegistry;
import io.github.phantamanta44.openar.util.Array2D;

public class MapFile {

	private Array2D<IGamePiece> pieceGrid = new Array2D<>(20, 13);
	private Array2D<Integer> rotGrid = new Array2D<>(20, 13);
	private Array2D<Integer> metaGrid = new Array2D<>(20, 13);
	private Array2D<Integer> intGrid = new Array2D<>(20, 13);

	private MapFile() {
		rotGrid.fill(() -> 0);
		metaGrid.fill(() -> 0);
		intGrid.fill(() -> 0);
	}

	public static MapFile parse(String[] t) {
		MapFile map = new MapFile();
		try {
			for (int i = 0; i < 13; i++) {
				for (int j = 0; j < 20; j++) {
					char[] token = t[i].substring(j * 4, (j + 1) * 4).toCharArray();
					map.pieceGrid.set(j, i, PieceRegistry.getPiece(Character.toString(token[0])));
					if (token[1] != ' ')
						map.rotGrid.set(j, i, Integer.parseInt(Character.toString(token[1])));
					map.metaGrid.set(j, i, BeamColor.fromToken(Character.toString(token[2])).ordinal());
					map.intGrid.set(j, i, Mutability.fromToken(Character.toString(token[3])).bitmask());
				}
			}
		} catch (Exception e) {
			Aargon.getLogger().error("Map parsing failed!");
			e.printStackTrace();
		}
		return map;
	}

	public void populate(GameField f) {
		pieceGrid.forEach((p, c) -> {
			f.setPiece(c, p);
			f.setRotation(c, rotGrid.get(c));
			f.setMeta(c, metaGrid.get(c));
			f.setMutability(c, intGrid.get(c));
		});
		f.updateBeams();
	}

}
