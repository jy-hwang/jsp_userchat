<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%
String userId = null;
if (session.getAttribute("userId") != null) {
	userId = (String) session.getAttribute("userId");
}

String toId = null;
if (request.getParameter("toId") != null) {
	toId = (String) request.getParameter("toId");
}

if (userId == null) {
	session.setAttribute("messageType", "오류 메시지");
	session.setAttribute("messageContent", "현재 로그인이 되어있지 않습니다.");
	response.sendRedirect("index.jsp");
	return;
}

if (toId == null) {
	session.setAttribute("messageType", "오류 메시지");
	session.setAttribute("messageContent", "대화 상대가 지정되지 않았습니다.");
	response.sendRedirect("index.jsp");
	return;
}
%>
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
      <button class="navbar-toggle collapse" data-toggle="collapsed" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
        <span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
      </button>
      <a href="index.jsp" class="navbar-brand">실시간 회원제 채팅 서비스</a>
    </div>
    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
      <ul class="nav navbar-nav">
        <li><a href="index.jsp">메인</a></li>
        <li><a href="find.jsp">친구찾기</a></li>
      </ul>

      <%
      if (userId != null) {
      %>
      <ul class="nav navbar-nav navbar-right">
        <li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
          aria-expanded="false">회원관리 <span class="caret"></span>
        </a>
          <ul class="dropdown-menu">
            <li><a href="logoutAction.jsp">로그아웃</a></li>
          </ul></li>
      </ul>

      <%
      }
      %>

    </div>
  </nav>
  <div class="container bootstrap snippet">
    <div class="row">
      <div class="col-xs-12">
        <div class="portlet portlet-default">
          <div class="portlet-heading">
            <div class="portlet-title">
              <h4>
                <i class="fa fa-circle text-green"></i>실시간 채팅방
              </h4>
            </div>
            <div class="clearfix"></div>
          </div>
          <div id="chat" class="panel-collapse collapse in">
            <div id="chatList" class="portlet-body chat-widget" style="overflow-y: auto; width: auto; height: 600px;"></div>
            <div class="portlet-footer">
              <div class="row" style="height: 90px">
                <div class="form-group col-xs-10">
                  <textarea style="height: 80px;" id="chatContent" class="form-control" placeholder="메시지를 입력하세요" maxlength="100"></textarea>
                </div>
                <div class="form-group col-xs-2">
                  <button type="button" class="btn btn-default pull-right" onclick="submitFunction();">전송</button>
                  <div class="clearfix"></div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div class="alert alert-success" id="successMessage" style="display: none;">
    <strong>메시지 전송에 성공했습니다.</strong>
  </div>
  <div class="alert alert-danger" id="dangerMessage" style="display: none;">
    <strong>이름과 내용을 모두 입력해주세요.</strong>
  </div>
  <div class="alert alert-warning" id="warningMessage" style="display: none;">
    <strong>데이터베이스 오류가 발생했습니다.</strong>
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
        <div class="modal-content <%if (messageType.equals("오류 메시지"))	out.println("panel-warning");else	out.println("panel-success");%>">
          <div class="modal-header panel-heading">
            <button type="button" class="close" data-dismiss="modal">
              <span aria-hidden="true">&times</span> <span class="sr-only">Close</span>
            </button>
            <h4 class="modal-title">
              <%=messageType%>
            </h4>
          </div>
          <div class="modal-body">
            <%=messageContent%>
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
</body>
<script type="text/javascript">
  function autoClosingAlert(selector, delay){
    var alert = $(selector).alert();
    alert.show();
    window.setTimeout(function(){
      alert.hide();
    }, delay)
  }
  
  function submitFunction(){
    var fromId = '<%=userId%>';
    var toId = '<%=toId%>';
    var chatContent = $('#chatContent').val();
    console.log(fromId, toId, chatContent);
    $.ajax({
      type : "post",
      url : './chatSubmitServlet',
      data : {
        fromId : encodeURIComponent(fromId),
        toId : encodeURIComponent(toId),
        chatContent : encodeURIComponent(chatContent)
      },
      success : function(result) {
        if (result == 1) {
          autoClosingAlert('#successMessage', 2000);
        } else if (result == 0) {
          autoClosingAlert('#dangerMessage', 2000);
        } else {
          autoClosingAlert('#warningMessage', 2000);
        }
      }
    });
    $('#chatContent').val('');
  }
  
  var lastNo = 0;
  function chatListFunction(type){
    var fromId = '<%=userId%>';
    var toId = '<%=toId%>';
  
    $.ajax({
      type : "post",
      url : './chatListServlet',
      data : {
        fromId : encodeURIComponent(fromId),
        toId : encodeURIComponent(toId),
        listType : type
      },
      success : function(data) {
        if (data == "") {
          return;
        }

        var parsed = JSON.parse(data);
        var result = parsed.result;

        for (var i = 0; i < result.length; i++) {
          addChat(result[i][0].fromId, result[i][2].chatContent,
              result[i][3].createdDate);
        }

        lastNo = Number(parsed.last);
      }
    });
  }

  function addChat(chatName, chatContent, chatTime) {
    $('#chatList')
        .append(
            '<div class="row">'
                + '<div class="col-lg-12">'
                + '<div class="media">'
                + '<a class="pull-left" href="#">'
                + '<img class="media-object img-circle" style="width: 30px; height: 30px;" src="images/icon.png" alf="">'
                + '</a>' + '<div class="media-body">'
                + '<h4 class="media-heading">' + chatName
                + '<span class="small pull-right">' + chatTime + '</span>'
                + '</h4>'+'<p>'+ chatContent + '</p>' + '</div>' + '</div>' + '</div>' + '</div>'
                + '<hr>');

  $('#chatList').scrollTop($('#chatList')[0].scrollHeight);
  }
  
  function getInfiniteChat(){
    setInterval(function(){
      chatListFunction(lastNo);
    }, 3000);
  }
  
</script>
<script type="text/javascript">
  $(document).ready(function(){
    chatListFunction('ten');
    getInfiniteChat();
  });
</script>

</html>
