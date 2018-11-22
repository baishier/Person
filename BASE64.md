# Base64
#### 引言

  最近发现记忆力越来越差，所以需要记录下代码，能拿起来就用的那种。当前项目工程支持javascript和java之间相互转换，使用了url-safe（url安全）的模式。

#### Base64简介
  略

#### 代码实现

###### 1.代码结构

```
└─src
    ├─main
    │  ├─java
    │  │  └─org
    │  │      └─crypto
    │  │        └─controller
    │  │                BASE64Controller.java
    │  │                
    │  │                  
    │  ├─resources
    │  │  │  log4j2.xml
    │  │  │  
    │  │  └─context
    │  │          springmvc-servlet.xml
    │  │          
    │  └─webapp
    │      │  index.jsp
    │      │  
    │      ├─js
    │      │      base64.js
    │      │      jquery.min.js
    │      │      
    │      ├─jsp
    │      │      base64.jsp
    │      │      
    │      └─WEB-INF
    │              web.xml
    │              
    └─test
        ├─java
        └─resources

```


###### BASE64Controller.java
```java
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

```

###### log4j2.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>  
<!DOCTYPE xml>
<configuration status="OFF">
	<appenders>
		<Console name="Console" target="SYSTEM_OUT">
			<PatternLayout
				pattern="%d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n" />
		</Console>
	</appenders>
	<loggers>
		<root level="debug">
			<appender-ref ref="Console" />
		</root>
		<root name="org.person" level="trace" additivity="false">
			<appender-ref ref="Console" />
		</root>
	</loggers>
</configuration>  
```

######

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:context="http://www.springframework.org/schema/context"
	xmlns:mvc="http://www.springframework.org/schema/mvc"
	xsi:schemaLocation="http://www.springframework.org/schema/beans
				http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
				http://www.springframework.org/schema/context
				http://www.springframework.org/schema/context/spring-context-3.0.xsd
				http://www.springframework.org/schema/mvc
				http://www.springframework.org/schema/mvc/spring-mvc-3.0.xsd">

	<!-- 默认的注解映射的支持 -->
	<mvc:annotation-driven />

	<!-- 设置使用注解的类所在的jar包 ,当使用 <context:component-scan/> 后，就可以将 <context:annotation-config/>
		移除了 -->
	<context:component-scan base-package="org.crypto" />


	<!-- 完成请求和注解POJO的映射 -->
	<bean
		class="org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter" />

	<bean
		class="org.springframework.web.servlet.view.InternalResourceViewResolver">
		<property name="prefix" value="/jsp/" />
		<property name="suffix" value=".jsp" />
		<property name="viewClass"
			value="org.springframework.web.servlet.view.JstlView" />
	</bean>
	<mvc:annotation-driven />
	<!-- 对静态资源文件的访问 -->
	<mvc:default-servlet-handler />

	<bean id="multipartResolver"
		class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
		<!-- one of the properties available; the maximum file size in bytes -->
		<property name="maxUploadSize" value="1000000" />
	</bean>
	<!-- <import resource="spring-mybatis.xml" /> -->
</beans>
```
###### base64.js
```JavaScript
/*
 *  base64.js
 *
 *  Licensed under the BSD 3-Clause License.
 *    http://opensource.org/licenses/BSD-3-Clause
 *
 *  References:
 *    http://en.wikipedia.org/wiki/Base64
 */
;(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined'
        ? module.exports = factory(global)
        : typeof define === 'function' && define.amd
        ? define(factory) : factory(global)
}((
    typeof self !== 'undefined' ? self
        : typeof window !== 'undefined' ? window
        : typeof global !== 'undefined' ? global
: this
), function(global) {
    'use strict';
    // existing version for noConflict()
    var _Base64 = global.Base64;
    var version = "2.4.9";
    // if node.js and NOT React Native, we use Buffer
    var buffer;
    if (typeof module !== 'undefined' && module.exports) {
        try {
            buffer = eval("require('buffer').Buffer");
        } catch (err) {
            buffer = undefined;
        }
    }
    // constants
    var b64chars
        = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/';
    var b64tab = function(bin) {
        var t = {};
        for (var i = 0, l = bin.length; i < l; i++) t[bin.charAt(i)] = i;
        return t;
    }(b64chars);
    var fromCharCode = String.fromCharCode;
    // encoder stuff
    var cb_utob = function(c) {
        if (c.length < 2) {
            var cc = c.charCodeAt(0);
            return cc < 0x80 ? c
                : cc < 0x800 ? (fromCharCode(0xc0 | (cc >>> 6))
                                + fromCharCode(0x80 | (cc & 0x3f)))
                : (fromCharCode(0xe0 | ((cc >>> 12) & 0x0f))
                   + fromCharCode(0x80 | ((cc >>>  6) & 0x3f))
                   + fromCharCode(0x80 | ( cc         & 0x3f)));
        } else {
            var cc = 0x10000
                + (c.charCodeAt(0) - 0xD800) * 0x400
                + (c.charCodeAt(1) - 0xDC00);
            return (fromCharCode(0xf0 | ((cc >>> 18) & 0x07))
                    + fromCharCode(0x80 | ((cc >>> 12) & 0x3f))
                    + fromCharCode(0x80 | ((cc >>>  6) & 0x3f))
                    + fromCharCode(0x80 | ( cc         & 0x3f)));
        }
    };
    var re_utob = /[\uD800-\uDBFF][\uDC00-\uDFFFF]|[^\x00-\x7F]/g;
    var utob = function(u) {
        return u.replace(re_utob, cb_utob);
    };
    var cb_encode = function(ccc) {
        var padlen = [0, 2, 1][ccc.length % 3],
        ord = ccc.charCodeAt(0) << 16
            | ((ccc.length > 1 ? ccc.charCodeAt(1) : 0) << 8)
            | ((ccc.length > 2 ? ccc.charCodeAt(2) : 0)),
        chars = [
            b64chars.charAt( ord >>> 18),
            b64chars.charAt((ord >>> 12) & 63),
            padlen >= 2 ? '=' : b64chars.charAt((ord >>> 6) & 63),
            padlen >= 1 ? '=' : b64chars.charAt(ord & 63)
        ];
        return chars.join('');
    };
    var btoa = global.btoa ? function(b) {
        return global.btoa(b);
    } : function(b) {
        return b.replace(/[\s\S]{1,3}/g, cb_encode);
    };
    var _encode = buffer ?
        buffer.from && Uint8Array && buffer.from !== Uint8Array.from
        ? function (u) {
            return (u.constructor === buffer.constructor ? u : buffer.from(u))
                .toString('base64')
        }
        :  function (u) {
            return (u.constructor === buffer.constructor ? u : new  buffer(u))
                .toString('base64')
        }
        : function (u) { return btoa(utob(u)) }
    ;
    var encode = function(u, urisafe) {
        return !urisafe
            ? _encode(String(u))
            : _encode(String(u)).replace(/[+\/]/g, function(m0) {
                return m0 == '+' ? '-' : '_';
            }).replace(/=/g, '');
    };
    var encodeURI = function(u) { return encode(u, true) };
    // decoder stuff
    var re_btou = new RegExp([
        '[\xC0-\xDF][\x80-\xBF]',
        '[\xE0-\xEF][\x80-\xBF]{2}',
        '[\xF0-\xF7][\x80-\xBF]{3}'
    ].join('|'), 'g');
    var cb_btou = function(cccc) {
        switch(cccc.length) {
        case 4:
            var cp = ((0x07 & cccc.charCodeAt(0)) << 18)
                |    ((0x3f & cccc.charCodeAt(1)) << 12)
                |    ((0x3f & cccc.charCodeAt(2)) <<  6)
                |     (0x3f & cccc.charCodeAt(3)),
            offset = cp - 0x10000;
            return (fromCharCode((offset  >>> 10) + 0xD800)
                    + fromCharCode((offset & 0x3FF) + 0xDC00));
        case 3:
            return fromCharCode(
                ((0x0f & cccc.charCodeAt(0)) << 12)
                    | ((0x3f & cccc.charCodeAt(1)) << 6)
                    |  (0x3f & cccc.charCodeAt(2))
            );
        default:
            return  fromCharCode(
                ((0x1f & cccc.charCodeAt(0)) << 6)
                    |  (0x3f & cccc.charCodeAt(1))
            );
        }
    };
    var btou = function(b) {
        return b.replace(re_btou, cb_btou);
    };
    var cb_decode = function(cccc) {
        var len = cccc.length,
        padlen = len % 4,
        n = (len > 0 ? b64tab[cccc.charAt(0)] << 18 : 0)
            | (len > 1 ? b64tab[cccc.charAt(1)] << 12 : 0)
            | (len > 2 ? b64tab[cccc.charAt(2)] <<  6 : 0)
            | (len > 3 ? b64tab[cccc.charAt(3)]       : 0),
        chars = [
            fromCharCode( n >>> 16),
            fromCharCode((n >>>  8) & 0xff),
            fromCharCode( n         & 0xff)
        ];
        chars.length -= [0, 0, 2, 1][padlen];
        return chars.join('');
    };
    var atob = global.atob ? function(a) {
        return global.atob(a);
    } : function(a){
        return a.replace(/[\s\S]{1,4}/g, cb_decode);
    };
    var _decode = buffer ?
        buffer.from && Uint8Array && buffer.from !== Uint8Array.from
        ? function(a) {
            return (a.constructor === buffer.constructor
                    ? a : buffer.from(a, 'base64')).toString();
        }
        : function(a) {
            return (a.constructor === buffer.constructor
                    ? a : new buffer(a, 'base64')).toString();
        }
        : function(a) { return btou(atob(a)) };
    var decode = function(a){
        return _decode(
            String(a).replace(/[-_]/g, function(m0) { return m0 == '-' ? '+' : '/' })
                .replace(/[^A-Za-z0-9\+\/]/g, '')
        );
    };
    var noConflict = function() {
        var Base64 = global.Base64;
        global.Base64 = _Base64;
        return Base64;
    };
    // export Base64
    global.Base64 = {
        VERSION: version,
        atob: atob,
        btoa: btoa,
        fromBase64: decode,
        toBase64: encode,
        utob: utob,
        encode: encode,
        encodeURI: encodeURI,
        btou: btou,
        decode: decode,
        noConflict: noConflict,
        __buffer__: buffer
    };
    // if ES5 is available, make Base64.extendString() available
    if (typeof Object.defineProperty === 'function') {
        var noEnum = function(v){
            return {value:v,enumerable:false,writable:true,configurable:true};
        };
        global.Base64.extendString = function () {
            Object.defineProperty(
                String.prototype, 'fromBase64', noEnum(function () {
                    return decode(this)
                }));
            Object.defineProperty(
                String.prototype, 'toBase64', noEnum(function (urisafe) {
                    return encode(this, urisafe)
                }));
            Object.defineProperty(
                String.prototype, 'toBase64URI', noEnum(function () {
                    return encode(this, true)
                }));
        };
    }
    //
    // export Base64 to the namespace
    //
    if (global['Meteor']) { // Meteor.js
        Base64 = global.Base64;
    }
    // module.exports and AMD are mutually exclusive.
    // module.exports has precedence.
    if (typeof module !== 'undefined' && module.exports) {
        module.exports.Base64 = global.Base64;
    }
    else if (typeof define === 'function' && define.amd) {
        // AMD. Register as an anonymous module.
        define([], function(){ return global.Base64 });
    }
    // that's it!
    return {Base64: global.Base64}
}));

```

###### jquery.min.js
  略

###### base64.jsp
```HTML
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%
	String _base = request.getContextPath();
	request.setAttribute("_base", _base);

%>
<title>Base64</title>
<script src="${_base}/js/jquery.min.js"></script>
<!-- 关于base64.js，请前往github查看说明 ,base64.js文件中有github信息以及license信息-->
<script src="${_base}/js/base64.js"></script>

</head>
<body >
	<div style="width: 100%">
	需要加密的报文
	<input type="text" style="width: 100%" id="entext" value="this is the test text.这是测试文本。" >
	<input type="button" value="加密" id="encode">
	<br> 需要解密的报文
	<input type="text" style="width: 100%" id="detext" value="dGhpcyBpcyB0aGUgdGVzdCB0ZXh0Lui_meaYr-a1i-ivleaWh-acrOOAgg==">
	<input type="button" value="解密" id="decode">
	<br>
	<div id="en-result"></div>
	<div id="en-result-js"></div>
	<div id="de-result"></div>
	<div id="de-result-js"></div>
	</div>
</body>
<script type="text/javascript">
	var _base = "${_base}";

	$(document).ready(function() {

		$("#encode").on("click", function() {
			$.ajax({
				url : _base + "/base64/encode",
				data : {
					textcomment : $("#entext").val()
				},
				success : function(result) {
					$("#en-result").html("<strong>" + result + "</strong>");
				}
			});
			$("#en-result-js").html("<strong>" + Base64.encodeURI($("#entext").val()) + "</strong>");
		});
		$("#decode").on("click", function() {
			$.ajax({
				url : _base + "/base64/decode",
				data : {
					textcomment : $("#detext").val()
				},
				success : function(result) {
					$("#de-result").html("<strong>" + result.key + "</strong>");
				}
			});
			$("#de-result-js").html("<strong>" + Base64.decode($("#detext").val()) + "</strong>");
		});

	});
</script>
</html>
```

###### web.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns="http://java.sun.com/xml/ns/javaee"
	xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd"
	id="WebApp_ID" version="2.5">
	<display-name>person</display-name>

	<filter>
		<filter-name>encoding</filter-name>
		<filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
		<init-param>
			<param-name>encoding</param-name>
			<param-value>UTF-8</param-value>
		</init-param>
		<init-param>
			<param-name>forceEncoding</param-name>
			<param-value>true</param-value>
		</init-param>
	</filter>
	<filter-mapping>
		<filter-name>encoding</filter-name>
		<url-pattern>/*</url-pattern>
	</filter-mapping>

	<servlet>
		<servlet-name>springmvc</servlet-name>
		<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
		<init-param>
			<param-name>contextConfigLocation</param-name>
			<param-value>
                classpath*:context/springmvc-servlet.xml
            </param-value>
		</init-param>
		<load-on-startup>1</load-on-startup>
	</servlet>

	<servlet-mapping>
		<servlet-name>springmvc</servlet-name>
		<url-pattern>/</url-pattern>
	</servlet-mapping>

	<welcome-file-list>
		<welcome-file>/index.jsp</welcome-file>
	</welcome-file-list>
</web-app>
```
###### index.jsp
  略

最后给出github
https://github.com/baishier/Person
