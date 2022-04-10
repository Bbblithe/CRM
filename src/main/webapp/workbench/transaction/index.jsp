<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

	<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
	<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

	<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
	<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
	<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
	<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
	<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

<script type="text/javascript">

	$(function(){
		pageList(1,5)

		$("#searchBtn").click(function (){
			$("#hide-clueSource").val($.trim($("#search-clueSource").val()));
			$("#hide-owner").val($.trim($("#search-owner").val()));
			$("#hide-stage").val($.trim($("#search-stage").val()));
			$("#hide-name").val($.trim($("#search-name").val()));
			$("#hide-customerName").val($.trim($("#search-customerName").val()));
			$("#hide-type").val($.trim($("#search-type").val()));
			$("#hide-contactName").val($.trim($("#search-contactName").val()));

			pageList($("#transactionPage").bs_pagination('getOption','currentPage'),
					$("#transactionPage").bs_pagination('getOption','rowsPerPage'));
		})

		$("#selectAll").click(function(){
			$("input[name=xz]").prop("checked",this.checked)
		})
		$("#transactionBody").on("click",$("input[name=xz]"),function (){
			$("#selectAll").prop("checked",$("input[name=xz]").length==$("input[name=xz]:checked").length)
		})

		$("#modifyBtn").click(function(){
			let $xz = $("input[name=xz]:checked");
			if($xz.length == 0){
				alert("请选择需要修改的记录");
			}else if($xz.length > 1){
				alert("请选择一条交易记录进行修改！");
			}else{
				window.location.href = "workbench/transaction/edit.do?id=" + $xz.val();
			}
		})
	});

	function pageList(pageNo,pageSize){

		$("#selectAll").prop("checked",false);

		$("#search-clueSource").val($.trim($("#hide-clueSource").val()));
		$("#search-owner").val($.trim($("#hide-owner").val()));
		$("#search-stage").val($.trim($("#hide-stage").val()));
		$("#search-name").val($.trim($("#hide-name").val()));
		$("#search-customerName").val($.trim($("#hide-customerName").val()));
		$("#search-type").val($.trim($("#hide-type").val()));
		$("#search-contactName").val($.trim($("#hide-contactName").val()));

		$.ajax({
			url:"workbench/transaction/pageList.do",
			data:{
				"pageNo":pageNo,
				"pageSize":pageSize,
				"source":$("#search-clueSource").val(),
				"owner":$("#search-owner").val(),
				"stage":$("#search-stage").val(),
				"name":$("#search-name").val(),
				"customerName":$("#search-customerName").val(),
				"type":$("#search-type").val(),
				"contactName":$("#search-contactName").val()
			},
			type:"get",
			dataType:"json",
			success:function(data){
				/*
					result:
					{"total":100,"transactionList":["线索1":{"id":adsfa,...},"线索2":{"":..}]}
					 result.
				 */
				let html = "";
				$.each(data.dataList,function(i,n){
					html += '<tr>'
					html += '	<td><input type="checkbox" name="xz" value="'+n.id+'"/></td>'
					html += '	<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/transaction/detail.do?id='+n.id+'\';">'+n.name+'</a></td>'
					html += '	<td>'+n.customerId+'</td>'
					html += '	<td>'+n.stage+'</td>'
					html += '	<td>'+n.type+'</td>'
					html += '	<td>'+n.owner+'</td>'
					html += '	<td>'+n.source+'</td>'
					html += '	<td>'+n.contactsId+'</td>'
					html += '</tr>'
				})

				$("#transactionBody").html(html);

				let totalPages = data.total%pageSize==0?data.total/pageSize:parseInt(data.total/pageSize)+1

				$("#transactionPage").bs_pagination({
					currentPage: pageNo, // 页码
					rowsPerPage: pageSize, // 每页显示的记录条数
					maxRowsPerPage: 20, // 每页最多显示的记录条数
					totalPages: totalPages, // 总页数
					totalRows: data.total, // 总记录条数

					visiblePageLinks: 3, // 显示几个卡片

					showGoToPage:true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,

					// 该回掉函数在，点击分页组件的时候触发的
					onChangePage : function(event, result){
						pageList(result.currentPage , result.rowsPerPage);
					}
				});
			}
		})
	}
</script>
</head>
<body>

	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>交易列表</h3>
			</div>
		</div>
	</div>
	
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">

		<input type="hidden" id="hide-clueSource">
		<input type="hidden" id="hide-owner">
		<input type="hidden" id="hide-stage">
		<input type="hidden" id="hide-name">
		<input type="hidden" id="hide-customerName">
		<input type="hidden" id="hide-type">
		<input type="hidden" id="hide-contactName">


		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" id="search-owner" type="text">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" id="search-name" type="text">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">客户名称</div>
				      <input class="form-control" id="search-customerName" type="text">
				    </div>
				  </div>
				  
				  <br>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">阶段</div>
					  <select class="form-control" id="search-stage">
					  	<option></option>
						  <c:forEach items="${stage}" var="s">
							  <option value="${s.value}">${s.text}</option>
						  </c:forEach>
					  </select>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">类型</div>
					  <select class="form-control" id="search-type">
					  	<option></option>
						  <c:forEach items="${transactionType}" var="tran">
							  <option value="${tran.value}">${tran.text}</option>
						  </c:forEach>
					  </select>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">来源</div>
				      <select class="form-control" id="search-clueSource">
						  <option></option>
						  <c:forEach items="${source}" var="s">
							  <option value="${s.value}">${s.text}</option>
						  </c:forEach>
						</select>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">联系人名称</div>
				      <input class="form-control" id="search-contactName" type="text">
				    </div>
				  </div>
				  
				  <button type="button" id="searchBtn" class="btn btn-default">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 10px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" onclick="window.location.href='workbench/transaction/add.do';"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="modifyBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="selectAll"/></td>
							<td>名称</td>
							<td>客户名称</td>
							<td>阶段</td>
							<td>类型</td>
							<td>所有者</td>
							<td>来源</td>
							<td>联系人名称</td>
						</tr>
					</thead>
					<tbody id="transactionBody">
					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 20px;">
				<div id="transactionPage"></div>
			</div>

		</div>
		
	</div>
</body>
</html>