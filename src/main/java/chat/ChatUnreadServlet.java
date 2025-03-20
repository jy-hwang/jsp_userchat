package chat;

import java.io.IOException;
import java.net.URLDecoder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import utils.StringUtils;

@WebServlet("/chatUnread")
public class ChatUnreadServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    request.setCharacterEncoding("UTF-8");
    response.setContentType("text/html;charset=UTF-8");

    String userId = request.getParameter("userId");

    if (StringUtils.isEmpty(userId)) {
      response.getWriter().write("0");
    } else {
      userId = URLDecoder.decode(userId, "UTF-8");

      HttpSession session = request.getSession();
      if (!userId.equals((String) session.getAttribute("userId"))) {
        response.getWriter().write("");
        return;
      }

      response.getWriter().write(new ChatDAO().getAllCountUnreadChat(userId) + "");
    }

  }

}
