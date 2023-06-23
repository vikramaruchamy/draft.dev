package utils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Utils {
	
	private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();
	
	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = HEX_ARRAY[v >>> 4];
			hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
		}
		return new String(hexChars);
	}
	
	public static String createSignature(String endpoint, String payload, String epiKey)
			throws NoSuchAlgorithmException, InvalidKeyException {
		String algorithm = "HmacSHA256";
		SecretKeySpec secretKeySpec = new SecretKeySpec(epiKey.getBytes(), algorithm);
		Mac mac = Mac.getInstance(algorithm);
		mac.init(secretKeySpec);
		String data = endpoint + payload;
		return bytesToHex(mac.doFinal(data.getBytes()));
	}
}
