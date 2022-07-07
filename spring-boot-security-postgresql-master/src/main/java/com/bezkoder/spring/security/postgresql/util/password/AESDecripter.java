package com.bezkoder.spring.security.postgresql.util.password;

import java.io.ByteArrayInputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.tomcat.util.codec.binary.Base64;

public class AESDecripter {

	private static String keyString = "adkj@#$02#@adflkj)(*jlj@#$#@LKjasdjlkj<.,mo@#$@#kljlkdsu343";

	public static Object decryptObject(String base64Data) throws Exception {
		// Decode the data
		byte[] encryptedData = Base64.decodeBase64(base64Data.getBytes());

		// Decode the Init Vector
		Cipher cipher1 = Cipher.getInstance("AES/CBC/PKCS5Padding");
		byte[] iv = new byte[16];
		new SecureRandom().nextBytes(iv);
		byte[] base64IV = Base64.encodeBase64(iv);
		String base64IVString = new String(base64IV);
		String urlEncodedIV = URLEncoder.encode(base64IVString, "UTF-8");
		byte[] rawIV = Base64.decodeBase64(urlEncodedIV.getBytes("UTF-8"));

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		Random rand = new SecureRandom();
		byte[] bytes = new byte[16];
		rand.nextBytes(bytes);
		IvParameterSpec ivSpec = new IvParameterSpec(bytes);
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		digest.update(keyString.getBytes());
		byte[] key = new byte[16];
		System.arraycopy(digest.digest(), 0, key, 0, key.length);
		SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
		cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

		byte[] decrypted = cipher.doFinal(encryptedData);

		ByteArrayInputStream stream = new ByteArrayInputStream(decrypted);
		ObjectInput in = new ObjectInputStream(stream);
		Object obj = null;
		try {
			obj = in.readObject();
		} finally {
			stream.close();
			in.close();
		}
		return obj;
	}

}
