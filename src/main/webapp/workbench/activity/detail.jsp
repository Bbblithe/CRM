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

	//默认情况下取消和保存按钮是隐藏的
	var cancelAndSaveBtnDefault = true;
	
	$(function(){

		showRemarkList();

		$("#hide-id").val("${activity.id}")
		$(".time").datetimepicker({
			minView: "month",
			language:  'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,
			todayBtn: true,
			pickerPosition: "bottom-left"
		});

		$("#deleteBtn").click(function(){
			// 找到复选框中所有挑勾的复选框的jquery对象
			<%--let param = "";--%>
			<%--param += ${activity.id};--%>

				if(confirm("确定删除所选中的记录吗")){

					// 一条或多条
					// 当使用同一key多个value时，不再使用json格式传递参数
					// 拼接参数
					$.ajax({
						url:"workbench/activity/deleteOne.do",
						data:{
							"id":$("#hide-id").val()
						},
						type:"post",
						dataType:"json",
						success:function(data){
							/*
                                data:{success:true/false}
                             */

							if(data.success){
								window.location.href='workbench/activity/index.jsp'
							}else {
								alert("删除市场活动失败");
							}
						}
					})
				}
		})

		$("#editBtn").click(function(){
			let id = $("#hide-id").val();
			$.ajax({
				url:"workbench/activity/getUserListAndActivity.do",
				data:{
					"id":id
				},
				type:"post",
				dataType:"json",
				success:function(data){
					/*
						data
							用户列表，市场活动对象
						{"uList":[{用户1},{用户2},...],"a":{市场活动}}
					 */
					let html = "<option value='"+data.user.id+"'>" + data.user.name + "</option>"
					$.each(data.userList,function (i,n){
						html += "<option value='"+n.id+"'>" + n.name + "</option>"
					})
					$("#edit-owner").html(html);
					let activity = data.activity
					$("#edit-activityName").val(activity.name)
					$("#edit-startDate").val(activity.startDate)
					$("#edit-endDate").val(activity.endDate)
					$("#edit-cost").val(activity.cost)
					$("#edit-description").val(activity.description)
					$("#editActivityModal").modal("show")
				}
			})
		})

		$("#updateBtn").click(function(){
			$.ajax({
				url:"workbench/activity/update.do",
				data:{
					"id":$("#hide-id").val(),
					"owner":$.trim($("#edit-owner").val()),
					"name":$.trim($("#edit-activityName").val()),
					"startDate":$.trim($("#edit-startDate").val()),
					"endDate":$.trim($("#edit-endDate").val()),
					"cost":$.trim($("#edit-cost").val()),
					"description":$.trim($("#edit-description").val())
				},
				type:"post",
				dataType: "json",
				success:function(data){
					/*
						data
							{"success":true/false}
					 */
					if(data.success){
						// 关闭添加操作的模态窗口
						$ ("#editActivityModal").modal("hide");
						let html = 'workbench/activity/detail.do?id=' + $("#hide-id").val();
						window.location.href = html;

					}else {
						alert("更新市场活动失败")
					}
				}
			})
		})

		// 为更新按钮绑定事件
		$("#updateRemarkBtn").click(function(){
			let id = $("#remarkId").val();
			// 执行备注更新操作
			$.ajax({
				url:"workbench/activity/updateRemark.do",
				data:{
					"noteContent":$.trim($("#noteContent").val()),
					"id":id
				},
				type:"post",
				dataType: "json",
				success:function(data){
					/*
						data:
							{"success":true/false,"ar",{备注}}
					 */
					if(data.success){
						// 修改备注成功
						// 更新div中相应的信息，需要更新的内容有noteContent,editTime,editBy
						$("#e"+id).html(data.ar.noteContent);
						$("#s"+id).html(data.ar.editTime+" 由"+data.ar.editBy);
						$("#editRemarkModal").modal("hide")
					}else{
						alert("修改备注失败")
					}
				}
			})
		})

		$("#saveRemarkBtn").click(function(){
			// 执行备注添加操作
			$.ajax({
				url:"workbench/activity/saveRemark.do",
				data:{
					"noteContent":$.trim($("#remark").val()),
					"activityId":"${activity.id}"
				},
				type:"post",
				dataType: "json",
				success:function(data){
					/*
						data:
							{"success":true/false,"ar":{备注}}
					 */
					if(data.success){
						// 添加成功，在textarea文本域上方新增一个div
						let html = "";
						html += '<div id="'+data.ar.id+'"class="remarkDiv" style="height: 60px;">';
						html += '    <img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">';
						html += '        <div style="position: relative; top: -40px; left: 40px;" >';
						html += '            <h5 id="e'+data.ar.id+'">'+data.ar.noteContent+'</h5>';
						html += '            <font color="gray">市场活动</font> <font color="gray">-</font> <b>${activity.name}</b> <small style="color: gray;" id="s'+data.ar.id+'"> '+ (data.ar.createTime) +' 由'+(data.ar.createBy)+'</small>';
						html += '            <div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">';
						html += '                <a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #FF0000;"></span></a>';
						html += '                &nbsp;&nbsp;&nbsp;&nbsp;';
						html += '                <a class="myHref" href="javascript:void(0);" onclick="deleteRemark(\''+data.ar.id+'\')"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #FF0000;"></span></a>';
						html += '            </div>';
						html += '        </div>';
						html += '</div>';
						$("#remarkDiv").before(html);
						$("#remark").val("")
					}else{
						alert("添加失败")
					}
				}
			})
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
		
		$(".remarkDiv").mouseover(function(){
			$(this).children("div").children("div").show();
		});
		
		$(".remarkDiv").mouseout(function(){
			$(this).children("div").children("div").hide();
		});
		
		$(".myHref").mouseover(function(){
			$(this).children("span").css("color","red");
		});
		
		$(".myHref").mouseout(function(){
			$(this).children("span").css("color","#E6E6E6");
		});

		$("#remarkBody").on("mouseover",".remarkDiv",function(){
			$(this).children("div").children("div").show();
		})
		$("#remarkBody").on("mouseout",".remarkDiv",function(){
			$(this).children("div").children("div").hide();
		})
	});
    // 页面加载完毕后，展现该市场活动关联的备注信息列表
    function showRemarkList(){
        $.ajax({
            url:"workbench/activity/getRemarkListByAId.do",
            data:{
                "activityId":"${activity.id}"
            },
            type:"get",
            dataType:"json",
            success:function(data){
                /*
                    data:[{备注1},{备注2},{...}]
                 */

                let html = "";
                $.each(data,function(i,n){
                    html += '<div id="'+n.id+'"class="remarkDiv" style="height: 60px;">';
                    html += '    <img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">';
                    html += '        <div style="position: relative; top: -40px; left: 40px;" >';
                    html += '            <h5 id="e'+n.id+'">'+n.noteContent+'</h5>';
                    html += '            <font color="gray">市场活动</font> <font color="gray">-</font> <b>${activity.name}</b> <small style="color: gray;" id="s'+n.id+'"> '+ (n.editFlag==0?n.createTime:n.editTime) +' 由'+(n.editFlag==0?n.createBy:n.editBy)+'</small>';
                    html += '            <div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">';
                    html += '                <a class="myHref" href="javascript:void(0);" onclick="editRemark(\''+n.id+'\')"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #FF0000;"></span></a>';
                    html += '                &nbsp;&nbsp;&nbsp;&nbsp;';
                    html += '                <a class="myHref" href="javascript:void(0);" onclick="deleteRemark(\''+n.id+'\')"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #FF0000;"></span></a>';
                    html += '            </div>';
                    html += '        </div>';
                    html += '</div>';
                })

                $("#remarkDiv").before(html);
            }
        })
    }

	function deleteRemark(id){
		$.ajax({
			url:"workbench/activity/deleteRemark.do",
			data:{
				id:id
			},
			type:"post",
			dataType:"json",
			success:function(data){
				if(data.success){
					// 找到需要删除记录的div，将div异常
					$("#"+id).remove();
				}else{
					alert("删除备注失败");
				}
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
</script>

</head>
<body>
	    <!-- 修改市场活动备注的模态窗口 -->
	    <div class="modal fade" id="editRemarkModal" role="dialog">
	    	&lt;%&ndash; 备注的id &ndash;%&gt;
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

     <!-- 修改市场活动的模态窗口 -->
     <div class="modal fade" id="editActivityModal" role="dialog">
		 <input type="hidden" id="hide-id">
		 <div class="modal-dialog" role="document" style="width: 85%;">
             <div class="modal-content">
                 <div class="modal-header">
                     <button type="button" class="close" data-dismiss="modal">
                         <span aria-hidden="true">×</span>
                     </button>
                     <h4 class="modal-title" id="myModalLabel">修改市场活动</h4>
                 </div>
                 <div class="modal-body">

                     <form class="form-horizontal" role="form">

                         <div class="form-group">
                             <label for="edit-owner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
                             <div class="col-sm-10" style="width: 300px;">
                                 <select class="form-control" id="edit-owner">

                                 </select>
                             </div>
                             <label for="edit-activityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                             <div class="col-sm-10" style="width: 300px;">
                                 <input type="text" class="form-control" id="edit-activityName" >
                             </div>
                         </div>

                         <div class="form-group">
                             <label for="edit-startDate" class="col-sm-2 control-label">开始日期</label>
                             <div class="col-sm-10" style="width: 300px;">
                                  <input type="text" class="form-control time" id="edit-startDate" readonly>
                              </div>
                              <label for="edit-endDate" class="col-sm-2 control-label">结束日期</label>
                              <div class="col-sm-10" style="width: 300px;">
                                  <input type="text" class="form-control time" id="edit-endDate" readonly>
                              </div>
                          </div>

                          <div class="form-group">
                              <label for="edit-cost" class="col-sm-2 control-label">成本</label>
                              <div class="col-sm-10" style="width: 300px;">
                                  <input type="text" class="form-control" id="edit-cost">
                              </div>
                          </div>

                          <div class="form-group">
                              <label for="edit-description" class="col-sm-2 control-label">描述</label>
                              <div class="col-sm-10" style="width: 81%;">
                                  <textarea class="form-control" rows="3" id="edit-description"></textarea>
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

	<!-- 返回按钮 -->
	<div style="position: relative; top: 35px; left: 10px;">
		<a href="javascript:void(0);" onclick="window.history.back();"><span class="glyphicon glyphicon-arrow-left" style="font-size: 20px; color: #DDDDDD"></span></a>
	</div>
	
	<!-- 大标题 -->
	<div style="position: relative; left: 40px; top: -30px;">
		<div class="page-header">
			<h3>市场活动-${activity.name} <small>${activity.startDate} ~ ${activity.endDate}</small></h3>
		</div>
		<div style="position: relative; height: 50px; width: 250px;  top: -72px; left: 700px;">
			<button type="button" id="editBtn" class="btn btn-default" data-toggle="modal" data-target="#editActivityModal"><span class="glyphicon glyphicon-edit"></span> 编辑</button>
			<button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
		</div>
	</div>
	
	<!-- 详细信息 -->
	<div style="position: relative; top: -70px;">
		<div style="position: relative; left: 40px; height: 30px;">
			<div style="width: 300px; color: gray;">所有者</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${activity.owner}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">名称</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${activity.name}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>

		<div style="position: relative; left: 40px; height: 30px; top: 10px;">
			<div style="width: 300px; color: gray;">开始日期</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${activity.startDate}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">结束日期</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${activity.endDate}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 20px;">
			<div style="width: 300px; color: gray;">成本</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${activity.cost}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 30px;">
			<div style="width: 300px; color: gray;">创建者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${activity.createBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">2017-01-18 10:10:10</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 40px;">
			<div style="width: 300px; color: gray;">修改者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${activity.editBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">2017-01-19 10:10:10</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 50px;">
			<div style="width: 300px; color: gray;">描述</div>
			<div style="width: 630px;position: relative; left: 200px; top: -20px;">
				<b>
					${activity.description}
				</b>
			</div>
			<div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
	</div>
	
	<!-- 备注 -->
	<div id="remarkBody" style="position: relative; top: 30px; left: 40px;">
		<div class="page-header">
			<h4>备注</h4>
		</div>


		
		<%-- <!-- 备注1 --> --%>
		<%-- <div class="remarkDiv" style="height: 60px;"> --%>
		<%-- 	<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;"> --%>
		<%-- 	<div style="position: relative; top: -40px; left: 40px;" > --%>
		<%-- 		<h5>哎呦！</h5> --%>
		<%-- 		<font color="gray">市场活动</font> <font color="gray">-</font> <b>发传单</b> <small style="color: gray;"> 2017-01-22 10:10:10 由zhangsan</small> --%>
		<%-- 		<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;"> --%>
		<%-- 			<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a> --%>
		<%-- 			&nbsp;&nbsp;&nbsp;&nbsp; --%>
		<%-- 			<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a> --%>
		<%-- 		</div> --%>
		<%-- 	</div> --%>
		<%-- </div> --%>
		
		<%-- <!-- 备注2 --> --%>
		<%-- <div class="remarkDiv" style="height: 60px;"> --%>
		<%-- 	<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;"> --%>
		<%-- 	<div style="position: relative; top: -40px; left: 40px;" > --%>
		<%-- 		<h5>呵呵！</h5> --%>
		<%-- 		<font color="gray">市场活动</font> <font color="gray">-</font> <b>发传单</b> <small style="color: gray;"> 2017-01-22 10:20:10 由zhangsan</small> --%>
		<%-- 		<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;"> --%>
		<%-- 			<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a> --%>
		<%-- 			&nbsp;&nbsp;&nbsp;&nbsp; --%>
		<%-- 			<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a> --%>
		<%-- 		</div> --%>
		<%-- 	</div> --%>
		<%-- </div> --%>
		
		<div id="remarkDiv" style="background-color: #E6E6E6; width: 870px; height: 90px;">
			<form role="form" style="position: relative;top: 10px; left: 10px;">
				<textarea id="remark" class="form-control" style="width: 850px; resize : none;" rows="2"  placeholder="添加备注..."></textarea>
				<p id="cancelAndSaveBtn" style="position: relative;left: 737px; top: 10px; display: none;">
					<button id="cancelBtn" type="button" class="btn btn-default">取消</button>
					<button type="button" class="btn btn-primary" id="saveRemarkBtn">保存</button>
				</p>
			</form>
		</div>
	</div>
	<div style="height: 200px;"></div>
</body>
</html>