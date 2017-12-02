package top.lrshuai.es.util;

import java.util.UUID;

public class Utils {

	public static String getUUID() {
		String str = UUID.randomUUID().toString();
		return str.substring(0, 8) + str.substring(9, 13) + str.substring(14, 18) + str.substring(19, 23)
				+ str.substring(24);
	}
}
