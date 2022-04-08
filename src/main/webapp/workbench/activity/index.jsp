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
		// 为创建按钮，绑定事件，打开添加操作的模态窗口
		$("#addBtn").click(function (){


			/*
				操作模态窗口的方式
					拿到操作的模态窗口的Jquery对象，调用modal方法，为该方法传递参数(show【打开模态窗口】 , hide【关闭模态窗口】 )
			 */
			// alert(123);

			// 走后台，目的是为了取得用户信息列表，为所有者下拉框铺值

			$.ajax({
				url:"workbench/activity/getUserList.do",
				type:"post",
				dataType:"json",
				success:function(data){
					/*
						List<User> list

						data:
							[{用户1},{用户2},...]
					 */
					let html = ""
					// 遍历出来的每一个n（第二个参数），就是一个对象
					$.each(data,function(i,n){
						html += "<option value='"+n.id+"'>" + n.name + "</option>"
					})
					$("#create-owner").html(html);

					// 将当前登陆的用户，设置为下拉框默认的选项
					// 取得当前登陆用户的id
					let id = "${user.id}";
					$("#create-owner").val(id);

					// 所有者下拉框处理完毕之后，展现模态窗口
					$("#createActivityModal").modal("show")
				}
			})

		})

		// 为保存按钮绑定时间，执行添加操作
		$("#saveBtn").click(function(){
			$.ajax({
				url:"workbench/activity/save.do",
				data:{
					"owner":$.trim($("#create-owner").val()),
					"name":$.trim($("#create-activityName").val()),
					"startDate":$.trim($("#create-startDate").val()),
					"endDate":$.trim($("#create-endDate").val()),
					"cost":$.trim($("#create-cost").val()),
					"description":$.trim($("#create-description").val())
				},
				type:"post",
				dataType: "json",
				success:function(data){
					/*
						data
							{"success":true/false}
					 */
					if(data.success){
						// 添加成功
						// 刷新市场活动信息列表（局部刷新）

						// 清空添加操作模态窗口中的数据
						// 提交表单
						// $("#activityAddForm").submit();
						// pageList(1,2)

						/**
						 *  $("#activityPage").bs_pagination('getOption','currentPage'
						 *  	操作后停留在当前页
						 *  $("#activityPage").bs_pagination('getOption','rowsPerPage')
						 *  	操作后维持已经设置好的每页展现的记录数
						 *
						 * 	这两个参数不需要我们进行任何的修改操作，直接使用即可
						 */
						pageList(1,
								$("#activityPage").bs_pagination('getOption','rowsPerPage'));

						// $("#activityAddForm").reset(); 该方法无法使用，jquery没有提供reset方法
						// 因此将jqeury对象转换成原生js对象dom
						$("#activityAddForm")[0].reset();

						// 关闭添加操作的模态窗口
						$("#createActivityModal").modal("hide");
					}else {
						alert("添加市场活动失败")
					}
				}
			})
		})

		// 页面加载完毕后，需要触发一个方法
		// 默认展开页表的第一页，没页展示三条记录
		pageList(1,5)

		// 为查询按钮绑定事件，触发pageList方法
		$("#searchBtn").click(function(){
			/*
				点击查询按钮的时候，我们应该将搜索框中的信息先保存起来，保存到隐藏域中
			 */

			$("#hidden-name").val($.trim($("#search-name").val()))
			$("#hidden-owner").val($.trim($("#search-owner").val()))
			$("#hidden-startDate").val($.trim($("#search-startDate").val()))
			$("#hidden-endDate").val($.trim($("#search-endDate").val()))

			pageList($("#activityPage").bs_pagination('getOption','currentPage'),
					$("#activityPage").bs_pagination('getOption','rowsPerPage'));
		})

		// 为全选的复选框绑定事件，触发全选操作
		$("#selectAll").click(function(){
			$("input[name=xz]").prop("checked",this.checked)
		})

		// 动态生成的元素，是不能够以普通绑定事件的形式来进行操作
		/*
			动态生成的元素，我们要以on方法的形式来触发事件
			语法：
				$(需要绑定元素的有效外层元素).on(绑定事件的方式,需要绑定的元素的jquery对象,回调函数)
		 */
		$("#activityBody").on("click",$("input[name=xz]"),function (){
			$("#selectAll").prop("checked",$("input[name=xz]").length==$("input[name=xz]:checked").length)
		})

		// 为删除按钮绑定事件，执行市场活动删除操作
		$("#deleteBtn").click(function(){
			// 找到复选框中所有挑勾的复选框的jquery对象
			let $xz = $("input[name=xz]:checked");
			if($xz.length == 0){
				alert("请选择需要删除的记录");
			}else{

				if(confirm("确定删除所选中的记录吗")){

					// 一条或多条
					// 当使用同一key多个value时，不再使用json格式传递参数
					// 拼接参数
					let param ="";

					// 将$xz中每一个dom对象遍历出来，取其value值，就想当于取得了需要删除的记录的id
					$.each($xz,function(i,n){
						param += "id=" + n.value;
						// 如果不是最后一个元素，需要在后面追减一个&
						if(i < $xz.length - 1){
							param+="&";
						}
					})
					// alert(param)

					$.ajax({
						url:"workbench/activity/delete.do",
						data:param,
						type:"post",
						dataType:"json",
						success:function(data){
							/*
                                data:{success:true/false}
                             */

							if(data.success){

								// 删除成功后
								pageList($("#activityPage").bs_pagination('getOption','currentPage'),
										$("#activityPage").bs_pagination('getOption','rowsPerPage'));

							}else {
								alert("删除市场活动失败");
							}
						}
					})
				}
			}
		})

		$("#editBtn").click(function(){
			let $xz = $("input[name=xz]:checked");
			if($xz.length == 0){
				alert("请选择需要修改的记录")
			}else if($xz.length > 1){
				alert("仅能选择修改唯一记录")
			}else{
				$("#hidden-id").val($xz.val());
				$.ajax({
					url:"workbench/activity/getUserListAndActivity.do",
					data:{
						"id":$xz.val()
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
			}
		})

		/*
			为更新按钮绑定事件，执行市场活动的执行顺序
			在实际项目开发中，一定是按照先做添加再做修改的这种顺序
			为了节省开发事件，修改操作一般做到的都是添加的copy操作
		 */

		$("#updateBtn").click(function(){
			$.ajax({
				url:"workbench/activity/update.do",
				data:{
					"id":$("#hidden-id").val(),
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
						pageList($("#activityPage").bs_pagination('getOption','currentPage'),
								$("#activityPage").bs_pagination('getOption','rowsPerPage'));
						$ ("#editActivityModal").modal("hide");

					}else {
						alert("更新市场活动失败")
					}
				}
			})
		})
	});

	/*
		对于所有的关系型数据库，前端的分野相关操作的基础组件
		就是pageNo和pageSize
		pageNo：表示当前页的页码（从1开始）
		pageSize：每页展现的记录数

		pageList方法：就是发出ajax请求到后台，从后台取得最新的市场活动信息列表数据
					通过响应回来的

	 */
	/*
		pageList调用时机：
			1）当点击市场活动时，默认页面下方需要展示，调用
			2）添加，修改，删除后，都需要调用
			3）点击下方换页按钮时候，也需要调用
			4）点击查询按钮也需要，需要刷新市场活动列表
		以上为pageList方法制定了6个入口，当以上6个操作完成之后，均需要调用pageList方法刷新市场列表

	 */
	function pageList(pageNo,pageSize){

		// 将全选的复选框的勾干掉
		$("#selectAll").prop("checked",false);

		// 查询前，将隐藏域中保存的信息取出来，重新赋予到搜索框中
		$("#search-name").val($.trim($("#hidden-name").val()))
		$("#search-owner").val($.trim($("#hidden-owner").val()))
		$("#search-startDate").val($.trim($("#hidden-startDate").val()))
		$("#search-endDate").val($.trim($("#hidden-endDate").val()))

		$.ajax({
			url:"workbench/activity/pageList.do",
			data:{
				"pageNo":pageNo,
				"pageSize":pageSize,
				"name":$.trim($("#search-name").val()),
				"owner":$.trim($("#search-owner").val()),
				"startDate":$.trim($("#search-startDate").val()),
				"endDate":$.trim($("#search-endDate").val())
			},
			type:"post",
			dataType:"json",
			success:function(data){
				/*
					data
						我们需要的，市场活动信息列表
						[{市场活动1},{2},{3}] List<Activity> aList
						一会分页插件需要的，查询出来的总记录数
						{"total":100}
						{"total":100,"dataList":[{市场活动1},{2},{3}]}
				 */
				let html = "";

				// 每一个n就是市场活动对象
				$.each(data.dataList,function(i,n){
					html += '<tr class="active">';
					html += '<td><input type="checkbox" name="xz" value="'+n.id+'"/></td>';
					html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.do?id='+n.id+'\';">'+n.name+'</a></td>';
					html += '<td>'+n.owner+'</td>';
					html += '<td>'+n.startDate+'</td>';
					html += '<td>'+n.endDate+'</td>';
					html += '</tr>';
				})

				$("#activityBody").html(html);
				// 计算总页数
				let totalPages = data.total%pageSize==0?data.total/pageSize:parseInt(data.total/pageSize)+1

				// 数据处理完毕后，结合分页查询，对前端展示分页相关的信息
				$("#activityPage").bs_pagination({
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
					onChangePage : function(event, data){
						pageList(data.currentPage , data.rowsPerPage);
					}
				});
			}
		})

		// 为修改按钮绑定事件，打开修改操作的模态窗口

	}

</script>
</head>
<body>

	<input type="hidden" id="hidden-name"/>
	<input type="hidden" id="hidden-owner"/>
	<input type="hidden" id="hidden-startDate"/>
	<input type="hidden" id="hidden-endDate"/>

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form id="activityAddForm" class="form-horizontal" role="form">

						<div class="form-group">
							<label for="create-owner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-owner">

								</select>
							</div>
                            <label for="create-activityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-activityName">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startDate" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startDate" readonly>
							</div>
							<label for="create-endDate" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endDate" readonly>
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<%--
					 	data-dimiss="modal"：表示关闭窗口

					 --%>
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<input type="hidden" id="hidden-id">
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
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
								<%--
								 	关于文本域textarea：
								 		1）一定是要以标签对的形式来呈现,正常状态下标签对要紧紧的挨着
								 		2）textarea虽然是以标签对的形式来呈现的，但是他也是属于表单元素范畴
								 			我们所有的对于textarea的取值和赋值操作，应该同一使用val()方法

								 --%>
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
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
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
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control time" type="text" id="search-startDate" readonly/>
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control time" type="text" id="search-endDate" readonly/>
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default" id="searchBtn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
					<!--
						点击创建按钮，观察属性和属性值
						data-toggle="modal"：
							表示触发该按钮，将要打开一个模态窗口
						data-target="#createActivityModal"：
							表示要打开那个模态窗口，通过#id的方式找到该窗口

						现在我们是以属性和属性值的方式写在button元素当中，用来打开模态窗口，
						但是这样做是有问题：
							问题在于没有对按钮的功能进行扩充
						所以未来的实习项目开发，对于触发模态窗口的操作，一定不要写死在元素当中，
						应该有我们自己写在js代码当中

					-->
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
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityBody">
						 <%-- <tr class="active">  --%>
						 <%-- 	<td><input type="checkbox" /></td>  --%>
						 <%-- 	<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>  --%>
                         <%--     <td>zhangsan</td>  --%>
						 <%-- 	<td>2020-10-10</td>  --%>
						 <%-- 	<td>2020-10-20</td>  --%>
						 <%-- </tr>  --%>
                        <%-- <tr class="active"> --%>
                        <%--     <td><input type="checkbox" /></td> --%>
                        <%--     <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td> --%>
                        <%--     <td>zhangsan</td> --%>
                        <%--     <td>2020-10-10</td> --%>
                        <%--     <td>2020-10-20</td> --%>
                        <%-- </tr> --%>
					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 30px;">
				<div id="activityPage"></div>
			</div>
			
		</div>
		
	</div>
</body>
</html>