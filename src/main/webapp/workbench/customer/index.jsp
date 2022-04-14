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
		$(".time").datetimepicker({
			minView: "month",
			language:  'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,
			todayBtn: true,
			pickerPosition: "bottom-left"
		});

		$("#selectAll").click(function(){
			$("input[name=xz]").prop("checked",this.checked)
		})
		$("#customerBody").on("click",$("input[name=xz]"),function (){
			$("#selectAll").prop("checked",$("input[name=xz]").length==$("input[name=xz]:checked").length)
		})

		//定制字段
		$("#definedColumns > li").click(function(e) {
			//防止下拉菜单消失
	        e.stopPropagation();
	    });
		pageList(1,5)
		$("#searchBtn").click(function (){
			$("#hide-website").val($.trim($("#search-website").val()));
			$("#hide-owner").val($.trim($("#search-owner").val()));
			$("#hide-name").val($.trim($("#search-name").val()));
			$("#hide-phone").val($.trim($("#search-phone").val()))

			pageList($("#customerPage").bs_pagination('getOption','currentPage'),
					$("#customerPage").bs_pagination('getOption','rowsPerPage'));
		})

		$("#addBtn").click(function (){
			$.ajax({
				url:"workbench/customer/getUserList.do",
				type:"get",
				dataType:"json",
				success:function(data){
					let html = "";
					$.each(data,function(i,n){
						html += "<option value='"+n.id+"'>"+n.name+"</option>"
					})
					$("#create-owner").html(html);

					let id = "${user.id}";
					$("#create-owner").val(id);

					// 处理完下拉框数据后，打开模态窗口
					$("#createCustomerModal").modal("show");
				}
			})
		})

		$("#saveBtn").click(function (){
			$.ajax({
				url:"workbench/customer/save.do",
				data:{
					"name":$.trim($("#create-name").val()),
					"website":$.trim($("#create-website").val()),
					"owner":$.trim($("#create-owner").val()),
					"phone":$.trim($("#create-phone").val()),
					"description":$.trim($("#create-description").val()),
					"contactSummary":$.trim($("#create-contactSummary").val()),
					"nextContactTime":$.trim($("#create-nextContactTime").val()),
					"address":$.trim($("#create-address").val())
				},
				type:"post",
				dataType:"json",
				success:function(result){
					if(result){
                        pageList(1,
								$("#customerPage").bs_pagination('getOption','rowsPerPage'))
						// 刷新保存中的数据
						$("#customerForm")[0].reset();

						// 关闭模态窗口
						$("#createCustomerModal").modal("hide");
					}else{
						alert("添加线索失败")
					}
				}
			})
		})

		$("#editBtn").click(function (){
			let $xz = $("input[name=xz]:checked");
			if($xz.length == 0){
				alert("请选择你需要修改的线索");
			}else if ($xz.length != 1){
				alert("仅能修改1一条线索")
			}else{
				$("#hidden-id").val($xz.val());

				$.ajax({
					url:"workbench/customer/getUserListAndCustomer.do",
					data:{
						id:$xz.val()
					},
					type:"get",
					dataType:"json",
					success:function(result){
						/*
							result:
								result:{"user":{"name":"张三","id":"324532bsddfg",...},
									"userList":["zhangsan":{},"lisi":{},...],"Clue":{"id":"","mphoe","",...}],
									"contact":{"id":"asdfasasdf235423","owner":"张三",...}
								}
						 */
						let html = "<option selected='selected' value='"+result.user.id+"'>" + result.user.name + "</option>"
						$.each(result.userList,function (i,n){
							html += "<option value='"+n.id+"'>" + n.name + "</option>"
						})

						$("#edit-owner").html(html);

						let customer = result.customer;
						$("#edit-name").val(customer.name)
						$("#edit-owner").val(customer.owner)
						$("#edit-website").val(customer.website)
						$("#edit-phone").val(customer.phone)
						$("#edit-description").val(customer.description)
						$("#edit-contactSummary").val(customer.contactSummary)
						$("#edit-nextContactTime").val(customer.nextContactTime)
						$("#edit-address").val(customer.address)
						$("#editCustomerModal").modal("show");
					}
				})
			}
		})

		$("#updateBtn").click(function (){
			let id = $("#hidden-id").val();
			alert(id);
			$.ajax({
				url:"workbench/customer/update.do",
				data:{
					"id":id,
					"name":$.trim($("#edit-name").val()),
					"owner":$.trim($("#edit-owner").val()),
					"website":$.trim($("#edit-website").val()),
					"description":$.trim($("#edit-description").val()),
					"contactSummary":$.trim($("#edit-contactSummary").val()),
					"nextContactTime":$.trim($("#edit-nextContactTime").val()),
					"address":$.trim($("#edit-address").val()),
					"phone":$.trim($("#edit-phone").val()),
				},
				type:"post",
				dataType:"json",
				success:function(result){
					if(result){
						pageList($("#customerPage").bs_pagination('getOption','currentPage'),
								$("#customerPage").bs_pagination('getOption','rowsPerPage'));
						$("#editCustomerModal").modal("hide");
					}else{
						alert("更新失败!");
					}
				}
			})
		})

		$("#deleteBtn").click(function (){
			let $xz = $("input[name=xz]:checked");

			if($xz.length == 0){
				alert("请选择需要删除的线索")
			}else{
				if(confirm("确认要删除以下"+$xz.length+"条线索吗")){
					let param ="";
					$.each($xz,function(i,n){
						param += "id=" + n.value;
						// 如果不是最后一个元素，需要在后面追减一个&
						if(i < $xz.length - 1){
							param+="&";
						}
					})

					// alert(param);
					$.ajax({
						url:"workbench/customer/delete.do",
						data:param,
						type:"post",
						dataType:"json",
						success:function(result){
							if(result){
								pageList($("#customerPage").bs_pagination('getOption','currentPage'),
										$("#customerPage").bs_pagination('getOption','rowsPerPage'));
							}else{
								alert("删除失败");
							}
						}
					})

				}
			}
		})
	});

	function pageList(pageNo,pageSize){

		$("#selectAll").prop("checked",false);

		$("#search-phone").val($.trim($("#hide-phone").val()));
		$("#search-owner").val($.trim($("#hide-owner").val()));
		$("#search-name").val($.trim($("#hide-name").val()));
		$("#search-website").val($.trim($("#hide-website").val()));

		$.ajax({
			url:"workbench/customer/pageList.do",
			data:{
				"pageNo":pageNo,
				"pageSize":pageSize,
				"phone":$("#search-phone").val(),
				"owner":$("#search-owner").val(),
				"name":$("#search-name").val(),
				"website":$("#search-website").val(),
			},
			type:"get",
			dataType:"json",
			success:function(data){
				/*
					result:
					{"total":100,"contactsList":["线索1":{"id":adsfa,...},"线索2":{"":..}]}
					 result.
				 */
				let html = "";
				$.each(data.dataList,function(i,n){
					html += '<tr>'
					html += '	<td><input type="checkbox" name="xz" value="'+n.id+'"/></td>'
					html += '	<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/customer/detail.do?id='+n.id+'\';">'+n.name+'</a></td>'
					html += '	<td>'+n.owner+'</td>'
					html += '	<td>'+n.phone+'</td>'
					html += '	<td>'+n.website+'</td>'
					html += '</tr>'
				})

				$("#customerBody").html(html);

				let totalPages = data.total%pageSize==0?data.total/pageSize:parseInt(data.total/pageSize)+1

				$("#customerPage").bs_pagination({
					currentPage: pageNo,
					rowsPerPage: pageSize,
					maxRowsPerPage: 20,
					totalPages: totalPages,
					totalRows: data.total,
					visiblePageLinks: 3,

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
	<input type="hidden" id="hide-phone">
	<input type="hidden" id="hide-owner">
	<input type="hidden" id="hide-website">
	<input type="hidden" id="hide-name">
	<input type="hidden" id="hidden-id">
	<!-- 创建客户的模态窗口 -->
	<div class="modal fade" id="createCustomerModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建客户</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" id="customerForm" role="form">

						<div class="form-group">
							<label for="create-owner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-owner">
								</select>
							</div>
							<label for="create-name" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-name">
							</div>
						</div>

						<div class="form-group">
                            <label for="create-website" class="col-sm-2 control-label">公司网站</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-website">
                            </div>
							<label for="create-phone" class="col-sm-2 control-label">公司座机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-phone">
							</div>
						</div>
						<div class="form-group">
							<label for="create-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>

                        <div style="position: relative;top: 15px;">
                            <div class="form-group">
                                <label for="create-contactSummary" class="col-sm-2 control-label">联系纪要</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="3" id="create-contactSummary"></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="create-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
                                <div class="col-sm-10" style="width: 300px;">
                                    <input type="text" class="form-control time" id="create-nextContactTime">
                                </div>
                            </div>
                        </div>

                        <div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 10px;"></div>

                        <div style="position: relative;top: 20px;">
                            <div class="form-group">
                                <label for="create-address" class="col-sm-2 control-label">详细地址</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="1" id="create-address"></textarea>
                                </div>
                            </div>
                        </div>
					</form>

				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 修改客户的模态窗口 -->
	<div class="modal fade" id="editCustomerModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">修改客户</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form">

						<div class="form-group">
							<label for="edit-owner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-owner">
								</select>
							</div>
							<label for="edit-name" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-name" >
							</div>
						</div>

						<div class="form-group">
                            <label for="edit-website" class="col-sm-2 control-label">公司网站</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-website">
                            </div>
							<label for="edit-phone" class="col-sm-2 control-label">公司座机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-phone" >
							</div>
						</div>

						<div class="form-group">
							<label for="edit-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-description"></textarea>
							</div>
						</div>

						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>

                        <div style="position: relative;top: 15px;">
                            <div class="form-group">
                                <label for="edit-contactSummary" class="col-sm-2 control-label">联系纪要</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="3" id="edit-contactSummary"></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="edit-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
                                <div class="col-sm-10" style="width: 300px;">
                                    <input type="text" class="form-control time" id="edit-nextContactTime">
                                </div>
                            </div>
                        </div>

                        <div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 10px;"></div>

                        <div style="position: relative;top: 20px;">
                            <div class="form-group">
                                <label for="edit-address" class="col-sm-2 control-label">详细地址</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="1" id="edit-address"></textarea>
                                </div>
                            </div>
                        </div>
					</form>

				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateBtn">更新</button>
				</div>
			</div>
		</div>
	</div>




	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>客户列表</h3>
			</div>
		</div>
	</div>

	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">

		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">

			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" id="search-name" type="text">
				    </div>
				  </div>

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" id="search-owner" type="text">
				    </div>
				  </div>

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">公司座机</div>
				      <input class="form-control" id="search-phone" type="text">
				    </div>
				  </div>

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">公司网站</div>
				      <input class="form-control" id="search-website" type="text">
				    </div>
				  </div>

				  <button type="button" id="searchBtn" class="btn btn-default">查询</button>

				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>

			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="selectAll"/></td>
							<td>名称</td>
							<td>所有者</td>
							<td>公司座机</td>
							<td>公司网站</td>
						</tr>
					</thead>
					<tbody id="customerBody">
					</tbody>
				</table>
			</div>

			<div style="height: 50px; position: relative;top: 30px;">
				<div id="customerPage"></div>
			</div>

		</div>

	</div>
</body>
</html>