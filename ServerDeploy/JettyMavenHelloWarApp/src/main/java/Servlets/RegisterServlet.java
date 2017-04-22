package Servlets;

import Controllers.DBSession;
import Models.User;
import Repositories.UserRepository;
import net.sf.json.JSONObject;
import org.apache.http.HttpStatus;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try(UserRepository userRepository = new UserRepository(DBSession.get())){
            BufferedReader reader = req.getReader();
            StringBuilder jb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
                jb.append(line);
            JSONObject jsonObject = JSONObject.fromObject(jb.toString());
            User users = userRepository.get(jsonObject.getString("login"), jsonObject.getString("password"));
            if (users == null) {
                userRepository.add(new User(jsonObject.getString("login"), jsonObject.getString("password")));
                resp.setStatus(HttpStatus.SC_CREATED);
            }
            else{
                resp.setStatus(HttpStatus.SC_ACCEPTED);
            }

        }
        catch (Exception ex){
            resp.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println("Something wrong");
        }
    }
}

