package user;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import utils.StringUtils;

@WebServlet("/userUpdatePassword")
public class UserUpdatePasswordServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    request.setCharacterEncoding("UTF-8");
    response.setContentType("text/html;charset=UTF-8");

    HttpSession session = request.getSession();

    String userId = request.getParameter("userId");
    if (!userId.equals((String) session.getAttribute("userId"))) {
      session.setAttribute("messageType", "오류 메시지");
      session.setAttribute("messageContent", "접근할 수 없습니다.");
      response.sendRedirect("index.jsp");
      return;
    }

    String oldPassword = request.getParameter("oldPassword");
    String newPassword1 = request.getParameter("newPassword1");
    String newPassword2 = request.getParameter("newPassword2");

    if (StringUtils.isEmpty(oldPassword) || StringUtils.isEmpty(newPassword1)
        || StringUtils.isEmpty(newPassword2)) {
      session.setAttribute("messageType", "오류 메시지");
      session.setAttribute("messageContent", "모든 내용을 입력하세요");
      response.sendRedirect("updateInfo.jsp");
      return;
    }

    UserDAO userDAO = new UserDAO();
    String oldDbPassword = userDAO.getPassword(userId);

    if (!oldPassword.equals(oldDbPassword)) {
      session.setAttribute("messageType", "오류 메시지");
      session.setAttribute("messageContent", "비밀번호를 다시 확인하세요.");
      response.sendRedirect("updatePassword.jsp");
      return;
    }

    if (!newPassword1.equals(newPassword2)) {
      session.setAttribute("messageType", "오류 메시지");
      session.setAttribute("messageContent", "비밀번호를 다시 확인하세요.");
      response.sendRedirect("updatePassword.jsp");
      return;
    }

    int result = userDAO.updatePassword(userId, oldPassword, newPassword1);

    if (result == 1) {
      request.getSession().setAttribute("userId", userId);
      request.getSession().setAttribute("messageType", "성공 메시지");
      request.getSession().setAttribute("messageContent", "비밀번호 수정에 성공했습니다.");
      response.sendRedirect("index.jsp");

    } else {
      request.getSession().setAttribute("messageType", "오류 메시지");
      request.getSession().setAttribute("messageContent", "데이터베이스 오류가 발생했습니다.");
      response.sendRedirect("updatePassword.jsp");
      return;
    }

  }

}
