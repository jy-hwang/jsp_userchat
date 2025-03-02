package user;

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

@WebServlet("/userUpdateProfileServlet")
public class UserUpdateProfileServlet extends HttpServlet {
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
      response.sendRedirect("updateProfile.jsp");
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

    String fileName = "";
    File file = multi.getFile("userProfile");
    if (file != null) {
      String ext = file.getName().substring(file.getName().lastIndexOf(".") + 1);
      if (ext.equals("jpg") || ext.equals("png") || ext.equals("gif")) {
        String prev = new UserDAO().getUser(userId).getUserProfile();
        File prevFile = new File(savePath + "/" + prev);
        if (prevFile.exists()) {
          prevFile.delete();
        }
        fileName = file.getName();
      } else {
        if (file.exists()) {
          file.delete();
        }
        request.getSession().setAttribute("messageType", "오류 메시지");
        request.getSession().setAttribute("messageContent", "이미지 파일만 업로드 가능합니다.");
        response.sendRedirect("updateProfile.jsp");
        return;
      }

    }
    new UserDAO().updateProfile(userId, fileName);
    request.getSession().setAttribute("messageType", "성공 메시지");
    request.getSession().setAttribute("messageContent", "성공적으로 프로필이 변경되었습니다.");
    response.sendRedirect("index.jsp");
    return;
  }

}
