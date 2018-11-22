package org.crypto.controller;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * jdk1.8+ & spring 4.x
 */
@RestController
@RequestMapping("/base64")
public class BASE64Controller {

	public static final String CHARSET = "UTF-8";

	@CrossOrigin
	@RequestMapping(path = "/encode")
	public @ResponseBody String encode(@RequestParam("textcomment") String textcomment) {
		String entext = "";
		try {
			entext = new String(Base64.getUrlEncoder().encode(textcomment.getBytes(CHARSET)), CHARSET);
		} catch (UnsupportedEncodingException e) {
			// won't happen
		}
		return entext;
	}

	@CrossOrigin
	@RequestMapping(path = "/decode")
	public @ResponseBody Map<String, String> decode(@RequestParam("textcomment") String textcomment) {
		// 如果此方法直接返回String对象，会出现中文乱码问题。
		Map<String, String> result = new HashMap<>();
		String detext = "";
		try {
			detext = new String(Base64.getUrlDecoder().decode(textcomment), CHARSET);
		} catch (UnsupportedEncodingException e) {
			// won't happen
		}
		result.put("key", detext);
		return result;
	}

}
