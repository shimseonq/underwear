<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	<!DOCTYPE html>
		<html>
			<head>
			<meta charset="UTF-8" />
			<meta name="viewport"
				content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, userscalable=no" />
			<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
		<title>Login</title>
			<!-- 모바일 웹 페이지 설정 -->
			<link rel="shortcut icon" href="/resources/image/icon.png" />
			<link rel="apple-touch-icon" href="/resources/image/icon.png" />
			<!-- 모바일 웹 페이지 설정 끝 -->
			<!--[if lt IE 9]>
			<script src="/resources/include/js/html5shiv.js"></script>
			<![endif]-->
			<script type="text/javascript"
			src="/resources/include/js/jquery-1.12.4.min.js"></script>
			<script type="text/javascript" src="/resources/include/js/common.js"></script>
			<script type="text/javascript" src="/resources/include/js/login.js"></script>
			<script type="text/javascript">
		
			function errorCodeCheck(){
			var status = '<c:out value="${status}" />';
			if(status != ""){
		// 명확한 자료형 명시를 위해 status의 타입을 정수형으로 변환.
			switch (parseInt(status)) {
				case 1:
				alert("아이디 또는 비밀번호 일치 하지 않거나 존재하지 않는 \n회원입니다. 다시 로그인해 주세요.");
				break;
				case 6:
				alert("5번이상 로그인 시도로 30초동안 로그인 할 수 없습니다.\n잠시 후 다시 시도해 주세요");
				break;
				}
					}
			}
			
			
			
			
			</script>
		</head>
<body>
	<div class="contentContainer">
		<div class="well">
			<%-- 로그인 전 화면 --%>
			<c:if test="${login.c_id == null or login.c_id == ''}">
				<form id="idCheckForm" class="form-horizontal">
					<div class="form-group">
						<label for="c_name" class="col-sm-2 control-label">이름 </label>
						<div class="col-sm-4">
							<input type="text" id="c_name" name="c_name" class="form-control"
								placeholder="Name"> 
						</div>
						<p class="form-control-static error"></p>
					</div>
					<div class="form-group">
						<label for="c_email" class="col-sm-2 control-label">이메일 </label>
						<div class="col-sm-4">
							<input type="text" id="c_email" name="c_email"
								class="formcontrol" placeholder="Email">
						</div>
						<p class="form-control-static error"></p>

					</div>
					<div class="form-group">
						<div class="col-sm-offset-2 col-sm-6">
							<input type="button" value="아이디 찾기" id="idCheckBtn"
								class="btn btn-default" /> <input type="button" value="취소"
								id="joinBtn" class="btn btn-default" />
						</div>
					</div>
				</form>
			<!-- 비밀번호 -->
				<form id="passCheckForm" class="form-horizontal">
					<div class="form-group">
						<label for="c_id" class="col-sm-2 control-label">아이디 </label>
						<div class="col-sm-4">
							<input type="text" id="c_id" name="c_id" class="form-control"
								placeholder="ID">
						</div>
						<p class="form-control-static error"></p>
					</div>
					
					<div class="form-group">
						<label for="c_name" class="col-sm-2 control-label">이름 </label>
						<div class="col-sm-4">
							<input type="text" id="c_name" name="c_name" class="form-control"
								placeholder="Name">
						</div>
						<p class="form-control-static error"></p>
					</div>
					<div class="form-group">
						<label for="c_email" class="col-sm-2 control-label">이메일 </label>
						<div class="col-sm-4">
							<input type="text" id="c_email" name="c_email"
								class="formcontrol" placeholder="Email">
						</div>
						<p class="form-control-static error"></p>

					</div>
					<div class="form-group">
						<div class="col-sm-offset-2 col-sm-6">
							<input type="button" value="아이디 찾기" id="passCheckBtn"
								class="btn btn-default" /> <input type="button" value="비밀번호 찾기"
								id="joinBtn" class="btn btn-default" />
						</div>
					</div>
				</form>
			</c:if>
			<%-- 로그인 후 화면 --%>
			
			<c:if test="${login.c_id != null and login.c_id != ''}">
				<fieldset id="loginAfter">
				<legend>
					<br/><br/><br/><br/>
						<strong>[ ${login.c_name} ]님 반갑습니다</strong>
						<br/><br/><br/><br/>
					</legend>
				
					<span id="memberMenu" class="tac"> 
					  <a href="/">메인으로</a>&nbsp;&nbsp;&nbsp;
					<a href="/login/logout.do">로그아웃</a>&nbsp;&nbsp;&nbsp;
					 <a href="/member/modify.do">정보수정(비밀번호변경)</a>&nbsp;&nbsp;&nbsp;
					  <a href="/member/delete.do">회원탈퇴</a>
					  </span>
						<br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>
				</fieldset>
			</c:if>
		</div>
	</div>
</body>
</html>
