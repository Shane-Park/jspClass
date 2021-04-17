<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<style>
body{
	padding : 40px;
}
img{
	height : 100px;
}
</style>
<meta charset="UTF-8">
<title>Insert title here</title>
<jsp:include page="/includee/preScript.jsp"/>
</head>
<body>
<h4>ALBA FORM</h4>
<form method="post" enctype="multipart/form-data">
	<div class="form-group">
		<label for="al_name">이름:</label>
		<input type="text" class="form-control" placeholder="Enter name" name="al_name" required>
	</div>
	<div class="form-group">
		<label for="profile">프로필 사진</label>
		<input type="file" accept="image/*" class="form-control-file" name="profile" id="profile">
		<div id="preview"></div>
	</div>
	<div class="form-group">
		<label for="al_age">나이:</label>
		<input type="number" class="form-control" placeholder="Enter age" name="al_age" required>
	</div>
	<div class="form-group">
		<label for="al_zip">우편번호:</label>
		<input type="text" class="form-control" placeholder="35036" name="al_zip" required>
	</div>
	<div class="form-group">
		<label for="al_addr1">주소:</label>
		<input type="text" class="form-control" placeholder="대전광역시" name="al_addr1" required>
	</div>
	<div class="form-group">
		<label for="al_addr2">상세주소:</label>
		<input type="text" class="form-control" placeholder="중구 대흥동 영민빌딩" name="al_addr2" required>
	</div>
	<div class="form-group">
		<label for="al_hp">전화번호:</label>
		<input type="text" class="form-control" placeholder="Enter phone number" name="al_hp" required>
	</div>
	<div class="form-group">
		<label for="grade">학력:</label>
 		<select name="gr_code" required>
     	<option value>학력 선택</option>
     	<c:forEach items="${gradeList}" var="grade">
     		<c:set var="selected" value="${grade.gr_code eq alba.gr_code ? 'selected' : ''}"></c:set>
     		<option value="${grade[gr_code]}" ${selected} >
     			${grade.gr_name }
     		</option>
     	</c:forEach>
 		</select>
	</div>
 	<div class="form-group">
		<label></label>
		 <div class="form-check form-check-inline">
		  <input class="form-check-input" type="radio" name="al_gen" value="M" id="male" >
		  <label class="form-check-label" for="male">남</label>
		 </div>
		<div class="form-check form-check-inline">
		  <input class="form-check-input" type="radio" name="al_gen" value="F" id="female" checked>
		  <label class="form-check-label" for="female">여</label>
		</div>
	</div>
	<div class="form-group">
		<label for="al_mail">이메일:</label>
		<input type="text" class="form-control" placeholder="Enter email" name="al_mail">
	</div>
	<div class="form-group">
		<label for="al_career">경력:</label>
		<textarea class="form-control" placeholder="Your work experience" name="al_career" rows="4"></textarea>
	</div>
		<label for="grade">보유자격증:</label>
   	<c:forEach items="${licenseList}" var="license">
   		<c:set var="selected" value=" "></c:set>
		<div class="form-check">
			<input class="form-check-input" type="checkbox" id="${license.lic_code}" value="${license.lic_code}" >
			<label class="form-check-label" for="${license.lic_code}">
				${license.lic_name }
			</label>
		</div>
   	</c:forEach>
	<div class="form-group">
		<label for="al_desc">자기소개:</label>
		<textarea class="form-control" placeholder="Anything about you" name="al_desc" rows="4"></textarea>
	</div>
  
	<button type="submit" class="btn btn-primary">Submit</button>
	<button type="button" onclick="location.href='albaList.do';" class="btn btn-danger">Cancel</button>
</form>
<script type="text/javascript">
$(function(){
	// 이미지 업로드시 미리보기 이벤트
	$("#profile").on("change", handleImgsFilesSelect);
})
let profile_file = [];
function handleImgsFilesSelect(e) {
	var files = e.target.files;
	var filesArr = Array.prototype.slice.call(files);
	
	filesArr.forEach(function(f) {
		if(!f.type.match("image.*")) {
			alert("이미지만 업로드 하실 수 있습니다.");
			return;
		}

		profile_file.push(f);

		var reader = new FileReader();
		reader.onload = function(e) {
			var img_html = '<img src=\"' + e.target.result + '\" />';
			$("#preview").html(img_html);
		}
		reader.readAsDataURL(f);
	});
}
</script>
<jsp:include page="/includee/postScript.jsp"/>
</body>
</html>