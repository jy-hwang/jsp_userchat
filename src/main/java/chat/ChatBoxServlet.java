package chat;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
        HttpSession session = request.getSession();
        if(!userId.equals((String) session.getAttribute("userId"))) {
          response.getWriter().write("");
          return;
        }
        
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
      for (int i = chatList.size() -1 ; i >= 0; i--) {
        String unreadCount = "";
        
        if(userId.equals(chatList.get(i).getToId())) {
          int unreadCountTemp = chatDAO.getCountUnreadChat(chatList.get(i).getFromId(), userId);
          unreadCount =  unreadCountTemp > 0 ? Integer.toString(unreadCountTemp) : "";  
        }

        // [{"fromId" : " + chatList.get(i).getFromId() + "},
        result.append("[{\"fromId\" : \"" + chatList.get(i).getFromId() + "\"},");
        result.append("{\"toId\" : \"" + chatList.get(i).getToId() + "\"},");
        result.append("{\"chatContent\" : \"" + chatList.get(i).getChatContent() + "\"},");
        result.append("{\"createdDate\" : \"" + chatList.get(i).getCreatedDate() + "\"},");
        result.append("{\"unreadCount\" : \"" + unreadCount + "\"}]");
        if (i != 0) {
          result.append(",");
        }
      }
      // ], "last" : ""+ chatList.get(chatList.size() -1).getChatNo() + ""}
      result.append(" ], \"last\" : \"" + chatList.get(chatList.size() - 1).getChatNo() + "\"}");

      return result.toString();
    }
    
  }
  
}
