package user;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utils.StringUtils;

@WebServlet("/userRegisterCheckServlet")
public class UserRegisterCheckServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    request.setCharacterEncoding("UTF-8");
    response.setContentType("text/html;charset=UTF-8");
    String userId = request.getParameter("userId");
    if (StringUtils.isEmpty(userId)) {
      response.getWriter().write("-1");
    }
    response.getWriter().write(new UserDAO().registerCheck(userId) + "");
  }
}
