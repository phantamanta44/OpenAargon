package io.github.phantamanta44.openar.util;

import java.util.Arrays;

public class TextUtils {

	public static String concat(String[] strings) {
		return concat(strings, " ");
	}

	public static String concat(String[] strings, String split) {
		return Arrays.stream(strings).reduce((a, b) -> a.concat(split).concat(b)).orElse("");
	}

}
