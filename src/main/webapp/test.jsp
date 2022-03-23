<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<base href="<%=basePath%>">


        $.ajax({
            url:".do",
            data:{
            },
            type:"",
            dataType:"",
            success:function(result){

            }
        })

<tr>
    <td><input type="checkbox" /></td>
    <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/clue/detail.jsp';">李四先生</a></td>
    <td>动力节点</td>
    <td>010-84846003</td>
    <td>12345678901</td>
    <td>广告</td>
    <td>zhangsan</td>
    <td>已联系</td>

    $("#cluePage").bs_pagination('getOption','currentPage'),
    $("#cluePage").bs_pagination('getOption','rowsPerPage')
</tr>