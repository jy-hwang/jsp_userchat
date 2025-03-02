package user;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utils.StringUtils;

@WebServlet("/userLoginServlet")
public class UserLoginServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    request.setCharacterEncoding("UTF-8");
    response.setContentType("text/html;charset=UTF-8");

    String userId = request.getParameter("userId");
    String userPassword = request.getParameter("userPassword");

    if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(userPassword)) {
      request.getSession().setAttribute("messageType", "오류 메시지");
      request.getSession().setAttribute("messageContent", "모든 내용을 입력해주세요.");
      response.sendRedirect("login.jsp");
      return;
    }

    int result = new UserDAO().login(userId, userPassword);

    if (result == 1) {
      request.getSession().setAttribute("userId", userId);
      request.getSession().setAttribute("messageType", "성공 메시지");
      request.getSession().setAttribute("messageContent", "로그인에 성공했습니다.");
      response.sendRedirect("index.jsp");
    } else if (result == 2) {
      request.getSession().setAttribute("messageType", "오류 메시지");
      request.getSession().setAttribute("messageContent", "비밀번호를 다시 확인하세요.");
      response.sendRedirect("login.jsp");
    } else if (result == 0) {
      request.getSession().setAttribute("messageType", "오류 메시지");
      request.getSession().setAttribute("messageContent", "아이디를 다시 확인하세요.");
      response.sendRedirect("login.jsp");
    } else if (result == -1) {
      request.getSession().setAttribute("messageType", "오류 메시지");
      request.getSession().setAttribute("messageContent", "데이터베이스 오류가 발생했습니다.");
      response.sendRedirect("login.jsp");
    }

    response.getWriter().write(new UserDAO().registerCheck(userId) + "");

  }

}
