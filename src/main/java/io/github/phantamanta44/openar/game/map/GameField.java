package io.github.phantamanta44.openar.game.map;

import io.github.phantamanta44.openar.Aargon;
import io.github.phantamanta44.openar.game.beam.Beam;
import io.github.phantamanta44.openar.game.beam.BeamTile;
import io.github.phantamanta44.openar.game.beam.Direction;
import io.github.phantamanta44.openar.game.piece.*;
import io.github.phantamanta44.openar.render.Line4I;
import io.github.phantamanta44.openar.render.Quad8I;
import io.github.phantamanta44.openar.render.RenderManager;
import io.github.phantamanta44.openar.util.Array2D;
import io.github.phantamanta44.openar.util.math.FloatVector;
import io.github.phantamanta44.openar.util.math.IntVector;

public class GameField implements IGameField {

	private static final String EMPTY_SLOT_PATH = "texture/empty.png";

	private Array2D<BeamTile> beamGrid = new Array2D<>(20, 13);
	private Array2D<IGamePiece> pieceGrid = new Array2D<>(20, 13);
	private Array2D<Integer> rotGrid = new Array2D<>(20, 13);
	private Array2D<Integer> metaGrid = new Array2D<>(20, 13);
	private Array2D<Integer> intGrid = new Array2D<>(20, 13);
	private boolean goal = false, fail = false;

	public GameField() {
		pieceGrid.fill(() -> PieceRegistry.EMPTY);
		beamGrid.fill(BeamTile::new);
		rotGrid.fill(() -> 0);
		metaGrid.fill(() -> 0);
		intGrid.fill(() -> 0);
	}

	public void updateBeams() {
		beamGrid.forEach((b, c) -> b.clear());
		pieceGrid.forEach((p, c) -> {
			if (p instanceof ISourcePiece)
				((ISourcePiece)p).getSourceBeams(this, c, getRotation(c), getMeta(c)).forEach(b -> sourceBeam(b.clone(), c));
		});
		goal = true;
		fail = false;
		pieceGrid.forEach((p, c) -> {
			int rot = getRotation(c), meta = getMeta(c);
			if (p instanceof IGoalPiece)
				goal &= ((IGoalPiece)p).isGoalMet(this, c, rot, meta);
			if (p instanceof IFailPiece)
				fail |= ((IFailPiece)p).isFailing(this, c, rot, meta);
		});
		if (fail)
			Aargon.getLogger().info("SYSTEM FAILING");
		else if (goal)
			Aargon.getLogger().info("SYSTEM SOLVED"); // TODO Pass winning/failing state to Aargon and do something
	}

	private void sourceBeam(Beam beam, IntVector coords) {
		BeamTile tile = beamGrid.get(coords);
		Beam in = tile.getIn(beam.getDir()), out = tile.getOut(beam.getDir());
		if (out != null)
			out.merge(beam);
		else
			tile.putOut(beam);
		if (in != null)
			in.merge(beam);
		IntVector next = beam.getDir().offset(coords);
		if (next.getX() < 0 || next.getY() < 0
				|| next.getX() >= pieceGrid.getWidth() || next.getY() >= pieceGrid.getHeight())
			return;
		receiveBeam(beam, next, tile);
	}

	private void receiveBeam(Beam beam, IntVector coords, BeamTile from) {
		int rot = getRotation(coords), meta = getMeta(coords);
		IGamePiece piece = getPiece(coords);
		BeamTile beams = beamGrid.get(coords);
		Beam newBeam = new Beam(beam.getColor(), beam.getDir().getOpposite());
		beams.putIn(newBeam);
		Beam out = beams.getOut(beam.getDir().getOpposite());
		if (out != null)
			out.merge(newBeam);
		piece.getReflections(this, coords, rot, meta, new Beam(beam.getColor(), beam.getDir().getOpposite())).forEach(b -> sourceBeam(b, coords));
	}

	public void setPiece(IntVector coords, IGamePiece piece) {
		pieceGrid.set(coords, piece);
		rotGrid.set(coords, 0);
		metaGrid.set(coords, 0);
		intGrid.set(coords, 0);
	}

	public void setRotation(IntVector coords, int rotation) {
		rotGrid.set(coords, rotation);
	}

	public void setMeta(IntVector coords, int meta) {
		metaGrid.set(coords, meta);
	}

	public void setMutability(IntVector coords, int muta) {
		intGrid.set(coords, muta);
	}

	@Override
	public IGamePiece getPiece(IntVector coords) {
		return pieceGrid.get(coords);
	}

	@Override
	public int getRotation(IntVector coords) {
		return rotGrid.get(coords);
	}

	@Override
	public int getMeta(IntVector coords) {
		return metaGrid.get(coords);
	}

	@Override
	public BeamTile getBeams(IntVector coords) {
		return beamGrid.get(coords);
	}

	@Override
	public int getMutability(IntVector coords) {
		return intGrid.get(coords);
	}

	public synchronized void bufferRenders(float xSize, float ySize, float ratio) {
		int origX = (int)-xSize, origY = (int)(ySize / ratio);
		pieceGrid.forEach((p, c) -> {
			if (p != null) {
				int rot = getRotation(c), meta = getMeta(c);
				String path = p.getTexturePath(this, c, rot, meta);
				IntVector offset = p.getTextureOffset(this, c, rot, meta);
				RenderManager.bufferQuad(new Quad8I(path, origX + c.getX() * 103, origY - (c.getY() + 1) * 103, 103, 103, offset.getX(), offset.getY(), 32, 32));
			}
			else
				RenderManager.bufferQuad(new Quad8I(EMPTY_SLOT_PATH, origX + c.getX() * 103, origY - (c.getY() + 1) * 103, 103, 103, 0, 0, 32, 32));
		});
		beamGrid.forEach((b, c) -> {
			for (Direction dir : Direction.values()) {
				if (b.getOut(dir) != null) {
					FloatVector t = dir.offsetHalf(c);
					Line4I base = new Line4I(origX + c.getX() * 103 + 52, origY - (c.getY() + 1) * 103 + 52,
							(int)((float)origX + t.getX() * 103F + 52F), (int)((float)origY - (t.getY() + 1) * 103F + 52F),
							b.getOut(dir).getColor(), 0.3F, 1F);
					RenderManager.bufferLine(base);
					accompanyLines(base);
				} else if (b.getIn(dir) != null) {
					FloatVector t = dir.offsetHalf(c);
					Line4I base = new Line4I(origX + c.getX() * 103 + 52, origY - (c.getY() + 1) * 103 + 52,
							(int)((float)origX + t.getX() * 103F + 52F), (int)((float)origY - (t.getY() + 1) * 103F + 52F),
							b.getIn(dir).getColor(), 0.3F, 1F);
					RenderManager.bufferLine(base);
					accompanyLines(base);
				}
			}
		});
	}

	private void accompanyLines(Line4I base) {
		RenderManager.bufferLine(new Line4I(base.x1, base.y1, base.x2, base.y2, base.r, base.g, base.b, 0.1F, 3F));
		RenderManager.bufferLine(new Line4I(base.x1, base.y1, base.x2, base.y2, base.r, base.g, base.b, 0.1F, 5F));
		RenderManager.bufferLine(new Line4I(base.x1, base.y1, base.x2, base.y2, base.r, base.g, base.b, 0.1F, 7F));
	}

}
