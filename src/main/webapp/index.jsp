<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ page import="entity.ItemsEntity" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Things List Program</title>
</head>
<body>
<h1>Welcome to my Things List Program!</h1>

 <form method="POST" action="${pageContext.request.contextPath}/things-list">
  <p>Enter a number from the choices below:</p>
  <p>1. Add an Item</p>
  <p>2. Delete an Item</p>
  <p>3. List the Items</p>
  <p>4. Exit the Program</p>
  <p><input type='number' name='action' min='1' max='4' required></p>
  <p>Item: <input type='text' name='item'></p>
     <p>ID: <input type='number' name='id'></p>
  <p><input type='submit' value='Submit'></p>
</form>
<table>
  <thead>
  <tr>
    <th>ID</th>
    <th>Item</th>
  </tr>
  </thead>
  <tbody>
  <% List<ItemsEntity> items = (List<ItemsEntity>) request.getAttribute("items"); %>
  <% if (items != null && !items.isEmpty()) { %>
  <% for (ItemsEntity item : items) { %>
  <tr>
    <td><%= item.getId() %></td>
    <td><%= item.getItem() %></td>
  </tr>
  <% } %>
  <% } else { %>
  <tr>
    <td colspan="2">No items found</td>
  </tr>
  <% } %>
  </tbody>
</table>
</body>
</html>

