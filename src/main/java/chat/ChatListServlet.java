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

@WebServlet("/chatListServlet")
public class ChatListServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    request.setCharacterEncoding("UTF-8");
    response.setContentType("text/html;charset=UTF-8");

    String fromId = request.getParameter("fromId");
    String toId = request.getParameter("toId");
    String listType = request.getParameter("listType");

    if (StringUtils.isEmpty(request.getParameter("fromId"))
        || StringUtils.isEmpty(request.getParameter("toId"))
        || StringUtils.isEmpty(request.getParameter("listType"))) {
      response.getWriter().write("");
    } else if (listType.equals("ten")) {
      response.getWriter()
          .write(getTen(URLDecoder.decode(fromId, "UTF-8"), URLDecoder.decode(toId, "UTF-8")));
    } else {
      try {
        HttpSession session = request.getSession();
        if(!fromId.equals((String) session.getAttribute("userId"))) {
          response.getWriter().write("");
          return;
        }
        
        response.getWriter().write(getNo(URLDecoder.decode(fromId, "UTF-8"),
            URLDecoder.decode(toId, "UTF-8"), Integer.parseInt(listType)));
      } catch (Exception e) {
        response.getWriter().write("");
      }
    }

  }

  public String getTen(String fromId, String toId) {

    StringBuffer result = new StringBuffer("");
    // {"result":[
    result.append("{\"result\":[");
    ChatDAO chatDAO = new ChatDAO();
    ArrayList<ChatDTO> chatList = chatDAO.getChatListByRecent(100, fromId, toId);
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
      chatDAO.readChat(fromId, toId);
      return result.toString();
    }

  }

  public String getNo(String fromId, String toId, int chatNo) {

    StringBuffer result = new StringBuffer("");
    // {"result":[
    result.append("{\"result\":[");
    ChatDAO chatDAO = new ChatDAO();
    ArrayList<ChatDTO> chatList = chatDAO.getChatListByNo(chatNo, fromId, toId);
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
      chatDAO.readChat(fromId, toId);
      return result.toString();
    }

  }
  
}
