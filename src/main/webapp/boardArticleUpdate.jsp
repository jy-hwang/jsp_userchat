<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ page import="utils.StringUtils" %>
<%@ page import="user.UserDTO" %>
<%@ page import="user.UserDAO" %>
<%@ page import="board.BoardDTO" %>
<%@ page import="board.BoardDAO" %>

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
  
  UserDTO user = new UserDAO().getUser(userId);
  
  String tempBoardNo = request.getParameter("boardNo");
  if(StringUtils.isEmpty(tempBoardNo)){
    session.setAttribute("messageType", "오류 메시지");
    session.setAttribute("messageContent", "게시물을 선택해주세요.");
    response.sendRedirect("index.jsp");
    return;    
  }
  int boardNo = Integer.parseInt(tempBoardNo);
  
  BoardDAO boardDAO = new BoardDAO();
  BoardDTO board = boardDAO.getOne(boardNo);
  if(!userId.equals(board.getUserId())){
    session.setAttribute("messageType", "오류 메시지");
    session.setAttribute("messageContent", "접근할 수 없습니다.");
    response.sendRedirect("index.jsp");
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
          <li><a href="box.jsp">메시지함<span id="unread" class="label label-info"></span></a></li>
          <li class="active"><a href="boardList.jsp">자유게시판</a></li>
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
              <li><a href="updateInfo.jsp">회원정보수정</a></li>
              <li><a href="updatePassword.jsp">비밀번호 변경</a></li>
              <li><a href="updateProfile.jsp">프로필 수정</a></li>
              <li><a href="logoutAction.jsp">로그아웃</a></li>
            </ul>
          </li>
        </ul>
      </div>
    </nav>
    <div class="container">
      <form method="post" action="./boardArticleUpdate" enctype="multipart/form-data">
        <table class="table table-borderd table-hover" style="text-align: center; border: 1px solid #ddd">
          <thead>
          <tr>
            <th colspan ="3" ><h4>게시물 수정 양식</h4></th>
          </tr>
          </thead>
          <tbody>
            <tr>
              <td style="width: 110px;"><h5>아이디</h5></td>
              <td><h5><%= user.getUserId() %></h5>
                <input type="hidden" name="userId" value="<%= user.getUserId() %>"/>
                <input type="hidden" name="boardNo" value="<%= boardNo %>"/>
              </td>
            </tr>
            
            <tr>
              <td style="width: 110px;"><h5>글 제목</h5></td>
              <td><input class="form-control" type="text" name="boardTitle" placeholder="글 제목을 입력하세요." maxlength="50" value="<%=board.getBoardTitle()%>" required/></td>
            </tr>
            
            <tr>
              <td style="width: 110px;"><h5>글 내용</h5></td>
              <td><textarea class="form-control" name="boardContent" placeholder="글 내용을 입력하세요." rows="10" maxlength="2048" required><%=board.getBoardContent()%></textarea></td>
            </tr>
           
            <tr>
              <td style="width: 110px;"><h5>파일 업로드</h5></td>
              <td colspan="2">
                <input type="file" name="boardFile" class="file">
                <div class="input-group col-xs-12">
                  <span class="input-group-addon"><i class="glyphicon glyphicon-picture"></i></span>
                  <input type="text" class="form-control input-lg" disabled placeholder="<%= board.getBoardFile() %>">
                  <span class="input-group-btn">
                    <button class="browse btn btn-primary input-lg" type="button"><i class="glyphicon glyphicon-search"></i>파일 찾기</button>
                  </span>
                </div>
              </td>
            </tr>
             <tr>
              <td style="text-align:left;" colspan="3"><h5 style="color:red;"></h5>
              <input class="btn btn-primary pull-right" type="submit" value="수정"/>
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
<script type="text/javascript">
 $(document).on('click','.browse', function(){
  var file = $(this).parent().parent().parent().find('.file');
  file.trigger('click');
 });
 
 $(document).on('change','.file',function(){
  $(this).parent().find('.form-control').val($(this).val().replace(/c:\\fakepath\\/i,'')); 
 });
</script>
  </body>
</html>
