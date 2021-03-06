<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<script defer type="text/javascript" src="<%=request.getContextPath() %>/js/prod/prodList.js"></script>
</head>
<body>
<form id="searchForm">
	<select name="prod_lgu">
		<option value>상품분류</option>
		<c:forEach items="${lprodList }" var="lprod">
			<option value="${lprod.loprod_gu }">${lprod.lprod_nm }</option>
		</c:forEach>
	</select>
	<select name="prod_buyer">
		<option value>거래처선택</option>
		<c:forEach items="${buyerList}" var="buyer">
			<option class="${buyer.buyer_lgu}" value="${buyer.buyer_id }">${buyer.buyer_name}</option>
		</c:forEach>
	</select>
	<input type="text" name="prod_name"/>
	<input type="hidden" name="page"/>
	<input type="submit" value="검색"/>
	<input type="button" onclick="location.href='prodInsert.do'"value="신규등록" id="newBtn"/>
</form>
<table>
	<thead>
		<tr>
			<th>No.</th>
			<th>상품코드</th>
			<th>상품분류명</th>
			<th>상품명</th>
			<th>거래처명</th>
			<th>구매가</th>
			<th>판매가</th>
			<th>마일리지</th>
		</tr>
	</thead>
	<tbody id="listBody">
		
	</tbody>
	<tfoot>
		<tr>
			<td colspan="8" id="pagingArea">
			</td>
		</tr>
	</tfoot>
</table>




