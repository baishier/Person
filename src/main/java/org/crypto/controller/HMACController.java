package org.crypto.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * jdk1.5+ & spring 4.x
 */
@RestController
@RequestMapping("/md5")
public class HMACController {
	public static final String CHARSET = "UTF-8";

	@CrossOrigin
	@RequestMapping(path = "/encode")
	public @ResponseBody String encode(@RequestParam("textcomment") String textcomment) {
		String entext = "";
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(textcomment.getBytes(CHARSET));
			entext = new BigInteger(1, md5.digest()).toString(16);
		} catch (NoSuchAlgorithmException e) {
			// won't happen
		} catch (UnsupportedEncodingException e) {
			// won't happen
		}
		return entext;
	}

	/**
	 * 初始化HMAC密钥
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String initMacKey() throws Exception {
		KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacMD5");

		SecretKey secretKey = keyGenerator.generateKey();
		return encryptBASE64(secretKey.getEncoded());
	}

	/**
	 * BASE64加密
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encryptBASE64(byte[] key) throws Exception {
		return (Base64.getEncoder().encodeToString(key));
	}

	/**
	 * BASE64解密
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptBASE64(String key) throws Exception {
		return (Base64.getDecoder().decode(key));
	}

	public static void main(String[] args) throws Exception {
		System.out.println();
		String key = initMacKey();

		SecretKey secretKey = new SecretKeySpec(decryptBASE64(key), "HmacMD5");
		Mac mac = Mac.getInstance(secretKey.getAlgorithm());
		mac.init(secretKey);
		byte[] hmac = mac.doFinal("aaaa".getBytes(CHARSET));
		System.out.println(new BigInteger(1, hmac).toString(16));
	}
}
