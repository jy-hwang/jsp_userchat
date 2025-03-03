package board;

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import utils.StringUtils;

@WebServlet("/boardArticleDeleteServlet")
public class BoardArticleDeleteServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doPost(request, response);
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    request.setCharacterEncoding("UTF-8");
    response.setContentType("text/html;charset=UTF-8");
    HttpSession session = request.getSession();
    String userId = (String) session.getAttribute("userId");

    String tempBoardNo = request.getParameter("boardNo");
    if (StringUtils.isEmpty(tempBoardNo)) {
      session.setAttribute("messageType", "오류 메시지");
      session.setAttribute("messageContent", "게시물을 선택해주세요.");
      response.sendRedirect("index.jsp");
      return;
    }
    int boardNo = Integer.parseInt(tempBoardNo);

    BoardDAO boardDAO = new BoardDAO();
    BoardDTO original = boardDAO.getOne(boardNo);
    if (!userId.equals(original.getUserId())) {
      session.setAttribute("messageType", "오류 메시지");
      session.setAttribute("messageContent", "접근할 수 없습니다.");
      response.sendRedirect("index.jsp");
      return;
    }

    String savePath = request.getRealPath("/uploads").replaceAll("\\\\", "/");
    String prev = original.getBoardFile();
    int result = boardDAO.deleteArticle(boardNo);
    if (result == -1) {
      session.setAttribute("messageType", "오류 메시지");
      session.setAttribute("messageContent", "접근할 수 없습니다.");
      response.sendRedirect("index.jsp");
      return;
    } else {
      if (!StringUtils.isEmpty(prev)) {
        File prevFile = new File(savePath + "/" + prev);
        if (prevFile.exists()) {
          prevFile.delete();
        }
      }
      session.setAttribute("messageType", "성공 메시지");
      session.setAttribute("messageContent", "게시글 삭제에 성공했습니다.");
      response.sendRedirect("boardList.jsp");
      return;
    }
  }

}
