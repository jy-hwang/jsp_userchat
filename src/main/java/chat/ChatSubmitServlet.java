package chat;

import java.io.IOException;
import java.net.URLDecoder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import util.StringUtils;

@WebServlet("/chatSubmitServlet")
public class ChatSubmitServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    request.setCharacterEncoding("UTF-8");
    response.setContentType("text/html;charset=UTF-8");

    String fromId = request.getParameter("fromId");
    String toId = request.getParameter("toId");
    String chatContent = request.getParameter("chatContent");

    if (StringUtils.isEmpty(request.getParameter("fromId"))
        || StringUtils.isEmpty(request.getParameter("toId"))
        || StringUtils.isEmpty(request.getParameter("chatContent"))) {
      response.getWriter().write("0");

    } else if (fromId.equals(toId)) {
      response.getWriter().write("-1");

    } else {
      fromId = URLDecoder.decode(fromId, "UTF-8");
      toId = URLDecoder.decode(toId, "UTF-8");

      HttpSession session = request.getSession();
      if (!fromId.equals((String) session.getAttribute("userId"))) {
        response.getWriter().write("");
        return;
      }

      chatContent = URLDecoder.decode(chatContent, "UTF-8");
      response.getWriter().write(new ChatDAO().submit(fromId, toId, chatContent) + "");

    }

  }

}
