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