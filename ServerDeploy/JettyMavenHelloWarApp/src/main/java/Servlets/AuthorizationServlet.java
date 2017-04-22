package Servlets;

import Controllers.DBSession;
import Models.User;
import Repositories.UserRepository;
import org.apache.http.HttpStatus;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthorizationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String auth = req.getHeader("Authorization");
        String login = auth.split(":")[0];
        String password = auth.split(":")[1];
        try(UserRepository userRepository = new UserRepository(DBSession.get())){
//            String cookie = Integer.toString((login + password).hashCode());
            User user = userRepository.get(login, password);
            if (user != null) {
                userRepository.update(new User(login, password), req.getSession().getId());
                resp.addHeader("Cookie", req.getSession().getId());
                resp.setStatus(HttpStatus.SC_OK);
            }
            else{
                resp.setStatus(HttpStatus.SC_UNAUTHORIZED);
            }
        }
    }
}

