<html>
<head>
<%@ page import ="java.sql.*, javax.sql.*, java.io.*, javax.naming.InitialContext, javax.naming.Context" %>
<meta charset="UTF-8">
</head>
<body>
<%
  Context initCtx = new InitialContext();
  Context envCtx = (Context)initCtx.lookup("java:/comp/env");
  DataSource ds = (DataSource) envCtx.lookup("jdbc/UserChat");

  Connection conn = ds.getConnection();
  Statement stmt = conn.createStatement();
  ResultSet rset = stmt.executeQuery("SELECT VERSION();");
  
  while(rset.next()) {
    out.println("MariaDB Version : " +rset.getString("version()"));
  }
  
  rset.close();
  stmt.close();
  conn.close();
  initCtx.close();

%>
</body>
</html>
