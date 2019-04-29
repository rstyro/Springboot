package top.lrshuai.googlecheck.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class GoogleAuthenticator {
	public static String getRandomSecretKey() {
		SecureRandom random = new SecureRandom();
		byte[] bytes = new byte[20];
		random.nextBytes(bytes);
		Base32 base32 = new Base32();
		String secretKey = base32.encodeToString(bytes);
		// make the secret key more human-readable by lower-casing and
		// inserting spaces between each group of 4 characters
		return secretKey.toUpperCase(); // .replaceAll("(.{4})(?=.{4})", "$1 ");
	}

	public static String getTOTPCode(String secretKey) {
		String normalizedBase32Key = secretKey.replace(" ", "").toUpperCase();
		Base32 base32 = new Base32();
		byte[] bytes = base32.decode(normalizedBase32Key);
		String hexKey = Hex.encodeHexString(bytes);
		long time = (System.currentTimeMillis() / 1000) / 30;
		String hexTime = Long.toHexString(time);
		return TOTP.generateTOTP(hexKey, hexTime, "6");
	}

	public static String getGoogleAuthenticatorBarCode(String secretKey,
			String account, String issuer) {
		String normalizedBase32Key = secretKey.replace(" ", "").toUpperCase();
		try {
			return "otpauth://totp/"
					+ URLEncoder.encode(issuer + ":" + account, "UTF-8")
							.replace("+", "%20")
					+ "?secret="
					+ URLEncoder.encode(normalizedBase32Key, "UTF-8").replace(
							"+", "%20") + "&issuer="
					+ URLEncoder.encode(issuer, "UTF-8").replace("+", "%20");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}

	public static void createQRCode(String barCodeData, String filePath,
			int height, int width) throws WriterException, IOException {
		BitMatrix matrix = new MultiFormatWriter().encode(barCodeData,
				BarcodeFormat.QR_CODE, width, height);
		try (FileOutputStream out = new FileOutputStream(filePath)) {
			MatrixToImageWriter.writeToStream(matrix, "png", out);
		}
	}

	static int window_size = 3; // default 3 - max 17 (from google docs)最多可偏移的时间

	/**
	 * set the windows size. This is an integer value representing the number of
	 * 30 second windows we allow The bigger the window, the more tolerant of
	 * clock skew we are.
	 * 
	 * @param s
	 *            window size - must be >=1 and <=17. Other values are ignored
	 */
	public static void setWindowSize(int s) {
		if (s >= 1 && s <= 17)
			window_size = s;
	}

	/**
	 * Check the code entered by the user to see if it is valid
	 * 
	 * @param secret
	 *            The users secret.
	 * @param code
	 *            The code displayed on the users device
	 * @param timeMsec
	 *            The time in msec (System.currentTimeMillis() for example)
	 * @return
	 */
	public static boolean check_code(String secret, long code, long timeMsec) {
		Base32 codec = new Base32();
		byte[] decodedKey = codec.decode(secret);
		// convert unix msec time into a 30 second "window"
		// this is per the TOTP spec (see the RFC for details)
		long t = (timeMsec / 1000L) / 30L;
		// Window is used to check codes generated in the near past.
		// You can use this value to tune how far you're willing to go.
		for (int i = -window_size; i <= window_size; ++i) {
			long hash;
			try {
				hash = verify_code(decodedKey, t + i);
			} catch (Exception e) {
				// Yes, this is bad form - but
				// the exceptions thrown would be rare and a static
				// configuration problem
				// e.printStackTrace();
				throw new RuntimeException(e.getMessage());
				// return false;
			}
			if (hash == code) {
				return true;
			}
		}
		// The validation code is invalid.
		return false;
	}

	private static int verify_code(byte[] key, long t)
			throws NoSuchAlgorithmException, InvalidKeyException {
		byte[] data = new byte[8];
		long value = t;
		for (int i = 8; i-- > 0; value >>>= 8) {
			data[i] = (byte) value;
		}
		SecretKeySpec signKey = new SecretKeySpec(key, "HmacSHA1");
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(signKey);
		byte[] hash = mac.doFinal(data);
		int offset = hash[20 - 1] & 0xF;
		// We're using a long because Java hasn't got unsigned int.
		long truncatedHash = 0;
		for (int i = 0; i < 4; ++i) {
			truncatedHash <<= 8;
			// We are dealing with signed bytes:
			// we just keep the first byte.
			truncatedHash |= (hash[offset + i] & 0xFF);
		}
		truncatedHash &= 0x7FFFFFFF;
		truncatedHash %= 1000000;
		return (int) truncatedHash;
	}
}
