<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Cooper</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="description" content="uploadPage">
	<script type="text/javascript">
	function analyse(){
		document.forms[0].action = "<%=request.getContextPath()%>/upload?command=analyse";
		document.forms[0].submit();
	}
	</script>
  </head>
  
  <body>
    	<form name="upform" method="POST" action="upload" enctype="multipart/form-data">
    	<div style="border: 1px solid black; color: black; background-color: rgb(230,230,230)">
    	分析的jar路径：<input type=file name=fileforload size=50/><u style="color: gray; font-size: 14">（只能上传一个jar文件）</u><br>
    	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    	组件信息：<input type="text" name="componentInfo" size=50 style="background-color: rgb(240,240,240)"/><u style="color: gray; font-size: 14">(组件名以“;”分开，组件名为所属包的公共前缀)</u>
    	<div align="center"><input type="button" onclick="analyse()" value=提交 name=提交/></div>
    	</div>
    	<br/>
    	<div style="border: 1px solid blue; color: blue; background-color: rgb(230,230,255)">
    	<li>一个程序员对代码结构的理解、运用和规划是“不断成长的”</li>
		<li>它是可以总结、交流的</li>
		<li>希望Cooper可以在这方面有所贡献</li>
		</div>
		<br/>
    	<div style="color: gray">更多内容请关注<a href="http://developer.neusoft.com/modules/newbb/viewforum.php?forum=121">该地址</a>
    	&nbsp;&nbsp;&nbsp;&nbsp;当前版本：1.2.0_02 <a href="version.html">ReleaseNote</a>&nbsp;&nbsp;&nbsp;&nbsp;有任何疑问请联系作者<a href="mailto:wangdg@neusoft.com">TSD-王德刚</a></div>
　		</form>
  </body>
</html>
