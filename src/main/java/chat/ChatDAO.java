package chat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class ChatDAO {

  DataSource dataSource;

  public ChatDAO() {

    try {
      Context initCtx = new InitialContext();
      Context envCtx = (Context) initCtx.lookup("java:/comp/env");
      dataSource = (DataSource) envCtx.lookup("jdbc/UserChat");

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  public ArrayList<ChatDTO> getChatListByNo(int chatNo, String fromId, String toId) {

    ArrayList<ChatDTO> chatList = null;

    Connection conn = null;
    PreparedStatement pStmt = null;
    ResultSet rSet = null;

    String sqlQuery =
        " SELECT chat_no AS chatNo, from_id AS fromId, to_id AS toId, chat_content AS chatContent, created_date AS createdDate FROM chats WHERE ((from_id = ? AND to_id = ?) OR (from_id = ? AND to_id = ?)) AND chat_no > ? ORDER BY created_date ASC; ";

    try {
      conn = dataSource.getConnection();

      pStmt = conn.prepareStatement(sqlQuery);
      pStmt.setString(1, fromId);
      pStmt.setString(2, toId);
      pStmt.setString(3, toId);
      pStmt.setString(4, fromId);
      pStmt.setInt(5, chatNo);

      rSet = pStmt.executeQuery();

      chatList = new ArrayList<ChatDTO>();

      while (rSet.next()) {
        ChatDTO chat = new ChatDTO();
        chat.setChatNo(rSet.getInt("chatNo"));
        chat.setFromId(rSet.getString("fromId").replaceAll(" ", "&nbsp;").replaceAll("<", "&lt;")
            .replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
        chat.setToId(rSet.getString("toId").replaceAll(" ", "&nbsp;").replaceAll("<", "&lt;")
            .replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
        chat.setChatContent(rSet.getString("chatContent").replaceAll(" ", "&nbsp;")
            .replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
        String tempTime = rSet.getString("createdDate");
        int chatTime = Integer.parseInt(tempTime.substring(11, 13));
        String timeType = "오전";
        if (chatTime > 12) {
          timeType = "오후";
          chatTime -= 12;
        }
        chat.setCreatedDate(tempTime.substring(0, 11) + " " + timeType + " " + chatTime + ":"
            + tempTime.substring(14, 16) + "");
        chatList.add(chat);
      }

    } catch (Exception e) {

      e.printStackTrace();

    } finally {
      try {
        if (rSet != null) {
          rSet.close();
        }

        if (pStmt != null) {
          pStmt.close();
        }

        if (conn != null) {
          conn.close();
        }
      } catch (Exception e2) {
        e2.printStackTrace();
      }
    }

    return chatList; // 리스트 반환
  }

  public ArrayList<ChatDTO> getChatListByRecent(int number, String fromId, String toId) {

    ArrayList<ChatDTO> chatList = null;

    Connection conn = null;
    PreparedStatement pStmt = null;
    ResultSet rSet = null;

    String sqlQuery =
        " SELECT chat_no AS chatNo, from_id AS fromId, to_id AS toId, chat_content AS chatContent, created_date AS createdDate FROM chats WHERE ((from_id = ? AND to_id = ?) OR (from_id = ? AND to_id = ?)) AND chat_no > (SELECT MAX(chat_no) - ? FROM chats WHERE ((from_id = ? AND to_id = ?) OR (from_id = ? AND to_id = ?)))  ORDER BY created_date ASC; ";

    try {
      conn = dataSource.getConnection();

      pStmt = conn.prepareStatement(sqlQuery);
      pStmt.setString(1, fromId);
      pStmt.setString(2, toId);
      pStmt.setString(3, toId);
      pStmt.setString(4, fromId);
      pStmt.setInt(5, number);
      pStmt.setString(6, fromId);
      pStmt.setString(7, toId);
      pStmt.setString(8, toId);
      pStmt.setString(9, fromId);

      rSet = pStmt.executeQuery();

      chatList = new ArrayList<ChatDTO>();

      while (rSet.next()) {
        ChatDTO chat = new ChatDTO();
        chat.setChatNo(rSet.getInt("chatNo"));
        chat.setFromId(rSet.getString("fromId").replaceAll(" ", "&nbsp;").replaceAll("<", "&lt;")
            .replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
        chat.setToId(rSet.getString("toId").replaceAll(" ", "&nbsp;").replaceAll("<", "&lt;")
            .replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
        chat.setChatContent(rSet.getString("chatContent").replaceAll(" ", "&nbsp;")
            .replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
        String tempTime = rSet.getString("createdDate");
        int chatTime = Integer.parseInt(tempTime.substring(11, 13));
        String timeType = "오전";
        if (chatTime > 12) {
          timeType = "오후";
          chatTime -= 12;
        }
        chat.setCreatedDate(tempTime.substring(0, 11) + " " + timeType + " " + chatTime + ":"
            + tempTime.substring(14, 16) + "");
        chatList.add(chat);
      }

    } catch (Exception e) {

      e.printStackTrace();

    } finally {
      try {
        if (rSet != null) {
          rSet.close();
        }

        if (pStmt != null) {
          pStmt.close();
        }

        if (conn != null) {
          conn.close();
        }
      } catch (Exception e2) {
        e2.printStackTrace();
      }
    }

    return chatList; // 리스트 반환

  }

  public int submit(String fromId, String toId, String chatContent) {

    Connection conn = null;
    PreparedStatement pStmt = null;
    ResultSet rSet = null;

    String sqlQuery = " INSERT INTO chats(from_id, to_id, chat_content) VALUES (?, ?, ?); ";
    try {
      conn = dataSource.getConnection();

      pStmt = conn.prepareStatement(sqlQuery);
      pStmt.setString(1, fromId);
      pStmt.setString(2, toId);
      pStmt.setString(3, chatContent);

      return pStmt.executeUpdate();

    } catch (Exception e) {

      e.printStackTrace();

    } finally {
      try {
        if (rSet != null) {
          rSet.close();
        }

        if (pStmt != null) {
          pStmt.close();
        }

        if (conn != null) {
          conn.close();
        }
      } catch (Exception e2) {
        e2.printStackTrace();
      }
    }

    return -1; // 데이터 베이스 오류

  }

  public int readChat(String fromId, String toId) {

    Connection conn = null;
    PreparedStatement pStmt = null;
    ResultSet rSet = null;

    String sqlQuery =
        " UPDATE chats SET chat_read = 1 where (from_id = ? and to_id = ?) and chat_read = 0; ";
    try {
      conn = dataSource.getConnection();

      pStmt = conn.prepareStatement(sqlQuery);
      pStmt.setString(1, toId);
      pStmt.setString(2, fromId);

      return pStmt.executeUpdate();

    } catch (Exception e) {

      e.printStackTrace();

    } finally {
      try {
        if (rSet != null) {
          rSet.close();
        }

        if (pStmt != null) {
          pStmt.close();
        }

        if (conn != null) {
          conn.close();
        }
      } catch (Exception e2) {
        e2.printStackTrace();
      }
    }
    return -1;
  }

  public int getAllCountUnreadChat(String userId) {

    Connection conn = null;
    PreparedStatement pStmt = null;
    ResultSet rSet = null;

    String sqlQuery =
        " SELECT COUNT(chat_no) AS COUNT FROM chats WHERE to_id = ? and chat_read = 0; ";
    try {
      conn = dataSource.getConnection();

      pStmt = conn.prepareStatement(sqlQuery);
      pStmt.setString(1, userId);

      rSet = pStmt.executeQuery();

      if (rSet.next()) {
        return rSet.getInt("COUNT");
      }
      return 0;
    } catch (Exception e) {

      e.printStackTrace();

    } finally {
      try {
        if (rSet != null) {
          rSet.close();
        }

        if (pStmt != null) {
          pStmt.close();
        }

        if (conn != null) {
          conn.close();
        }
      } catch (Exception e2) {
        e2.printStackTrace();
      }
    }
    return -1;
  }

  public ArrayList<ChatDTO> getBox(String userId) {

    ArrayList<ChatDTO> chatList = null;

    Connection conn = null;
    PreparedStatement pStmt = null;
    ResultSet rSet = null;

    String sqlQuery =
        " SELECT chat_no AS chatNo, from_id AS fromId, to_id AS toId, chat_content AS chatContent, created_date AS createdDate FROM chats WHERE chat_no IN (SELECT max(chat_no) FROM chats WHERE to_id = ? OR from_id = ? GROUP BY from_id, to_id); ";

    try {
      conn = dataSource.getConnection();

      pStmt = conn.prepareStatement(sqlQuery);
      pStmt.setString(1, userId);
      pStmt.setString(2, userId);

      rSet = pStmt.executeQuery();

      chatList = new ArrayList<ChatDTO>();

      while (rSet.next()) {

        ChatDTO chat = new ChatDTO();
        chat.setChatNo(rSet.getInt("chatNo"));
        chat.setFromId(rSet.getString("fromId").replaceAll(" ", "&nbsp;").replaceAll("<", "&lt;")
            .replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
        chat.setToId(rSet.getString("toId").replaceAll(" ", "&nbsp;").replaceAll("<", "&lt;")
            .replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
        chat.setChatContent(rSet.getString("chatContent").replaceAll(" ", "&nbsp;")
            .replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
        String tempTime = rSet.getString("createdDate");
        int chatTime = Integer.parseInt(tempTime.substring(11, 13));
        String timeType = "오전";
        if (chatTime > 12) {
          timeType = "오후";
          chatTime -= 12;
        }
        chat.setCreatedDate(tempTime.substring(0, 11) + " " + timeType + " " + chatTime + ":"
            + tempTime.substring(14, 16) + "");
        chatList.add(chat);
      }

      for (int i = 0; i < chatList.size(); i++) {
        ChatDTO x = chatList.get(i);
        for (int j = 0; j < chatList.size(); j++) {
          ChatDTO y = chatList.get(j);
          if (x.getFromId().equals(y.getToId()) && x.getToId().equals(y.getFromId())) {
            if (x.getChatNo() < y.getChatNo()) {
              chatList.remove(x);
              i--;
              break;
            } else {
              chatList.remove(y);
              j--;
            }
          }
        }
      }

    } catch (Exception e) {

      e.printStackTrace();

    } finally {
      try {
        if (rSet != null) {
          rSet.close();
        }

        if (pStmt != null) {
          pStmt.close();
        }

        if (conn != null) {
          conn.close();
        }
      } catch (Exception e2) {
        e2.printStackTrace();
      }
    }

    return chatList; // 리스트 반환

  }
}
