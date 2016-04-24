package io.github.phantamanta44.openar.util;

import java.io.*;
import java.net.URL;

public class IOUtils {

	public static String readText(File file) throws IOException {
		return readText(new FileReader(file));
	}

	public static String readText(URL url) throws IOException {
		return readText(new InputStreamReader(url.openStream()));
	}
	
	public static String readText(Reader reader) throws IOException {
		try (BufferedReader strIn = new BufferedReader(reader)) {
			return strIn.lines()
					.reduce((a, b) -> a.concat("\n").concat(b)).orElse("");
		}
	}
	
}
