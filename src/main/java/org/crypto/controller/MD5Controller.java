package org.crypto.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
public class MD5Controller {
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

//	public static void main(String[] args) throws Exception {
//		String textcomment = "textcomment";
//		MessageDigest md5 = MessageDigest.getInstance("MD5");
//		md5.update(textcomment.getBytes(CHARSET));
//		// BigInteger bi = new BigInteger(md5.digest());
//		System.out.println(new BigInteger(1, md5.digest()).toString(16));
//		// SPRING 自带
//		System.out.println(new BigInteger(1, DigestUtils.md5Digest(textcomment.getBytes(CHARSET))).toString(16));
//
//	}
}
