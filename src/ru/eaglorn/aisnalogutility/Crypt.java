package ru.eaglorn.aisnalogutility;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.val;

public class Crypt {
	private static @val Logger logger = LoggerFactory.getLogger(Crypt.class);
	
	private static @val String initVector = "kgsyrmgnbsdhyrkp";
	private static @val String key = "ngdteohqkslbhydm";
	
	private static @val String charset = "UTF-8";

	public static String decrypt(String encrypted) {
		try {
			IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(charset));
			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(charset), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

			byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));
			return new String(original);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex.getMessage());
		}

		return null;
	}

	public static String encrypt(String value) {
		try {
			IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(charset));
			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(charset), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

			byte[] encrypted = cipher.doFinal(value.getBytes());
			return new String(Base64.getEncoder().encode(encrypted));
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex.getMessage());
		}
		return null;
	}
}
