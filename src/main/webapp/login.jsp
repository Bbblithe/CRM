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
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	<script type="text/javascript">
		$(function (){

			if(window.top != window){
				window.top.location = window.location;
			}

			// 让页面加载完毕之后在登陆框中聚焦,将用户文本框的内容清空
			$("#loginAct").val("");
			$("#loginAct").focus();

			// // 当账号或者密码获得焦点时，清除提示消息
            // $("#loginAct").focus(function(){
            //     $("#msg").val("");
            // })
            //
            // $("#loginPwd").focus(function (){
            //     $("#msg").val("");
            // })

			// 为登陆按钮绑定事件，执行登陆操作
			$("#submit").click(function(){
				// alert("执行验证登陆")
				login()
			})

			// 为当前登陆页窗口绑定按键事件
			// event：参数可以取得按键值
			$(window).keydown(function (event){
				// alert(event.keyCode);
				// 表示按了会车按键
				if(event.keyCode == 13){
					login()
				}
			})
		})

		// 普通的自定义function方法，写在$(function(){})的外面
		function login(){
			// alert("登陆操作")

			// 验证账号密码不能为空
			// 将文本中的左右空格去掉，使用$.trim(文本)
			var loginAct = $.trim($("#loginAct").val());
			var loginPwd = $.trim($("#loginPwd").val());

			if(loginAct == "" || loginPwd == "") {
				$("#msg").html("您的账号密码不能为空！")
				// 如果账号密码为空，则终止
				return false;
			}

			// 去后台验证登陆相关操作
			$.ajax({
				url:"settings/user/login.do",
				data:{
					"loginAct":loginAct,
					"loginPwd":loginPwd
				},
				type:"post",
				dataType:"json",
				success:function(result){
					/*
					data
						{"success":true/false,"msg":"那里错了"}
					 */
					// alert("undefine");

					// 登陆成功
					if(result.success){
						// 跳转到工作台
						window.location.href = "workbench/index.jsp"
					}else{
						$("#msg").html(result.msg);
					}
				}
			})
		}

	</script>
</head>
<body>
	<div style="position: absolute; top: 0px; left: 0px; width: 58%;">
		<img src="image/we.jpeg" style="width: 100%; height: 90%; position: relative; top: 50px;">
	</div>
	<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
		<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRM &nbsp;<span style="font-size: 12px;">&copy;2022 小波</span></div>
	</div>
	
	<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
		<div style="position: absolute; top: 0px; right: 60px;">
			<div class="page-header">
				<h1>登录</h1>
			</div>
			<form action="user/login.do" class="form-horizontal" role="form" method="post">
				<div class="form-group form-group-lg">
					<div style="width: 350px;">
						<input class="form-control" type="text" placeholder="用户名" name="loginAct" id="loginAct">
					</div>
					<div style="width: 350px; position: relative;top: 20px;">
						<input class="form-control" type="password" placeholder="密码" name="loginPwd" id="loginPwd">
					</div>
					<div class="checkbox"  style="position: relative;top: 30px; left: 10px;">
						
							<span id="msg" style="color: orangered;"></span>
						
					</div>
					<!--
						注意：按钮写在form表单中，默认的行为就是提交表单
						一定要将按钮的类型设置成button
						按钮所触发的行为，由自己决定
					-->
					<button type="button" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;" id="submit">登录</button>
				</div>
			</form>
		</div>
	</div>
</body>
</html>