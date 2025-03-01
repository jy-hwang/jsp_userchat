package chat;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import util.StringUtils;

@WebServlet("/chatBoxServlet")
public class ChatBoxServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    request.setCharacterEncoding("UTF-8");
    response.setContentType("text/html;charset=UTF-8");

    String userId = request.getParameter("userId");

    if (StringUtils.isEmpty(userId)) {
      response.getWriter().write("");
    } else {
      try {
        userId = URLDecoder.decode(userId, "UTF-8");
        response.getWriter().write(getBox(userId) + "");

      } catch (Exception e) {
        response.getWriter().write("");
      }
    }

  }

  public String getBox(String userId) {
    StringBuffer result = new StringBuffer("");
    // {"result":[
    result.append("{\"result\":[");
    ChatDAO chatDAO = new ChatDAO();
    ArrayList<ChatDTO> chatList = chatDAO.getBox(userId);
    if (chatList.size() == 0) {
      return "";
    } else {
      for (int i = 0; i < chatList.size(); i++) {
        // [{"fromId" : " + chatList.get(i).getFromId() + "},
        result.append("[{\"fromId\" : \"" + chatList.get(i).getFromId() + "\"},");
        result.append("{\"toId\" : \"" + chatList.get(i).getToId() + "\"},");
        result.append("{\"chatContent\" : \"" + chatList.get(i).getChatContent() + "\"},");
        result.append("{\"createdDate\" : \"" + chatList.get(i).getCreatedDate() + "\"}]");
        if (i != chatList.size() - 1) {
          result.append(",");
        }
      }
      // ], "last" : ""+ chatList.get(chatList.size() -1).getChatNo() + ""}
      result.append(" ], \"last\" : \"" + chatList.get(chatList.size() - 1).getChatNo() + "\"}");

      return result.toString();
    }
    
  }
  
}
