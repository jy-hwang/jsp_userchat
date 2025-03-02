<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<%
  String userId = null;
  if (session.getAttribute("userId") != null){
    userId = (String) session.getAttribute("userId");
  }
  
  if (userId == null){
    session.setAttribute("messageType","오류 메시지");
    session.setAttribute("messageContent","현재 로그인이 되어있지 않습니다.");
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
</head>
<body>

  <nav class="navbar navbar-default">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"
        aria-expanded="false">
        <span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
      </button>
      <a href="index.jsp" class="navbar-brand">실시간 회원제 채팅 서비스</a>
    </div>
    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
      <ul class="nav navbar-nav">
        <li><a href="index.jsp">메인</a></li>
        <li class="active"><a href="find.jsp">친구찾기</a></li>
        <li><a href="box.jsp">메시지함<span id="unread" class="label label-info"></span></a></li>
      </ul>

      <ul class="nav navbar-nav navbar-right">
        <li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
          aria-expanded="false">회원관리 <span class="caret"></span>
        </a>
          <ul class="dropdown-menu">
            <li><a href="updateInfo.jsp">회원정보수정</a></li>
            <li><a href="updateProfile.jsp">프로필 수정</a></li>
            <li><a href="logoutAction.jsp">로그아웃</a></li>
          </ul></li>
      </ul>
    </div>
  </nav>
  <div class="container">
    <table class="table table-bordered table-hover" style="text-align: center;
  border: 1px solid #ddd;">
      <thead>
        <tr>
          <th colspan="2"><h4>검색으로 친구찾기</h4></th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td style="width: 110px"><h5>친구아이디</h5></td>
          <td><input class="form-control" type="text" id="findId" maxlength="20" placeholder="찾을 아이디를 입력하세요" /></td>
        </tr>
        <tr>
          <td colspan="2"><button class="btn btn-primary pullright" onclick="findFunction();">검색</button></td>
        </tr>
      </tbody>
    </table>
  </div>

  <div class="container">
    <table id="friendResult" class="table table-bordered table-hover" style="text-align: center;
  border: 1px solid #ddd;">

    </table>
  </div>
  <%
  String messageType = null;
  if (session.getAttribute("messageType") != null) {
    messageType = (String) session.getAttribute("messageType");
  }

  String messageContent = null;
  if (session.getAttribute("messageContent") != null) {
    messageContent = (String) session.getAttribute("messageContent");
  }

  if (messageContent != null) {
  %>
  <div class="modal fade" id="messageModal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="vertical-alignment-helper">
      <div class="modal-dialog vertical-align-center">
        <div class="modal-content <% if(messageType.equals("오류 메시지")) out.println("panel-warning"); else out.println("panel-success"); %>">
          <div class="modal-header panel-heading">
            <button type="button" class="close" data-dismiss="modal">
              <span aria-hidden="true">&times</span> <span class="sr-only">Close</span>
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
  })
</script>
  <%
  }
%>

  <div class="modal fade" id="checkModal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="vertical-alignment-helper">
      <div class="modal-dialog vertical-align-center">
        <div id="checkType" class="modal-content panel-info">
          <div class="modal-header panel-heading">
            <button type="button" class="close" data-dismiss="modal">
              <span aria-hidden="true">&times</span> <span class="sr-only">Close</span>
            </button>
            <h4 class="modal-title">확인 메시지</h4>
          </div>
          <div class="modal-body" id="checkMessage"></div>
          <div class="modal-footer">
            <button type="button" class="btn btn-primary" data-dismiss="modal">확인</button>
          </div>
        </div>
      </div>
    </div>
  </div>

  <script type="text/javascript">
  function findFunction(){
    var userId = $("#findId").val();
    $.ajax({
      type : "post",
      url : './userFind',
      data : {userId : userId},
      success : function(result) {
        if(result == -1){
          $('#checkMessage').html('친구 찾기에 실패했습니다.');
          $('#checkType').attr('class','modal-content panel-warning');
          failFriend();
        }else {
          $('#checkMessage').html('친구 찾기에 성공했습니다..');
          $('#checkType').attr('class','modal-content panel-success');
          var data = JSON.parse(result);
          var profile = data.userProfile;
          getFriend(userId, profile);
        }
        $('#checkModal').modal('show');
      }
        
    });
  }

  function getFriend(userId, userProfile){
    $("#friendResult").html('<thead>'
        + '<tr>'
        + '<th><h4>검색결과</h4></th>'
        + '</tr>'
        + '</thead>'
        + '<tbody>'
        + '<tr>'
        + '<td style="text-align: center;">'
        + '<img class="media-object img-circle" style="max-width:300px; margin: 0 auto;"  src="' + userProfile + '" />'
        + '<h3>' + userId + '</h3><a href="chat.jsp?toId=' + encodeURIComponent(userId) + '"class="btn btn-primary pull-right;">' + '메시지보내기</a></td>'
        + '</tr>'
        + '</tbody>'
    
    );
    
  }

  function failFriend(){
    $("#friendResult").html('');
  }

  function getUnread(){
    $.ajax({
      type : "post",
      url : './chatUnread',
      data : {
        userId : encodeURIComponent("<%= userId %>")
      },
      success : function(result){
        if(result > 0){
          showUnread(result);
        } else{
          showUnread('');
        }
      }
    });
  }
  
  function getInfiniteUnread(){
    setInterval(function(){
      getUnread();
    }, 3000);
  }
  
  function showUnread(result){
    $("#unread").html(result);
  }
</script>
</body>
</html>
