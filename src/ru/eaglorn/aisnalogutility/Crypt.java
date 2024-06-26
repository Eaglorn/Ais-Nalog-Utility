package ru.eaglorn.aisnalogutility;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Crypt {
	private static String initVector = "kgsyrmgnbsdhyrkp";
	private static String key = "ngdteohqkslbhydm";
	private static String charset = "UTF-8";

	public static String decrypt(String encrypted) {
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(charset), "AES"), new IvParameterSpec(initVector.getBytes(charset)));
			return new String(cipher.doFinal(Base64.getDecoder().decode(encrypted)));
		} catch (Exception e) {
			StringWriter stack = new StringWriter();
			e.printStackTrace(new PrintWriter(stack));
			log.error(stack.toString());
		}
		return null;
	}

	public static String encrypt(String value) {
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(charset), "AES"), new IvParameterSpec(initVector.getBytes(charset)));
			return new String(Base64.getEncoder().encode(cipher.doFinal(value.getBytes())));
		} catch (Exception e) {
			StringWriter stack = new StringWriter();
			e.printStackTrace(new PrintWriter(stack));
			log.error(stack.toString());
		}
		return null;
	}

	private Crypt() {
		throw new IllegalStateException("Utility class");
	}
}
