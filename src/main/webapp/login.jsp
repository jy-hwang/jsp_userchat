<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<%
  String userId = null;
  if (session.getAttribute("userId") != null){
    userId = (String) session.getAttribute("userId");
  }
  
  if (userId != null){
    session.setAttribute("messageType","오류 메시지");
    session.setAttribute("messageContent","현재 로그인이 되어있는 상태입니다.");
    response.sendRedirect("index.jsp");
    return;
  }
%>
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link rel="stylesheet" href="css/bootstrap.css" />
    <link rel="stylesheet" href="css/custom.css" />
    <title>JSP Ajax 실시간 회원제 채팅 서비스</title>
    <script src="js/jquery-3.7.1.js"></script>
    <script src="js/bootstrap.js"></script>
  </head>
  <body>
    <nav class="navbar navbar-default">
      <div class="navbar-header">
        <button
          class="navbar-toggle collapse"
          data-toggle="collapsed"
          data-target="#bs-example-navbar-collapse-1"
          aria-expanded="false"
        >
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
        </button>
         <a href="index.jsp" class="navbar-brand">실시간 회원제 채팅 서비스</a>
      </div>
      <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
        <ul class="nav navbar-nav">
          <li><a href="index.jsp">메인</a></li>
          <li><a href="find.jsp">친구찾기</a></li>
          <li><a href="box.jsp">메시지함<span id="unread" class="label label-info"></span></a></li>
          <li><a href="boardList.jsp">자유게시판</a></li>
        </ul>

<%
  if(userId == null){
%>

        <ul class="nav navbar-nav navbar-right">
          <li class="dropdown"
            ><a
              href="#"
              class="dropdown-toggle"
              data-toggle="dropdown"
              role="button"
              aria-haspopup="true"
              aria-expanded="false"
              >접속하기 <span class="caret"></span>
            </a>
            <ul class="dropdown-menu">
              <li class="active"><a href="login.jsp">로그인</a></li>
              <li><a href="join.jsp">회원가입</a></li>
            </ul>
          </li>
        </ul>

<%
  }
%>

      </div>
    </nav>
     <div class="container">
      <form method="post" action="./userLogin">
        <table class="table table-borderd table-hover" style="text-align: center; border: 1px solid #ddd">
          <thead>
          <tr>
            <th colspan ="2" ><h4>로그인 양식</h4></th>
          </tr>
          </thead>
          <tbody>
            <tr>
              <td style="width: 110px;"><h5>아이디</h5></td>
              <td><input class="form-control" type="text" id="userId" name="userId" maxlength="20" placeholder="아이디를 입력하세요" /></td>
            </tr>
            <tr>
              <td style="width: 110px;"><h5>비밀번호</h5></td>
              <td colspan="2"><input class="form-control" type="password" id="userPassword" name="userPassword" maxlength="20" placeholder="비밀번호를 입력하세요" /></td>
            </tr>
             <tr>
              <td style="text-align:left;" colspan="2">
              <input class="btn btn-primary pull-right" type="submit" value="로그인"/>
              </td>
            </tr>
          </tbody>
        
        </table>
      </form>
    </div>
   
<%
  String messageType = null;
  if(session.getAttribute("messageType") != null){
    messageType = (String) session.getAttribute("messageType");
  }

  String messageContent = null;
  if(session.getAttribute("messageContent") != null){
    messageContent = (String) session.getAttribute("messageContent");
  }
  
  if(messageContent != null) {
    
%>
<div class="modal fade" id="messageModal" tabindex = "-1" role="dialog" aria-hidden="true">
  <div class="vertical-alignment-helper">
    <div class="modal-dialog vertical-align-center">
      <div class="modal-content <% if(messageType.equals("오류 메시지")) out.println("panel-warning"); else out.println("panel-success"); %>">
        <div class="modal-header panel-heading">
          <button type="button" class="close" data-dismiss="modal">
            <span aria-hidden="true">&times</span>
            <span class="sr-only">Close</span>
          </button>
          <h4 class="modal-title">
          <%= messageType %>
          </h4>
        </div>
        <div class="modal-body">
          <%= messageContent %>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-primary" data-dismiss="modal">확인</button>
        </div>
      </div>
    </div>
  </div>
</div>
<script>
  $('#messageModal').modal('show');
</script>
<%
  session.removeAttribute("messageType");
  session.removeAttribute("messageContent");
  }
%>

<div class="modal fade" id="checkModal" tabindex = "-1" role="dialog" aria-hidden="true">
  <div class="vertical-alignment-helper">
    <div class="modal-dialog vertical-align-center">
      <div id="checkType" class="modal-content panel-info">
        <div class="modal-header panel-heading">
          <button type="button" class="close" data-dismiss="modal">
            <span aria-hidden="true">&times</span>
            <span class="sr-only">Close</span>
          </button>
          <h4 class="modal-title">확인 메시지</h4>
        </div>
        <div class="modal-body" id="checkMessage">
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-primary" data-dismiss="modal">확인</button>
        </div>
      </div>
    </div>
  </div>
</div>
  </body>
  <script>

  </script>
</html>
