<%@ page import="java.util.Map" %>
<%@ page import="java.util.Set" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	Map<String,String> pMap = (Map<String, String>) application.getAttribute("pMap");
	Set<String> set =  pMap.keySet();
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
	<script type="text/javascript" src="jquery/bs_typeahead/bootstrap3-typeahead.min.js"></script>

	<script type="text/javascript">

		let json = {
			<%
				for(String key:set){
					String value = pMap.get(key);
			%>
			"<%=key%>":<%=value%>,
			<%
				}
			%>
		}

		$(function(){
			$("#edit-customerName").typeahead({
				source: function (query, process) {
					$.get(
							"workbench/transaction/getCustomerName.do",
							{ "name" : query },
							function (data) {
								//alert(data);
								process(data);
							},
							"json"
					);
				},
				delay: 100
			});

			$(".time1").datetimepicker({
				minView: "month",
				language:  'zh-CN',
				format: 'yyyy-mm-dd',
				autoclose: true,
				todayBtn: true,
				pickerPosition: "bottom-left"
			});

			$(".time2").datetimepicker({
				minView: "month",
				language:  'zh-CN',
				format: 'yyyy-mm-dd',
				autoclose: true,
				todayBtn: true,
				pickerPosition: "top-left"
			});
			selectRightOne();
			$("#aName").keydown(function (event){
				if(event.keyCode == 13){
					getActivity();
					return false;
				}
			})

			$("#cName").keydown(function (event){
				if(event.keyCode == 13){
					getContacts();
					return false;
				}
			})

			$("#connectABtn").click(function (){
				getActivity()
				$("#findMarketActivity").modal("show");
			})

			$("#connectCBtn").click(function (){
				getContacts();
				$("#findContacts").modal("show");
			})

			$("#updateBtn").click(function(){
				// 发出传统请求，提交表单
				$("#tranForm").submit();
			})
		})

		function selectRightOne(){
			$("#edit-transactionStage").val("${t.stage}")
			$("#edit-transactionType").val('${t.type}')
			$("#edit-clueSource").val('${t.source}')
			let possibility = json['${t.stage}'];
			$("#edit-possibility").val(possibility)
		}

		function getActivity(){
			$.ajax({
				url:"workbench/transaction/associateActivity.do",
				data:{
					"name":$.trim($("#aName").val())
				},
				type:"get",
				dataType:"json",
				success:function(result){
					let html = ""
					$.each(result,function (i,n){
						html += '<tr>'
						html += '	<td><input type="radio" name="activity" onclick="saveAValue(\''+n.id+'\')"/></td>'
						html += '	<td id="'+n.id+'">'+n.name+'</td>'
						html += '	<td>'+n.startDate+'</td>'
						html += '	<td>'+n.endDate+'</td>'
						html += '	<td>'+n.owner+'</td>'
						html += '</tr>'
					})

					$("#activityList").html(html);
				}
			})
		}

		function getContacts(){
			$.ajax({
				url:"workbench/transaction/associateContacts.do",
				data:{
					"name":$.trim($("#cName").val())
				},
				type:"get",
				dataType:"json",
				success:function(result){
					let html = ""
					$.each(result,function (i,n){
						html += '<tr>'
						html += '	<td><input type="radio" onclick="saveCValue(\''+n.id+'\')" /></td>'
						html += '	<td id="'+n.id+'">'+n.fullname+'</td>'
						html += '	<td>'+n.email+'</td>'
						html += '	<td>'+n.mphone+'</td>'
						html += '</tr>'
					})

					$("#contactsList").html(html);
				}
			})
		}

		function saveAValue(id){
			$("#create-activitySrc").val($("#"+id).html());
			$("#activityId").val(id);
			$("#findMarketActivity").modal("hide");
		}

		function saveCValue(id){
			$("#create-contactsName").val($("#"+id).html());
			$("#contactsId").val(id);
			$("#findContacts").modal("hide");
		}
	</script>
</head>
<body>

	<!-- 查找市场活动 -->	
	<div class="modal fade" id="findMarketActivity" role="dialog">
		<div class="modal-dialog" role="document" style="width: 80%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">查找市场活动</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input type="text" class="form-control" id="aName" style="width: 300px;" placeholder="请输入市场活动名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable4" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>开始日期</td>
								<td>结束日期</td>
								<td>所有者</td>
							</tr>
						</thead>
						<tbody id="activityList">
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>

	<!-- 查找联系人 -->	
	<div class="modal fade" id="findContacts" role="dialog">
		<div class="modal-dialog" role="document" style="width: 80%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">查找联系人</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input type="text" class="form-control" id="cName" style="width: 300px;" placeholder="请输入联系人名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>邮箱</td>
								<td>手机</td>
							</tr>
						</thead>
						<tbody id="contactsList">
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	
	
	<div style="position:  relative; left: 30px;">
		<h3>更新交易</h3>
	  	<div style="position: relative; top: -40px; left: 70%;">
			<button type="button" class="btn btn-primary" id="updateBtn">更新</button>
			<button type="button" class="btn btn-default">取消</button>
		</div>
		<hr style="position: relative; top: -40px;">
	</div>
	<form class="form-horizontal" action="workbench/transaction/update.do" id="tranForm" method="post" role="form" style="position: relative; top: -30px;">
		<div class="form-group">
			<label for="edit-transactionOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="edit-transactionOwner" name="owner">
				  <option selected value="${t.owner}">${user.name}</option>
					<c:forEach items="${userList}" var="u">
						<option value="${u.id}">${u.name}</option>
					</c:forEach>
				</select>
			</div>
			<label for="edit-amountOfMoney" class="col-sm-2 control-label">金额</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="hidden" value="${t.id}" name="id">
				<input type="text" class="form-control" id="edit-amountOfMoney" name="money" value="${t.money}">
			</div>
		</div>
		
		<div class="form-group">
			<label for="edit-transactionName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="edit-transactionName" name="name" value="${t.name}">
			</div>
			<label for="edit-expectedClosingDate" class="col-sm-2 control-label">预计成交日期<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control time1" readonly id="edit-expectedClosingDate" name="expectedDate" value="${t.expectedDate}">
			</div>
		</div>
		
		<div class="form-group">
			<label for="edit-customerName" class="col-sm-2 control-label">客户名称<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="edit-customerName" name="customerName" value="${t.customerId}" placeholder="支持自动补全，输入客户不存在则新建">
			</div>
			<label for="edit-transactionStage" class="col-sm-2 control-label">阶段<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
			  <select class="form-control" id="edit-transactionStage" name="stage">
				  <c:forEach items="${stage}" var="s">
					  <option value="${s.value}">${s.text}</option>
				  </c:forEach>
			  </select>
			</div>
		</div>
		
		<div class="form-group">
			<label for="edit-transactionType" class="col-sm-2 control-label">类型</label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="edit-transactionType" name="type">
					<c:forEach items="${transactionType}" var="t">
						<option value="${t.value}">${t.text}</option>
					</c:forEach>
				</select>
			</div>
			<label for="edit-possibility" class="col-sm-2 control-label">可能性</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="edit-possibility" readonly>
			</div>
		</div>
		
		<div class="form-group">
			<label for="edit-clueSource" class="col-sm-2 control-label">来源</label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="edit-clueSource" name="source">
					<c:forEach items="${source}" var="s">
						<option value="${s.value}">${s.text}</option>
					</c:forEach>
				</select>
			</div>
			<label for="edit-activitySrc" class="col-sm-2 control-label">市场活动源&nbsp;&nbsp;<a href="javascript:void(0);" id="connectABtn" data-toggle="modal"><span class="glyphicon glyphicon-search"></span></a></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="hidden" id="activityId" name="activityId">
				<input type="text" class="form-control" id="edit-activitySrc" readonly value="${t.activityId}">
			</div>
		</div>
		
		<div class="form-group">
			<label for="edit-contactsName" class="col-sm-2 control-label">联系人名称&nbsp;&nbsp;<a href="javascript:void(0);" data-toggle="modal" id="connectCBtn"><span class="glyphicon glyphicon-search"></span></a></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="hidden" id="contactsId" name="contactsId">
				<input type="text" class="form-control" id="edit-contactsName" readonly value="${t.contactsId}">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-describe" class="col-sm-2 control-label">描述</label>
			<div class="col-sm-10" style="width: 70%;">
				<textarea class="form-control" rows="3" id="create-describe" name="description">${t.description}</textarea>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-contactSummary" class="col-sm-2 control-label">联系纪要</label>
			<div class="col-sm-10" style="width: 70%;">
				<textarea class="form-control" rows="3" id="create-contactSummary" name="contactSummary">${t.contactSummary}</textarea>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control time2" readonly id="create-nextContactTime" name="nextContactTime" value="${t.nextContactTime}">
			</div>
		</div>
		
	</form>
</body>
</html>