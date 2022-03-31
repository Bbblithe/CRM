<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>


<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

<script type="text/javascript">
	$(function(){

		$(".time").datetimepicker({
			minView: "month",
			language:  'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,
			todayBtn: true,
			pickerPosition: "top-left"
		});

		$("#isCreateTransaction").click(function(){
			if(this.checked){
				$("#create-transaction2").show(200);
			}else{
				$("#create-transaction2").hide(200);
			}
		});

		// 来为放大镜图标来绑定事件，打开搜索市场活动的模态窗口
		$("#openSearchModalBtn").click(function (){
			$("#searchActivityModal").modal("show");
		})

		// 为搜索操作的模态窗口的搜索框绑定事件，搜索市场活动
		getActivityByName()
		$("#aname").keydown(function (event){
			if(event.keyCode==13){
				getActivityByName()
				return false;
			}
		})

		$("#convertBtn").click(function (){
			/*
				提交请求到后台，执行线索转换的操作，应该发出传统请求
				请求结束后，最终响应回线索列表页

				根据"为客户创建交易"的复选框有没有选中来判断是否创建交易
			 */
			if($("#isCreateTransaction").prop("checked")){
				// 如果需要创建交易，除了为后台传递clueId之外，还得为后台传递交易表单中的信息（金额，预计成交日期，交易名称，阶段，市场活动源(id)）
				<%--window.location.href = "workbench/clue/convert.do?clueId=${param.id}&money=";--%>
				// 以上传递参数的方式很麻烦，而且表单一旦扩充，挂载的参数有可能超出浏览器地址的上限
				// 因此选择使用表单的参数不用我们手动挂载（表单中使用name属性），提交表单能够提交post请求

				// 提交表单
				$("#tranForm").submit();
			}else{
				// 不需要创建交易的时候，传入一个clueId就可以了
				window.location.href = "workbench/clue/convert.do?clueId=${param.id}";
			}
		})
	});

	function getActivityByName(){
		$.ajax({
			url:"workbench/clue/getActivityListByName.do",
			data:{
				"aname":$.trim($("#aname").val())
			},
			type:"get",
			dataType:"json",
			success:function(result){
				let html = "";
				$.each(result,function (i,n){
					html += '<tr>'
					html += '	<td><input type="radio" onclick="saveValue(\''+n.id+'\')" name="xz"/></td>'
					html += '	<td id="'+n.id+'">'+n.name+'</td>'
					html += '	<td>'+n.startDate+'</td>'
					html += '	<td>'+n.endDate+'</td>'
					html += '	<td>'+n.owner+'</td>'
					html += '</tr>'
				})
				$("#activitySearchBody").html(html);
			}
		})
	}

	function saveValue(id){
		$("#activity").val($("#"+id).html());
		$("activityId").val(id);
		$("#searchActivityModal").modal("hide");
	}
</script>

</head>
<body>
	
	<!-- 搜索市场活动的模态窗口 -->
	<div class="modal fade" id="searchActivityModal" role="dialog" >
		<div class="modal-dialog" role="document" style="width: 90%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">搜索市场活动</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input type="text" id="aname" class="form-control" style="width: 300px;" placeholder="请输入市场活动名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>开始日期</td>
								<td>结束日期</td>
								<td>所有者</td>
								<td></td>
							</tr>
						</thead>
						<tbody id="activitySearchBody">
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>

	<div id="title" class="page-header" style="position: relative; left: 20px;">
		<!--
			el表达式为我们提供了N多个隐藏对象，
			只有xxxScope系列的隐含的对象可以省略掉
			其他所有的隐含域对象一概不能省略（如果省略掉了，会变成从域对象中取值）

		-->
		<h4>转换线索 <small>${param.fullname}${param.appellation}-${param.company}</small></h4>
	</div>
	<div id="create-customer" style="position: relative; left: 40px; height: 35px;">
		新建客户：${param.company}
	</div>
	<div id="create-contact" style="position: relative; left: 40px; height: 35px;">
		新建联系人：${param.fullname}${param.appellation}
	</div>
	<div id="create-transaction1" style="position: relative; left: 40px; height: 35px; top: 25px;">
		<input type="checkbox" id="isCreateTransaction"/>
		为客户创建交易
	</div>
	<div id="create-transaction2" style="position: relative; left: 40px; top: 20px; width: 80%; background-color: #F7F7F7; display: none;" >
	
		<form id="tranForm" action="workbench/clue/convert.do" method="post">
			<input type="hidden" name="flag" value="a"/>
			<input type="hidden" name="clueId" value="${param.id}">

		  <div class="form-group" style="width: 400px; position: relative; left: 20px;">
		    <label for="amountOfMoney">金额</label>
		    <input type="text" class="form-control" id="amountOfMoney" name="money">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="tradeName">交易名称</label>
		    <input type="text" class="form-control" id="tradeName" name="name">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="expectedClosingDate">预计成交日期</label>
		    <input type="text" class="form-control time" id="expectedClosingDate" name="expectedDate">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="stage">阶段</label>
		    <select id="stage"  class="form-control" name="stage">
		    	<option></option>
		    	<c:forEach items="${stage}" var="s">
					<option id="${s.value}">${s.text}</option>
				</c:forEach>
		    </select>
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="activity">市场活动源&nbsp;&nbsp;<a href="javascript:void(0);" id="openSearchModalBtn" style="text-decoration: none;"><span class="glyphicon glyphicon-search"></span></a></label>
		    <input type="text" class="form-control" id="activity" placeholder="点击上面搜索" readonly>
			  <input type="hidden" id="activityId" name="activityId">
		  </div>
		</form>
		
	</div>
	
	<div id="owner" style="position: relative; left: 40px; height: 35px; top: 50px;">
		记录的所有者：<br>
		<b>${param.owner}</b>
	</div>
	<div id="operation" style="position: relative; left: 40px; height: 35px; top: 100px;">
		<input class="btn btn-primary" type="button" id="convertBtn" value="转换">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<input class="btn btn-default" type="button" value="取消">
	</div>
</body>
</html>