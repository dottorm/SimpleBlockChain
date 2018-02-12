package com.dottorsoft.SimpleBlockchain.main.util;

import java.util.Collection;
import java.util.stream.Stream;

public class StreamUtils {

	private StreamUtils() {
		throw new UnsupportedOperationException();
	}

	public static <T> Stream<T> streamOfNullable(Collection<T> list) {
		return list == null ? Stream.empty() : list.stream();
	}
}

