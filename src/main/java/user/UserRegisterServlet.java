package user;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utils.StringUtils;

@WebServlet("/userRegisterServlet")
public class UserRegisterServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    request.setCharacterEncoding("UTF-8");
    response.setContentType("text/html;charset=UTF-8");

    String userId = request.getParameter("userId");
    String userPassword1 = request.getParameter("userPassword1");
    String userPassword2 = request.getParameter("userPassword2");
    String userName = request.getParameter("userName");
    String userAge = request.getParameter("userAge");
    String userGender = request.getParameter("userGender");
    String userEmail = request.getParameter("userEmail");

    if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(userPassword1)
        || StringUtils.isEmpty(userPassword2) || StringUtils.isEmpty(userName)
        || StringUtils.isEmpty(userAge) || StringUtils.isEmpty(userGender)
        || StringUtils.isEmpty(userEmail)) {

      request.getSession().setAttribute("messageType", "오류 메시지");
      request.getSession().setAttribute("messageContent", "모든 내용을 입력하세요");
      response.sendRedirect("join.jsp");
      return;
    }

    if (!userPassword1.equals(userPassword2)) {
      request.getSession().setAttribute("messageType", "오류 메시지");
      request.getSession().setAttribute("messageContent", "비밀번호가 일치하지 않습니다.");
      response.sendRedirect("join.jsp");
      return;
    }

    UserDTO userDTO = new UserDTO();

    userDTO.setUserId(userId);
    userDTO.setUserPassword(userPassword1);
    userDTO.setUserName(userName);
    userDTO.setUserAge(Integer.parseInt(userAge));
    userDTO.setUserGender(userGender);
    userDTO.setUserEmail(userEmail);

    int result = new UserDAO().register(userDTO);

    if (result == 1) {
      request.getSession().setAttribute("userId", userId);
      request.getSession().setAttribute("messageType", "성공 메시지");
      request.getSession().setAttribute("messageContent", "회원가입에 성공했습니다.");
      response.sendRedirect("index.jsp");

    } else {
      request.getSession().setAttribute("messageType", "오류 메시지");
      request.getSession().setAttribute("messageContent", "회원가입에 실패했습니다.");
      response.sendRedirect("join.jsp");
      return;
    }

  }

}
