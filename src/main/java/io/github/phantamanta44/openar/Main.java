package io.github.phantamanta44.openar;

import io.github.phantamanta44.openar.game.map.MapFile;
import io.github.phantamanta44.openar.util.TextUtils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Main {

	private static final File TG_DIR = getOsDependentTGPath();

	public static void main(String[] args) {
		Aargon.init();
		File file = null;
		if (args.length > 0)
			file = new File(TextUtils.concat(args));
		else
			file = showFileDialog();
		if (file == null) {
			Aargon.getLogger().error("No map file!");
			fail(1);
		}
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			MapFile map = MapFile.parse(reader.lines().toArray(String[]::new));
			Aargon.getInstance().setMap(map);
		} catch (IOException e) {
			Aargon.getLogger().error("Could not read map file!");
			e.printStackTrace();
			fail(1);
		}
		Aargon.getInstance().getWindowManager().show();
	}

	private static File showFileDialog() {
		JFileChooser dialog = new JFileChooser();
		dialog.setFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				return f.isDirectory() || (f.isFile() && f.getName().toLowerCase().endsWith(".map"));
			}

			@Override
			public String getDescription() {
				return "Aargon Deluxe Map File (*.map)";
			}
		});
		dialog.setMultiSelectionEnabled(false);
		if (TG_DIR != null && TG_DIR.exists())
			dialog.setCurrentDirectory(TG_DIR);
		dialog.setDialogTitle("Select a Map File");
		if (dialog.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
			return null;
		return dialog.getSelectedFile();
	}

	public static void exitRequested() {
		fail(0);
	}

	private static void fail(int errorCode) {
		Aargon.onExit();
		Runtime.getRuntime().exit(errorCode);
	}

	public static File getOsDependentTGPath() {
		String os = System.getProperty("os.name");
		if (os.startsWith("Windows"))
			return new File("C:\\Program Files (x86)\\Twilight");
		return null; // Aargon Deluxe is Windows-only at this point
	}

}
