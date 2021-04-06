<%@page import="kr.or.ddit.vo.ProdVO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<style>
table, th, td{
	border : 1px solid black
}
</style>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
	ProdVO prod = (ProdVO)request.getAttribute("prod");
%>
<table>
	<tr>
		<th>상품코드</th>
		<td><%=prod.getProd_id() %></td>
	</tr>
	<tr>
		<th>상품명</th>
		<td><%=prod.getProd_name() %></td>
	</tr>
	<tr>
		<th>상품가격</th>
		<td><%=prod.getProd_price() %></td>
	</tr>
	<tr>
		<th>상품간단설명</th>
		<td><%=prod.getProd_outline() %></td>
	</tr>
	<tr>
		<th>상품상세설명</th>
		<td><%=prod.getProd_detail() %></td>
	</tr>
	<tr>
		<th>상품재고</th>
		<td><%=prod.getProd_totalstock() %></td>
	</tr>
	<tr>
		<th>상품주의</th>
		<td><%=prod.getProd_delivery() %></td>
	</tr>
	<tr>
		<th>거래처 정보</th>
		<td>
			<table>
				<thead>
					<tr>
						<th>거래처명</th>
						<th>담당자명</th>
						<th>연락처</th>
						<th>주소1</th>
					</tr>
					<tr>
						<td><%=prod.getBuyer().getBuyer_name() %></td>
						<td><%=prod.getBuyer().getBuyer_charger() %></td>
						<td><%=prod.getBuyer().getBuyer_comtel() %></td>
						<td><%=prod.getBuyer().getBuyer_add1() %></td>
					</tr>
				</thead>
			</table>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<button type="button" onClick="window.location='<%=request.getContextPath()%>/prod/prodList.do'">상품목록으로</button>
		</td>
	</tr>
</table>
</body>
</html>