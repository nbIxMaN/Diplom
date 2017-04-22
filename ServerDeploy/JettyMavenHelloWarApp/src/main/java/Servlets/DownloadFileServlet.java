package Servlets;

import Controllers.DBSession;
import Controllers.S3Controller;
import Models.FileInfo;
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

/**
 * Created by alex on 18.03.17.
 */
public class DownloadFileServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (UserRepository userRepository = new UserRepository(DBSession.get())) {
            User user = userRepository.get(req.getSession().getId());
            if (user != null) {
                JSONObject jsonObject = new JSONObject();
                for (FileInfo fileInfo : user.getSetOfFile()) {
                    jsonObject.accumulate("file", fileInfo.getName());
                }
                resp.getWriter().append(jsonObject.toString());
            }
            else{
                resp.setStatus(HttpStatus.SC_UNAUTHORIZED);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getSession().getId();
            try(UserRepository userRepository = new UserRepository(DBSession.get())) {
            User user = userRepository.get(req.getSession().getId());
            if (user != null) {
                S3Controller s3Controller = new S3Controller();
                BufferedReader reader = req.getReader();
                StringBuilder jb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null)
                    jb.append(line);
                JSONObject jsonObject = JSONObject.fromObject(jb.toString());
                resp.getWriter().write(s3Controller.getFileLink(user, jsonObject.getString("file")).toString());
            }
            else{
                resp.setStatus(HttpStatus.SC_UNAUTHORIZED);
            }
        }
    }
}
