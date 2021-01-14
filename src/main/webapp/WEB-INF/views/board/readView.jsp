<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<!-- JQuery 추가 -->
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<title>게시판</title>
</head>
<script type="text/javascript">
	$(document).ready(function(){
		
		// 무조건 bno 값 받아주기 위한 장치
		var formObj = $("form[name='readForm']");
		
		//수정
		$(".update_btn").on("click", function(){
			formObj.attr("action", "/board/updateView");
			formObj.attr("method", "get");
			formObj.submit();
		})
		
		// 삭제
		$(".delete_btn").on("click", function(){
			formObj.attr("action", "/board/delete");
			formObj.attr("method", "post");
			formObj.submit();
		})
		
		// 취소
		$(".list_btn").on("click", function(){
		
			location.href = "/board/list";
		})
		
	})

</script>
<body>

	<div id="root">
		<header>
			<h1>게시판</h1>
		</header>
		<hr />

		<nav>홈 - 글 작성</nav>
		<hr />

		<section id="container">
			<!-- 수정, 삭제 버튼 클릭하면 hidden type 방식으로 bno 값 넘겨주기 위한 form -->
			<form name="readForm" role="form" method="post">
				<input type="hidden" id="bno" name="bno" value="${read.bno}"/>
			</form>
				<table>
					<tbody>
						<tr>
							<td>
								<label for="bno">글 번호</label>
								<input type="text" id="bno" name="bno" value="${read.bno}" />
							</td>
						</tr>
						<tr>
							<td>
								<label for="title">제목</label>
								<input type="text" id="title" name="title" value="${read.title}" />
							</td>
						</tr>
						<tr>
							<td>
								<label for="content">내용</label>
								<textarea id="content" name="content"><c:out
										value="${read.content}"
									/></textarea>
							</td>
						</tr>
						<tr>
							<td>
								<label for="writer">작성자</label>
								<input type="text" id="writer" name="writer"
									value="${read.writer}"
								/>
							</td>
						</tr>
						<tr>
							<td>
								<label for="regdate">작성날짜</label>
								<fmt:formatDate value="${read.regdate}" pattern="yyyy-MM-dd" />
							</td>
						</tr>
					</tbody>
				</table>
		</section>
		<div>
			<button type="submit" class="update_btn">수정</button>
			<button type="submit" class="delete_btn">삭제</button>
			<button type="submit" class="list_btn">목록</button>
		</div>
		<hr />
	</div>
</body>
</html>