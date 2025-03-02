package board;

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import utils.StringUtils;

@WebServlet("/boardUpdateServlet")
public class BoardUpdateServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    request.setCharacterEncoding("UTF-8");
    response.setContentType("text/html;charset=UTF-8");

    MultipartRequest multi = null;
    int fileMaxSize = 10 * 1024 * 1024;
    String savePath = request.getRealPath("/uploads").replaceAll("\\\\", "/");

    try {
      multi = new MultipartRequest(request, savePath, fileMaxSize, "UTF-8",
          new DefaultFileRenamePolicy());
    } catch (Exception e) {
      request.getSession().setAttribute("messageType", "오류 메시지");
      request.getSession().setAttribute("messageContent", "파일 크기는 10MB를 초과할 수 없습니다.");
      response.sendRedirect("index.jsp");
      return;
    }

    String userId = multi.getParameter("userId");
    HttpSession session = request.getSession();
    if (!userId.equals((String) session.getAttribute("userId"))) {
      request.getSession().setAttribute("messageType", "오류 메시지");
      request.getSession().setAttribute("messageContent", "접근할 수 없습니다.");
      response.sendRedirect("index.jsp");
      return;
    }
    int boardNo = Integer.parseInt(multi.getParameter("boardNo"));
    if (boardNo == 0) {
      request.getSession().setAttribute("messageType", "오류 메시지");
      request.getSession().setAttribute("messageContent", "접근할 수 없습니다.");
      response.sendRedirect("index.jsp");
      return;
    }
    BoardDAO boardDAO = new BoardDAO();
    BoardDTO original = boardDAO.getOne(boardNo);
    if (!userId.equals(original.getUserId())) {
      request.getSession().setAttribute("messageType", "오류 메시지");
      request.getSession().setAttribute("messageContent", "접근할 수 없습니다.");
      response.sendRedirect("boardList.jsp");
      return;
    }
    String boardTitle = multi.getParameter("boardTitle");
    String boardContent = multi.getParameter("boardContent");
    if (StringUtils.isEmpty(boardTitle) || StringUtils.isEmpty(boardContent)) {
      request.getSession().setAttribute("messageType", "오류 메시지");
      request.getSession().setAttribute("messageContent", "내용을 모두 채워주세요.");
      response.sendRedirect("index.jsp");
      return;
    }

    String boardFile = "";
    String boardRealFile = "";

    String fileName = "";
    File file = multi.getFile("boardFile");
    if (file != null) {
      boardFile = multi.getOriginalFileName("boardFile");
      boardRealFile = file.getName();
      String prev = original.getBoardRealFile();
      File prevFile = new File(savePath + "/" + prev);
      if (prevFile.exists()) {
        prevFile.delete();
      }
    } else {
      boardFile = original.getBoardFile();
      boardRealFile = original.getBoardRealFile();
    }

    BoardDTO updateArticle = new BoardDTO();
    updateArticle.setBoardNo(boardNo);
    updateArticle.setBoardTitle(boardTitle);
    updateArticle.setBoardContent(boardContent);
    updateArticle.setBoardFile(boardFile);
    updateArticle.setBoardRealFile(boardRealFile);


    boardDAO.updateArticle(updateArticle);

    request.getSession().setAttribute("messageType", "성공 메시지");
    request.getSession().setAttribute("messageContent", "성공적으로 게시물이 수정되었습니다.");
    response.sendRedirect("boardList.jsp");
    return;

  }

}
