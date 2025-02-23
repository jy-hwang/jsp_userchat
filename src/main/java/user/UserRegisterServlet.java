package user;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/userRegisterServlet")
public class UserRegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    request.setCharacterEncoding("UTF-8");
	    response.setContentType("text/html;charset=UTF-8");
	    
	    String userId = request.getParameter("userId");
	    String userPassword1 = request.getParameter("userPassword1");
	    String userPassword2 = request.getParameter("userPassword2");
	    String userName = request.getParameter("userName");
	    String userAge = request.getParameter("userAge");
	    String userGender = request.getParameter("userGender");
	    String userEmail = request.getParameter("userEmail");
	    
	    if(userId == null || userId.equals("") || userPassword1 == null || userPassword1.equals("")
	        || userPassword2 == null || userPassword2.equals("") || userName == null || userName.equals("")
	        || userAge == null || userAge.equals("") || userGender == null || userGender.equals("")
	        || userEmail == null || userEmail.equals("")) {
	      
	      request.getSession().setAttribute("messageType", "오류메시지");
	      request.getSession().setAttribute("messageContent", "모든 내용을 입력하세요");
	      response.sendRedirect("join.jsp");
	      return;
	    }
	    if(!userPassword1.equals(userPassword2)) {
	      request.getSession().setAttribute("messageType", "오류메시지");
          request.getSession().setAttribute("messageContent", "비밀번호가 일치하지 않습니다.");
          response.sendRedirect("join.jsp");
          return;
        }
	    
	    UserDTO userDTO = new UserDTO();
	    
        userDTO.setUserId(userId);
        userDTO.setUserPassword(userPassword1);
        userDTO.setUserName(userId);
        userDTO.setUserAge(Integer.parseInt(userAge));
        userDTO.setUserGender(userGender);
        userDTO.setUserEmail(userEmail);
        
        int result = new UserDAO().register(userDTO);

	    if( result == 1) {
	      request.getSession().setAttribute("messageType", "성공메시지");
          request.getSession().setAttribute("messageContent", "회원가입에 성공했습니다.");
          response.sendRedirect("index.jsp");
          
	    } else {
	      request.getSession().setAttribute("messageType", "오류메시지");
          request.getSession().setAttribute("messageContent", "회원가입에 실패했습니다.");
          response.sendRedirect("join.jsp");
          return;
	    }
	    
	}

}
