<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<%
  String userId = null;
  if (session.getAttribute("userId") != null){
    userId = (String) session.getAttribute("userId");
  }
  
  if (userId == null) {
    session.setAttribute("messageType", "오류 메시지");
    session.setAttribute("messageContent", "현재 로그인이 되어있지 않습니다.");
    response.sendRedirect("login.jsp");
    return;
  }
  
%>
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link rel="stylesheet" href="css/bootstrap.css" />
    <link rel="stylesheet" href="css/custom.css" />
    <link rel="stylesheet" href="css/chatbox.css" />
    <title>JSP Ajax 실시간 회원제 채팅 서비스</title>
    <script src="js/jquery-3.7.1.js"></script>
    <script src="js/bootstrap.js"></script>
      <script type="text/javascript">
    function getUnread() {
      $.ajax({
        type: "post",
        url: "./chatUnread",
        data: {
          userId: encodeURIComponent("<%= userId %>"),
        },
        success: function (result) {
          if (result > 0) {
            showUnread(result);
          } else {
            showUnread("");
          }
        },
      });
    }

    function getInfiniteUnread() {
      setInterval(function () {
        getUnread();
      }, 3000);
    }

    function showUnread(result) {
      $("#unread").html(result);
    }

    function chatBoxFunction() {
      var userId = "<%= userId %>";
      $.ajax({
        type: "post",
        url: "./chatBox",
        data: {
          userId: encodeURIComponent(userId),
        },
        success: function (data) {
          if (data == "") {
            return;
          } else {
            $("#boxTable").html("");
            var parsed = JSON.parse(data);
            var result = parsed.result;

            for (var i = 0; i < result.length; i++) {
             
              if (result[i][0].fromId == userId) {
                result[i][0].fromId = result[i][1].toId;
              } else {
                result[i][1].toId = result[i][0].fromId;
              }
              addBox(result[i][0].fromId, result[i][1].toId, result[i][2].chatContent, result[i][3].createdDate);
            }
          }
        },
      });
    }
    
    function addBox(lastId, toId, chatContent, createdDate){
      console.log('addBox');
      $('#boxTable').append('<tr onclick="location.href=\'chat.jsp?toId=' + encodeURIComponent(toId) + '\'">'
          + '<td  style="width:150px"><h5>' + lastId + '</h5></td>'
          + '<td>'
          + '<h5>' + chatContent + '</h5>'
          + '<div class="pull-right">' + createdDate +  '</div>'
          + '</td>'
          + '</tr>');
    }
    
    function getInfiniteBox(){
     setInterval(function(){
       chatBoxFunction();
     }, 3000);
      
    }
    
  </script>
  </head>
  <body>

  
    <nav class="navbar navbar-default">
      <div class="navbar-header">
        <button
          type="button"
          class="navbar-toggle collapsed"
          data-toggle="collapse"
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
          <li class="active"><a href="box.jsp">메시지함<span id="unread" class="label label-info"></span></a></li>
        </ul>

        <ul class="nav navbar-nav navbar-right">
          <li class="dropdown"
            ><a
              href="#"
              class="dropdown-toggle"
              data-toggle="dropdown"
              role="button"
              aria-haspopup="true"
              aria-expanded="false"
              >회원관리 <span class="caret"></span>
            </a>
             <ul class="dropdown-menu">
              <li><a href="logoutAction.jsp">로그아웃</a></li>
            </ul>
          </li>
        </ul>
      </div>
    </nav>
    <div class="container">
      <table class="table" style="margin: 0 auto;">
        <thead>
            <tr>
            <th><h4>주고받은 메시지 목록</h4></th>      
            </tr>
          
        
        </thead>
        <div style="overflow-y : auto; width: 100%; max-height: 450px;">
          <table class="table table-bordered table-hover" style="text-align: center; border: 1px solid #ddd;">
            <tbody id="boxTable">
            
            </tbody>
          </table>
        </div>
      </table>
      </table>
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

<%
  if(userId != null){
%>
<script type="text/javascript">
  $(function(){
    getUnread();
    getInfiniteUnread();
    chatBoxFunction();
    getInfiniteBox();
  })
</script>
<%
  }
%>

  </body>
</html>
