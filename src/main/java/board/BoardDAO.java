package board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

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
        board.setBoardNo(rSet.getInt("boardNo"));
        board.setUserId(rSet.getString("userId"));
        board.setBoardTitle(rSet.getString("boardTitle").replaceAll(" ", "&nbsp;")
            .replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
        board.setBoardContent(rSet.getString("boardContent").replaceAll(" ", "&nbsp;")
            .replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
        board.setBoardHit(rSet.getInt("boardHit"));
        board.setBoardFile(rSet.getString("boardFile"));
        board.setBoardRealFile(rSet.getString("boardRealFile"));
        board.setBoardGroup(rSet.getInt("boardGroup"));
        board.setBoardSequence(rSet.getInt("boardSequence"));
        board.setBoardLevel(rSet.getInt("boardLevel"));
        board.setBoardAvailable(rSet.getInt("boardAvailable"));
        board.setCreatedDate(rSet.getString("createdDate").substring(0, 11));
        if (rSet.getString("updatedDate") != null) {
          board.setUpdatedDate(rSet.getString("updatedDate").substring(0, 11));
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

    return board;
  }

  public ArrayList<BoardDTO> getList(String pageNumber) {

    ArrayList<BoardDTO> boardList = null;

    Connection conn = null;
    PreparedStatement pStmt = null;
    ResultSet rSet = null;

    String sqlQuery =
        " SELECT board_no AS boardNo, user_id AS userId, board_title AS boardTitle, board_hit AS boardHit, board_group AS boardGroup, board_sequence AS boardSequence, board_level AS boardLevel, board_available AS boardAvailable, created_date AS createdDate, updated_date AS updatedDate FROM boards WHERE board_group > (SELECT max(board_group) FROM boards) - ? AND board_group <= (SELECT max(board_group) FROM boards ) - ?  ORDER BY board_group DESC, board_sequence ASC ";
    boardList = new ArrayList<BoardDTO>();
    try {
      conn = dataSource.getConnection();
      pStmt = conn.prepareStatement(sqlQuery);
      pStmt.setInt(1, Integer.parseInt(pageNumber) * 10);
      pStmt.setInt(2, (Integer.parseInt(pageNumber) - 1) * 10);

      rSet = pStmt.executeQuery();

      while (rSet.next()) {
        BoardDTO board = new BoardDTO();
        board.setBoardNo(rSet.getInt("boardNo"));
        board.setUserId(rSet.getString("userId"));
        board.setBoardTitle(rSet.getString("boardTitle").replaceAll(" ", "&nbsp;")
            .replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
        board.setBoardHit(rSet.getInt("boardHit"));
        board.setBoardGroup(rSet.getInt("boardGroup"));
        board.setBoardSequence(rSet.getInt("boardSequence"));
        board.setBoardLevel(rSet.getInt("boardLevel"));
        board.setBoardAvailable(rSet.getInt("boardAvailable"));
        board.setCreatedDate(rSet.getString("createdDate").substring(0, 11));
        if (rSet.getString("updatedDate") != null) {
          board.setUpdatedDate(rSet.getString("updatedDate").substring(0, 11));
        }
        boardList.add(board);
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

    return boardList;
  }

  public int hit(int boardNo) {

    Connection conn = null;
    PreparedStatement pStmt = null;

    String sqlQuery = " UPDATE boards SET board_hit = board_hit + 1 WHERE board_no = ? ";

    try {
      conn = dataSource.getConnection();
      pStmt = conn.prepareStatement(sqlQuery);

      pStmt.setInt(1, boardNo);

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

  public boolean isNextPage(String pageNumber) {

    BoardDTO board = new BoardDTO();

    Connection conn = null;
    PreparedStatement pStmt = null;
    ResultSet rSet = null;

    String sqlQuery = " SELECT board_title AS boardTitle FROM boards WHERE board_group >= ? ";

    try {
      conn = dataSource.getConnection();
      pStmt = conn.prepareStatement(sqlQuery);
      pStmt.setInt(1, Integer.parseInt(pageNumber) * 10);
      rSet = pStmt.executeQuery();

      if (rSet.next()) {
        return true;
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

    return false;
  }

  public int targetPage(String pageNumber) {

    BoardDTO board = new BoardDTO();

    Connection conn = null;
    PreparedStatement pStmt = null;
    ResultSet rSet = null;

    String sqlQuery = " SELECT COUNT(board_group) FROM boards WHERE board_group > ? ";

    try {
      conn = dataSource.getConnection();
      pStmt = conn.prepareStatement(sqlQuery);
      pStmt.setInt(1, (Integer.parseInt(pageNumber) -1) * 10);
      rSet = pStmt.executeQuery();

      if (rSet.next()) {
        return rSet.getInt(1) / 10;
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

    return 0;
  }

  public String getFile(int boardNo) {

    BoardDTO board = new BoardDTO();

    Connection conn = null;
    PreparedStatement pStmt = null;
    ResultSet rSet = null;

    String sqlQuery = " SELECT board_file AS boardFile FROM boards WHERE board_no = ? ";

    try {
      conn = dataSource.getConnection();
      pStmt = conn.prepareStatement(sqlQuery);
      pStmt.setInt(1, boardNo);
      rSet = pStmt.executeQuery();

      if (rSet.next()) {
        return rSet.getString("boardFile");
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

    return "";
  }

  public String getRealFile(int boardNo) {

    BoardDTO board = new BoardDTO();

    Connection conn = null;
    PreparedStatement pStmt = null;
    ResultSet rSet = null;

    String sqlQuery = " SELECT board_real_file AS boardRealFile FROM boards WHERE board_no = ? ";

    try {
      conn = dataSource.getConnection();
      pStmt = conn.prepareStatement(sqlQuery);
      pStmt.setInt(1, boardNo);
      rSet = pStmt.executeQuery();

      if (rSet.next()) {
        return rSet.getString("boardRealFile");
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

    return "";
  }

  public int updateArticle(BoardDTO boardDTO) {

    Connection conn = null;
    PreparedStatement pStmt = null;

    String sqlQuery =
        " UPDATE boards SET board_title = ?, board_content = ?, board_file = ?, board_real_file = ?, updated_date = current_timestamp WHERE board_no = ? ";

    try {
      conn = dataSource.getConnection();
      pStmt = conn.prepareStatement(sqlQuery);

      pStmt.setString(1, boardDTO.getBoardTitle());
      pStmt.setString(2, boardDTO.getBoardContent());
      pStmt.setString(3, boardDTO.getBoardFile());
      pStmt.setString(4, boardDTO.getBoardRealFile());
      pStmt.setInt(5, boardDTO.getBoardNo());

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

  public int deleteArticle(int boardNo) {

    Connection conn = null;
    PreparedStatement pStmt = null;

    String sqlQuery =
        " UPDATE boards SET board_available = 0, updated_date = current_timestamp WHERE board_no = ? ";

    try {
      conn = dataSource.getConnection();
      pStmt = conn.prepareStatement(sqlQuery);

      pStmt.setInt(1, boardNo);

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

  public int writeReply(BoardDTO parent, BoardDTO child) {

    Connection conn = null;
    PreparedStatement pStmt = null;

    String sqlQuery =
        " INSERT INTO boards (user_id, board_title, board_content, board_file, board_real_file, board_group, board_sequence, board_level) VALUES ( ?, ?, ?, ?, ?, ?, ? ,? ) ";

    try {
      conn = dataSource.getConnection();
      pStmt = conn.prepareStatement(sqlQuery);

      pStmt.setString(1, child.getUserId());
      pStmt.setString(2, child.getBoardTitle());
      pStmt.setString(3, child.getBoardContent());
      pStmt.setString(4, child.getBoardFile());
      pStmt.setString(5, child.getBoardRealFile());
      pStmt.setInt(6, parent.getBoardGroup());
      pStmt.setInt(7, parent.getBoardSequence() + 1);
      pStmt.setInt(8, parent.getBoardLevel() + 1);

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

  public int updateReplySequence(BoardDTO parent) {

    Connection conn = null;
    PreparedStatement pStmt = null;

    String sqlQuery =
        " UPDATE boards SET board_sequence = board_sequence + 1 WHERE board_group = ? AND board_sequence > ? ";

    try {
      conn = dataSource.getConnection();
      pStmt = conn.prepareStatement(sqlQuery);

      pStmt.setInt(1, parent.getBoardGroup());
      pStmt.setInt(2, parent.getBoardSequence());

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

  public int deleteReply(int boardNo) {

    Connection conn = null;
    PreparedStatement pStmt = null;

    String sqlQuery =
        " UPDATE boards SET board_available = 0, updated_date = current_timestamp WHERE board_no = ? ";

    try {
      conn = dataSource.getConnection();
      pStmt = conn.prepareStatement(sqlQuery);

      pStmt.setInt(1, boardNo);

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

}
