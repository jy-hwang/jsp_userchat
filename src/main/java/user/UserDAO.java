package user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class UserDAO {

  DataSource dataSource;

  public UserDAO() {

    try {
      Context initCtx = new InitialContext();
      Context envCtx = (Context) initCtx.lookup("java:/comp/env");
      dataSource = (DataSource) envCtx.lookup("jdbc/UserChat");


    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  public int login(String userId, String userPassword) {

    Connection conn = null;
    PreparedStatement pStmt = null;
    ResultSet rSet = null;

    String sqlQuery =
        " SELECT user_id AS userId, user_password AS userPassword FROM users WHERE user_id = ? ";

    try {
      conn = dataSource.getConnection();
      pStmt = conn.prepareStatement(sqlQuery);

      rSet = pStmt.executeQuery();

      if (rSet.next()) {
        if (rSet.getString("userPassword").equals(userPassword)) {
          return 1;// 로그인 성공
        }
        return 2;// 비밀번호 x
      } else {
        return 0;// 존재하지 않음
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

    return -1;// 데이터베이스 오류
  }
  
  public int registerCheck(String userId) {

    Connection conn = null;
    PreparedStatement pStmt = null;
    ResultSet rSet = null;

    String sqlQuery =
        " SELECT user_id AS userId FROM users WHERE user_id = ? ";

    try {
      conn = dataSource.getConnection();
      pStmt = conn.prepareStatement(sqlQuery);

      rSet = pStmt.executeQuery();

      if (rSet.next() && rSet.getString("userId").equals(userId)) {
          return 1;// 중복
        } else {
        return 0;// OK
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

    return -1;// 데이터베이스 오류
  }
  
  public int register(UserDTO userDTO) {

    Connection conn = null;
    PreparedStatement pStmt = null;
    ResultSet rSet = null;

    String sqlQuery =
        " INSERT INTO users (user_id, user_password, user_name, user_age, user_gender, user_email)  VALUES (?, ?, ?, ?, ?, ?); ";

    try {
      conn = dataSource.getConnection();
      pStmt = conn.prepareStatement(sqlQuery);

      pStmt.setString(1, userDTO.getUserId());
      pStmt.setString(2, userDTO.getUserPassword());
      pStmt.setString(3, userDTO.getUserName());
      pStmt.setInt(4, userDTO.getUserAge());
      pStmt.setString(5, userDTO.getUserGender());
      pStmt.setString(6, userDTO.getUserEmail());
      
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

    return -1;// 데이터베이스 오류
  }
}
