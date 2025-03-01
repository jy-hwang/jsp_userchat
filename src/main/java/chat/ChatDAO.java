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
        " SELECT chat_no AS chatNo, from_id AS fromId, to_id AS toId, chat_content AS chatContent, created_date AS createdDate FROM chats WHERE ((from_id = ? AND to_id = ?) OR (from_id = ? AND to_id = ?)) AND chat_no > ? ORDER BY created_date DESC; ";

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
        " SELECT chat_no AS chatNo, from_id AS fromId, to_id AS toId, chat_content AS chatContent, created_date AS createdDate FROM chats WHERE ((from_id = ? AND to_id = ?) OR (from_id = ? AND to_id = ?)) AND chat_no > (SELECT MAX(chat_no) - ? FROM chats)  ORDER BY created_date DESC; ";

    try {
      conn = dataSource.getConnection();

      pStmt = conn.prepareStatement(sqlQuery);
      pStmt.setString(1, fromId);
      pStmt.setString(2, toId);
      pStmt.setString(3, toId);
      pStmt.setString(4, fromId);
      pStmt.setInt(5, number);

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

}
