package io.github.phantamanta44.openar;

import io.github.phantamanta44.openar.game.map.MapFile;
import io.github.phantamanta44.openar.util.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		Aargon.init();
		if (args.length < 1) {
			System.out.println("Must supply a map file!");
			fail(1);
		}
		try (BufferedReader reader = new BufferedReader(new FileReader(new File(TextUtils.concat(args))))) {
			MapFile map = MapFile.parse(reader.lines().toArray(l -> new String[l]));
			Aargon.getInstance().setMap(map);
		} catch (IOException e) {
			Aargon.getLogger().error("Could not read map file!");
			e.printStackTrace();
			fail(1);
		}
	}

	public static void exitRequested() {
		fail(0);
	}

	private static void fail(int errorCode) {
		Aargon.onExit();
		Runtime.getRuntime().exit(errorCode);
	}

}
