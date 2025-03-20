package user;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import utils.StringUtils;

@WebServlet("/userUpdateInfo")
public class UserUpdateInfoServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    request.setCharacterEncoding("UTF-8");
    response.setContentType("text/html;charset=UTF-8");

    String userId = request.getParameter("userId");
    String userName = request.getParameter("userName");
    String userAge = request.getParameter("userAge");
    String userGender = request.getParameter("userGender");
    String userEmail = request.getParameter("userEmail");

    if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(userName) || StringUtils.isEmpty(userAge)
        || StringUtils.isEmpty(userGender) || StringUtils.isEmpty(userEmail)) {

      request.getSession().setAttribute("messageType", "오류 메시지");
      request.getSession().setAttribute("messageContent", "모든 내용을 입력하세요");
      response.sendRedirect("updateInfo.jsp");
      return;
    }

    HttpSession session = request.getSession();
    if (!userId.equals((String) session.getAttribute("userId"))) {
      request.getSession().setAttribute("messageType", "오류 메시지");
      request.getSession().setAttribute("messageContent", "접근할 수 없습니다.");
      response.sendRedirect("index.jsp");
      return;
    }

    UserDTO userDTO = new UserDTO();

    userDTO.setUserId(userId);
    userDTO.setUserName(userName);
    userDTO.setUserAge(Integer.parseInt(userAge));
    userDTO.setUserGender(userGender);
    userDTO.setUserEmail(userEmail);

    int result = new UserDAO().updateInfo(userDTO);

    if (result == 1) {
      request.getSession().setAttribute("userId", userId);
      request.getSession().setAttribute("messageType", "성공 메시지");
      request.getSession().setAttribute("messageContent", "회원정보 수정에 성공했습니다.");
      response.sendRedirect("index.jsp");

    } else {
      request.getSession().setAttribute("messageType", "오류 메시지");
      request.getSession().setAttribute("messageContent", "데이터베이스 오류가 발생했습니다.");
      response.sendRedirect("updateInfo.jsp");
      return;
    }

  }

}
