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
<script type="text/javascript">

	//默认情况下取消和保存按钮是隐藏的
	var cancelAndSaveBtnDefault = true;
	
	$(function(){

		$(".time").datetimepicker({
			minView: "month",
			language:  'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,
			todayBtn: true,
			pickerPosition: "top-left"
		});

		// 显示备注列表信息
		showRemarkList();

		$("#selectAll").click(function(){
			$("input[name=xz]").prop("checked",this.checked)
		})
		$("#activitySearchBody").on("click",$("input[name=xz]"),function (){
			$("#selectAll").prop("checked",$("input[name=xz]").length==$("input[name=xz]:checked").length)
		})

		$("#remark").focus(function(){
			if(cancelAndSaveBtnDefault){
				//设置remarkDiv的高度为130px
				$("#remarkDiv").css("height","130px");
				//显示
				$("#cancelAndSaveBtn").show("2000");
				cancelAndSaveBtnDefault = false;
			}
		});
		
		$("#cancelBtn").click(function(){
			//显示
			$("#cancelAndSaveBtn").hide();
			//设置remarkDiv的高度为130px
			$("#remarkDiv").css("height","90px");
			cancelAndSaveBtnDefault = true;
		});

		$("#remarkList").on("mouseover",".myHref",function(){
			$(this).children("span").css("color","red");
		})
		$("#remarkList").on("mouseout",".myHref",function(){
			$(this).children("span").css("color","#E6E6E6");
		})
		$("#remarkList").on("mouseover",".remarkDiv",function(){
			$(this).children("div").children("div").show();
		})
		$("#remarkList").on("mouseout",".remarkDiv",function() {
			$(this).children("div").children("div").hide();
		})
		// 页面加载完毕之后，取出关联的市场活动信息列表
		showActivityList();


		// 为关联市场活动模态窗口中的搜索框，绑定事件，通过触发回车键，查询并展现所需市场活动列表
		$("#aName").keydown(function (event){
			if(event.keyCode==13){
				getActivityNotAssociate();
				// 展现完列表后，将模态窗口的默认回车行为禁用掉
				return false;
			}
		})

		$("#connectBtn").click(function (){
			getActivityNotAssociate();
			$("#bundModal").modal("show");
		})

		// 为关联按钮绑定事件，执行关联表的添加操作
		$("#bundBtn").click(function(){
			let $xz = $("input[name=xz]:checked")

			if($xz.length == 0){
				alert("请选择需要关联的市场活动！");
			}else{
				// 1条或者多条

				let param = "clueId=" + "${clue.id}&";

				$.each($xz,function(i,n){
					param += ("id="+n.value);
					if(i < $xz.length - 1){
						param +="&";
					}
				})
				$.ajax({
					url:"workbench/clue/bund.do",
					data:param,
					type:"post",
					dataType:"json",
					success:function(result){
						if(result){
							showActivityList();
							// 清除搜索框中的信息
							$("#aName").val("");
							$("#selectAll").prop("checked",false);
							$("#bundModal").modal("hide");
						}else{
							alert("关联失败！")
						}
					}
				})
			}
		})

		$("#deleteBtn").click(function (){
			if(confirm("你确定要删除吗")){
				$.ajax({
					url:"workbench/clue/deleteClue.do",
					data:{"id":"${clue.id}"},
					type:"post",
					dataType:"json",
					success:function(result){
						if(result){
							window.location.href = "workbench/clue/index.jsp";
						}else{
							alert("删除失败");
						}
					}
				})
			}
		})

		$("#editBtn").click(function (){
			$.ajax({
				url:"workbench/clue/getUserListAndClue.do",
				data:{"id":"${clue.id}"},
				type:"get",
				dataType:"json",
				success:function(result){

					let html = "<option value='"+result.user.id+"'>" + result.user.name + "</option>"
					$.each(result.userList,function (i,n){
						html += "<option value='"+n.id+"'>" + n.name + "</option>"
					})

					$("#edit-owner").html(html);

					let clue = result.clue;
					$("#edit-company").val(clue.company)
					$("#edit-fullname").val(clue.fullname)
					$("#edit-appellation").val(clue.appellation)
					$("#edit-owner").val(clue.owner)
					$("#edit-job").val(clue.job)
					$("#edit-email").val(clue.email)
					$("#edit-phone").val(clue.phone)
					$("#edit-mphone").val(clue.mphone)
					$("#edit-source").val(clue.source)
					$("#edit-state").val(clue.state)
					$("#edit-website").val(clue.website)
					$("#edit-description").val(clue.description)
					$("#edit-contactSummary").val(clue.contactSummary)
					$("#edit-nextContactTime").val(clue.nextContactTime)
					$("#edit-address").val(clue.address)

					$("#editClueModal").modal("show");
				}
			})
		})

		$("#updateRemarkBtn").click(function (){
			let id = $("#remarkId").val();
			// 执行备注更新操作
			$.ajax({
				url:"workbench/clue/updateRemark.do",
				data:{
					"noteContent":$.trim($("#noteContent").val()),
					"id":id
				},
				type:"post",
				dataType: "json",
				success:function(data){
					if(data.success){
						$("#e"+id).html(data.cr.noteContent);
						$("#s"+id).html(data.cr.editTime+" 由"+data.cr.editBy);
						$("#editRemarkModal").modal("hide")
					}else{
						alert("修改备注失败")
					}
				}
			})
		})

		$("#updateBtn").click(function(){
			$.ajax({
				url:"workbench/clue/update.do",
				data:{
					"company":$.trim($("#edit-company").val()),
					"id":"${clue.id}",
					"fullname":$.trim($("#edit-fullname").val()),
					"appellation":$.trim($("#edit-appellation").val()),
					"owner":$.trim($("#edit-owner").val()),
					"job":$.trim($("#edit-job").val()),
					"email":$.trim($("#edit-email").val()),
					"phone":$.trim($("#edit-phone").val()),
					"source":$.trim($("#edit-source").val()),
					"state":$.trim($("#edit-state").val()),
					"website":$.trim($("#edit-website").val()),
					"description":$.trim($("#edit-description").val()),
					"contactSummary":$.trim($("#edit-contactSummary").val()),
					"nextContactTime":$.trim($("#edit-nextContactTime").val()),
					"address":$.trim($("#edit-address").val()),
					"mphone":$.trim($("#edit-mphone").val())
				},
				type:"post",
				dataType:"json",
				success:function(result){
					if(result){
						window.location.href = "workbench/clue/detail.do?id=" + "${clue.id}";
					}else{
						alert("更新失败!");
					}
				}
			})
		})

		$("#saveBtn").click(function (){
			$.ajax({
				url:"workbench/clue/saveRemark.do",
				data:{
					"noteContent":$.trim($("#remark").val()),
					"clueId":"${clue.id}"
				},
				type:"post",
				dataType:"json",
				success:function(result){
					if(result.success){
						let html = "";
						html += '<div id="'+result.cr.id+'"class="remarkDiv" style="height: 60px;">'
						html += '	<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">'
						html += '		<div style="position: relative; top: -40px; left: 40px;" >'
						html += '			<h5 id="e'+result.cr.id+'">'+result.cr.noteContent+'</h5>'
						html += '			<font color="gray">线索</font> <font color="gray">-</font> <b>'+"${clue.fullname}${clue.appellation}"+'-'+"${clue.company}"+'</b> <small style="color: gray;" id="s'+result.cr.id+'"> '+result.cr.createTime+'由'+result.cr.createBy+'</small>'
						html += '			<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">'
						html += '				<a class="myHref" href="javascript:void(0);" onclick="editRemark(\''+result.cr.id+'\')"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>';
						html += '                &nbsp;&nbsp;&nbsp;&nbsp;';
						html += '                <a class="myHref" href="javascript:void(0);" onclick="deleteRemark(\''+result.cr.id+'\')"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>';
						html += '            </div>'
						html += '		</div>'
						html += '</div>'
						$("#remarkDiv").before(html);
						$("#remark").val("")
					}else{
						alert("保存失败！")
					}
				}
			})
		})
	});

	function showActivityList(){
		$.ajax({
			url:"workbench/clue/getActivityListByClueId.do",
			data:{
				"clueId":"${clue.id}"
			},
			type:"get",
			dataType:"json",
			success:function(result){
				/*
					result
						{"dataList":["市场活动1:{"id":..,"owner":"张三",...},"市场活动2":{..},...]
				 */
				let html = ""

				$.each(result,function (i,n){
					html += '<tr>'
					html += '	<td>'+n.name+'</td>'
					html += '	<td>'+n.startDate+'</td>'
					html += '	<td>'+n.endDate+'</td>'
					html += '	<td>'+n.owner+'</td>'
					html += '	<td><a href="javascript:void(0);" onclick="unband(\''+n.id+'\')" style="text-decoration: none;"><span class="glyphicon glyphicon-remove"></span>解除关联</a></td>'
					html += '</tr>'
				})

				$("#activityBody").html(html);
			}
		})
	}

	/*
		id：我们想要一个关联关系表的id
	 */
	function unband(id){
		$.ajax({
			url:"workbench/clue/unband.do",
			data:{
				"id":id
			},
			type:"post",
			dataType:"json",
			success:function(result){
				if(result){
					showActivityList()
				}else{
					alert("删除失败！");
				}
			}
		})
	}

	function getActivityNotAssociate(){
		$.ajax({
			url:"workbench/clue/getActivityListByNameAndNotAssociateByClueId.do",
			data:{
				"aName":$.trim($("#aName").val()),
				"clueId":"${clue.id}"
			},
			type:"get",
			dataType:"json",
			success:function(result){
				/*
                    data
                    {"activityList":["activity1":{"id":2342,"onwer":"....",...},"activity2":{...},...]}
                 */
				let html = "";
				$.each(result,function(i,n){
					html += '<tr>'
					html += '    <td><input type="checkbox" name="xz" value="'+n.id+'"/></td>'
					html += '    <td>'+n.name+'</td>'
					html += '    <td>'+n.startDate+'</td>'
					html += '    <td>'+n.endDate+'</td>'
					html += '    <td>'+n.owner+'</td>'
					html += '</tr>'
				})

				$("#activitySearchBody").html(html);
			}
		})
	}

	function showRemarkList(){
		$.ajax({
			url:"workbench/clue/showRemarkList.do",
			data:{
				"id":"${clue.id}"
			},
			type:"get",
			dataType:"json",
			success:function(result){
				/*
                    result :
                        {"remarkList":["remark1":{"id":"23424","name":"李四",...},"remark2":{"id":"234987",...}]}
                 */

				let html = "";
				$.each(result,function (i,n){
					html += '<div id="'+n.id+'"class="remarkDiv" style="height: 60px;">'
					html += '	<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">'
					html += '		<div style="position: relative; top: -40px; left: 40px;" >'
					html += '			<h5 id="e'+n.id+'">'+n.noteContent+'</h5>'
					html += '			<font color="gray">线索</font> <font color="gray">-</font> <b>'+"${clue.fullname}${clue.appellation}"+'-'+"${clue.company}"+'</b> <small style="color: gray;" id="s'+n.id+'"> '+(n.editFlag==0?n.createTime:n.editTime)+'由'+(n.editFlag==0?n.createBy:n.editBy)+'</small>'
					html += '			<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">'
					html += '				<a class="myHref" href="javascript:void(0);" onclick="editRemark(\''+n.id+'\')"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #e6e6e6;"></span></a>';
					html += '                &nbsp;&nbsp;&nbsp;&nbsp;';
					html += '                <a class="myHref" href="javascript:void(0);" onclick="deleteRemark(\''+n.id+'\')"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #e6e6e6;"></span></a>';
					html += '            </div>'
					html += '		</div>'
					html += '</div>'
				})

				$("#remarkDiv").before(html);
			}
		})
	}

	function editRemark(id){
		// 将模态窗口中，隐藏域中的id进行赋值
		$("#remarkId").val(id);

		// 找到指定的存放备注信息的h5标签
		let noteContent = $("#e"+id).html();
		$("#noteContent").val(noteContent);

		// 以上信息处理完毕后，将处理信息的模态窗口打开
		$("#editRemarkModal").modal("show");
	}

	function deleteRemark(id){
		$.ajax({
			url:"workbench/clue/deleteRemark.do",
			data:{
				"id":id
			},
			type:"post",
			dataType:"json",
			success:function(result){
				if(result){
					$("#"+id).remove();
				}else{
					alert("移除失败")
				}
			}
		})
	}
</script>

</head>
<body>

	<!-- 关联市场活动的模态窗口 -->
	<div class="modal fade" id="bundModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 80%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">关联市场活动</h4>
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
					<table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td><input type="checkbox" id="selectAll"/></td>
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
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" id="bundBtn">关联</button>
				</div>
			</div>
		</div>
	</div>

    <!-- 修改线索的模态窗口 -->
    <div class="modal fade" id="editClueModal" role="dialog">
        <div class="modal-dialog" role="document" style="width: 90%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">修改线索</h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal" role="form">

                        <div class="form-group">
                            <label for="edit-owner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <select class="form-control" id="edit-owner">
                                </select>
                            </div>
                            <label for="edit-company" class="col-sm-2 control-label">公司<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-company">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="edit-appellation" class="col-sm-2 control-label">称呼</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <select class="form-control" id="edit-appellation">
									<c:forEach items="${appellation}" var="a">
										<option value="${a.value}">${a.text}</option>
									</c:forEach>
                                </select>
                            </div>
                            <label for="edit-fullname" class="col-sm-2 control-label">姓名<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-fullname">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="edit-job" class="col-sm-2 control-label">职位</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-job" >
                            </div>
                            <label for="edit-email" class="col-sm-2 control-label">邮箱</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-email">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="edit-phone" class="col-sm-2 control-label">公司座机</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-phone" >
                            </div>
                            <label for="edit-website" class="col-sm-2 control-label">公司网站</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-website" >
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="edit-mphone" class="col-sm-2 control-label">手机</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-mphone">
                            </div>
                            <label for="edit-state" class="col-sm-2 control-label">线索状态</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <select class="form-control" id="edit-state">
									<c:forEach items="${clueState}" var="c">
										<option value="${c.value}">${c.text}</option>
									</c:forEach>
                                </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="edit-source" class="col-sm-2 control-label">线索来源</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <select class="form-control" id="edit-source">
									<c:forEach items="${source}" var="s">
										<option value="${s.value}">${s.text}</option>
									</c:forEach>
                                </select>
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
                                    <input type="text" class="form-control time" id="edit-nextContactTime" readonly>
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
                    <button type="button" class="btn btn-default" id="updateBtn">更新</button>
                </div>
            </div>
        </div>
    </div>

	<!-- 返回按钮 -->
	<div style="position: relative; top: 35px; left: 10px;">
		<a href="javascript:void(0);" onclick="window.history.back();"><span class="glyphicon glyphicon-arrow-left" style="font-size: 20px; color: #DDDDDD"></span></a>
	</div>
	
	<!-- 大标题 -->
	<div style="position: relative; left: 40px; top: -30px;">
		<div class="page-header">
			<h3>${clue.fullname}${clue.appellation} <small>${clue.company}</small></h3>
		</div>
		<div style="position: relative; height: 50px; width: 500px;  top: -72px; left: 700px;">
			<button type="button" class="btn btn-default" onclick="window.location.href='workbench/clue/convert.jsp?id=${clue.id}&fullname=${clue.fullname}&appellation=${clue.appellation}&company=${clue.company}&owner=${clue.owner}';"><span class="glyphicon glyphicon-retweet"></span> 转换</button>
			<button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-edit"></span> 编辑</button>
			<button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
		</div>
	</div>
	
	<!-- 详细信息 -->
	<div style="position: relative; top: -70px;">
		<div style="position: relative; left: 40px; height: 30px;">
			<div style="width: 300px; color: gray;">名称</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${clue.fullname}${clue.appellation}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">所有者</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${clue.owner}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 10px;">
			<div style="width: 300px; color: gray;">公司</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${clue.company}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">职位</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${clue.job}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 20px;">
			<div style="width: 300px; color: gray;">邮箱</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${clue.email}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">公司座机</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${clue.phone}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 30px;">
			<div style="width: 300px; color: gray;">公司网站</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${clue.website}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">手机</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${clue.mphone}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 40px;">
			<div style="width: 300px; color: gray;">线索状态</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${clue.state}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">线索来源</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${clue.source}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 50px;">
			<div style="width: 300px; color: gray;">创建者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>&nbsp;${clue.createBy}&nbsp;</b><small style="font-size: 10px; color: gray;">${clue.createTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 60px;">
			<div style="width: 300px; color: gray;">修改者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${clue.editBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${clue.editTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 70px;">
			<div style="width: 300px; color: gray;">描述</div>
			<div style="width: 630px;position: relative; left: 200px; top: -20px;">
				<b>
					${clue.description}
				</b>
			</div>
			<div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 80px;">
			<div style="width: 300px; color: gray;">联系纪要</div>
			<div style="width: 630px;position: relative; left: 200px; top: -20px;">
				<b>
					${clue.contactSummary}
				</b>
			</div>
			<div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 90px;">
			<div style="width: 300px; color: gray;">下次联系时间</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${clue.nextContactTime}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -20px; "></div>
		</div>
        <div style="position: relative; left: 40px; height: 30px; top: 100px;">
            <div style="width: 300px; color: gray;">详细地址</div>
            <div style="width: 630px;position: relative; left: 200px; top: -20px;">
                <b>
					${clue.address}
                </b>
            </div>
            <div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
        </div>
	</div>

	<!-- 修改市场活动备注的模态窗口 -->
	<div class="modal fade" id="editRemarkModal" role="dialog">
		<input type="hidden" id="remarkId">
		<div class="modal-dialog" role="document" style="width: 40%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="edit-remarkDescription">修改备注</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form">
						<div class="form-group">
							<label for="edit-description" class="col-sm-2 control-label">内容</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="noteContent"></textarea>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateRemarkBtn">更新</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 备注 -->
	<div id="remarkList" style="position: relative; top: 40px; left: 40px;">
		<div class="page-header">
			<h4>备注</h4>
		</div>
		<!--备注-->
		<div id="remarkDiv" style="background-color: #E6E6E6; width: 870px; height: 90px;">
			<form role="form" style="position: relative;top: 10px; left: 10px;">
				<textarea id="remark" class="form-control" style="width: 850px; resize : none;" rows="2"  placeholder="添加备注..."></textarea>
				<p id="cancelAndSaveBtn" style="position: relative;left: 737px; top: 10px; display: none;">
					<button id="cancelBtn" type="button" class="btn btn-default">取消</button>
					<button type="button" id="saveBtn" class="btn btn-primary">保存</button>
				</p>
			</form>
		</div>
	</div>
	
	<!-- 市场活动 -->
	<div>
		<div style="position: relative; top: 60px; left: 40px;">
			<div class="page-header">
				<h4>市场活动</h4>
			</div>
			<div style="position: relative;top: 0px;">
				<table class="table table-hover" style="width: 900px;">
					<thead>
						<tr style="color: #B3B3B3;">
							<td>名称</td>
							<td>开始日期</td>
							<td>结束日期</td>
							<td>所有者</td>
							<td></td>
						</tr>
					</thead>
					<tbody id="activityBody">
						 <%-- <tr>  --%>
						 <%-- 	<td>发传单</td>  --%>
						 <%-- 	<td>2020-10-10</td>  --%>
						 <%-- 	<td>2020-10-20</td>  --%>
						 <%-- 	<td>zhangsan</td>  --%>
						 <%-- 	<td><a href="javascript:void(0);"  style="text-decoration: none;"><span class="glyphicon glyphicon-remove"></span>解除关联</a></td>  --%>
						 <%-- </tr>  --%>
						<%-- <tr> --%>
						<%-- 	<td>发传单</td> --%>
						<%-- 	<td>2020-10-10</td> --%>
						<%-- 	<td>2020-10-20</td> --%>
						<%-- 	<td>zhangsan</td> --%>
						<%-- 	<td><a href="javascript:void(0);"  style="text-decoration: none;"><span class="glyphicon glyphicon-remove"></span>解除关联</a></td> --%>
						<%-- </tr> --%>
					</tbody>
				</table>
			</div>
			
			<div>
				<a href="javascript:void(0);" data-toggle="modal" id="connectBtn" style="text-decoration: none;"><span class="glyphicon glyphicon-plus"></span>关联市场活动</a>
			</div>
		</div>
	</div>
	
	
	<div style="height: 200px;"></div>
</body>
</html>