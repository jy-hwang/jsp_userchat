package board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import chat.ChatDTO;
import user.UserDTO;

public class BoardDAO {

  DataSource dataSource;

  public BoardDAO() {

    try {
      Context initCtx = new InitialContext();
      Context envCtx = (Context) initCtx.lookup("java:/comp/env");
      dataSource = (DataSource) envCtx.lookup("jdbc/UserChat");


    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  public int writeArticle(BoardDTO boardDTO) {

    Connection conn = null;
    PreparedStatement pStmt = null;

    String sqlQuery =
        " INSERT INTO boards (user_id, board_title, board_content, board_file, board_real_file, board_group)  SELECT ?, ?, ?, ?, ?, IFNULL(MAX(board_group), 0) + 1 FROM (SELECT board_group FROM boards) AS temp ";

    try {
      conn = dataSource.getConnection();
      pStmt = conn.prepareStatement(sqlQuery);

      pStmt.setString(1, boardDTO.getUserId());
      pStmt.setString(2, boardDTO.getBoardTitle());
      pStmt.setString(3, boardDTO.getBoardContent());
      pStmt.setString(4, boardDTO.getBoardFile());
      pStmt.setString(5, boardDTO.getBoardRealFile());

      return pStmt.executeUpdate();

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
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

    return -1;// 데이터베이스 오류
  }
  
  public BoardDTO getOne(int boardNo) {

    BoardDTO board = new BoardDTO();

    Connection conn = null;
    PreparedStatement pStmt = null;
    ResultSet rSet = null;

    String sqlQuery =
        " SELECT board_no AS boardNo, user_id AS userId, board_title AS boardTitle, board_content AS boardContent, board_hit AS boardHit, board_file AS boardFile, board_real_file AS boardRealFile, board_group AS boardGroup, board_sequence AS boardSequence, board_level AS boardLevel, board_available AS boardAvailable, created_date AS createdDate, updated_date AS updatedDate FROM boards WHERE board_no = ? ";

    try {
      conn = dataSource.getConnection();
      pStmt = conn.prepareStatement(sqlQuery);
      pStmt.setInt(1, boardNo);
      rSet = pStmt.executeQuery();

      if (rSet.next()) {
        board.setUserId(rSet.getString("userId"));
        board.setBoardTitle(rSet.getString("boardTitle").replaceAll(" ", "&nbsp;").replaceAll("<", "&lt;")
            .replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
        board.setBoardContent(rSet.getString("boardContent").replaceAll(" ", "&nbsp;").replaceAll("<", "&lt;")
            .replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
        board.setBoardHit(rSet.getInt("boardHit"));
        board.setBoardFile(rSet.getString("boardFile"));
        board.setBoardRealFile(rSet.getString("boardRealFile"));
        board.setBoardGroup(rSet.getInt("boardHit"));
        board.setBoardSequence(rSet.getInt("boardSequence"));
        board.setBoardLevel(rSet.getInt("boardLevel"));
        board.setCreatedDate(rSet.getString("createdDate").substring(0, 11));
        board.setUpdatedDate(rSet.getString("updatedDate").substring(0, 11));
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

    return board;
  }
  
}
