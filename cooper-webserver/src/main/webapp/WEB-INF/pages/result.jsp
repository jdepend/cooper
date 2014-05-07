<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>分析结果</title>
	<meta http-equiv="description" content="分析结果">
	<script type="text/javascript" src="styles/js/sorttable.js"></script>
	<style type="text/css">
	body{
	    font-size:14px;
	}
	a{
	    color:black;
	}
	.content{
	    border: 1px solid black;
	}
	th, td {
  		padding: 3px !important;
	}
	
	/* Sortable tables */
	table.sortable thead {
	    background-color:#eee;
	    color:#666666;
	    font-weight: bold;
	    cursor: default;
	    font-size: 14;
	}
	table.sortable td {
	    font-size: 14;
	}
	</style>
</head>
<body>
<%Map<String, String> outputData = (Map<String, String>)request.getAttribute("outputData"); %>
<table width="100%" border="0">
  <tr>
    <td>
    <%int m = 0;%>
    <%for(String name : outputData.keySet()){ %>
    <%m++;%>
    <%String op = null; %>
    <a style="font-size: 16px; cursor: hand" onclick="<% for(int n = 1; n <= outputData.size(); n++){if(n==m){op = "block";} else {op = "none";} out.print("tab" + n + "c.style.display='" + op+ "';");}%>"><%=name%></a>
    <%}%>
    </td>
  </tr>
  <tr>
    <td class="content">
     	<%int i = 0; %>
	    <%for(String name : outputData.keySet()){ %>
	    <%i++;%>
        <div id="tab<%=i%>c" <%if(i!=1) out.print("style=\"display:none\"");%>>
            <p><%=outputData.get(name)%></p>
        </div>
        <%}%>
    </td>
  </tr>
</table>
</body>
</html>
