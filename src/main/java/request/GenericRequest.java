package request;

import Utils.ByteUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class GenericRequest implements GenericRequestInterface {

	public String sha256Hash(String originalString) {
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA3-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		final byte[] hashBytes = digest.digest(originalString.getBytes(StandardCharsets.UTF_8));
		return ByteUtils.bytesToHex(hashBytes);
	}
}
