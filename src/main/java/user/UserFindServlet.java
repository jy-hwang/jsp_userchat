package user;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utils.StringUtils;

@WebServlet("/userFind")
public class UserFindServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    request.setCharacterEncoding("UTF-8");
    response.setContentType("text/html;charset=UTF-8");

    String userId = request.getParameter("userId");

    if (StringUtils.isEmpty(userId)) {
      response.getWriter().write("-1");
    } else if (new UserDAO().registerCheck(userId) == 1) {
      try {
        response.getWriter().write(find(userId));
      } catch (Exception e) {
        System.out.println(Thread.currentThread().getStackTrace()[1].getClassName() + " : " + e.getMessage());
        response.getWriter().write("-1");
      }
    } else {
      response.getWriter().write("-1");
    }
  }

  public String find(String userId) throws Exception {
    StringBuffer result = new StringBuffer("");

    // {"userProfile" : ""}
    result.append("{\"userProfile\" : \"" + new UserDAO().getUserProfile(userId) + "\"}");

    return result.toString();
  }
}
