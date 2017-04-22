package Servlets;

import Controllers.DBSession;
import Controllers.S3Controller;
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
import java.net.URL;

//@MultipartConfig(
//        fileSizeThreshold = 16768,
//        maxRequestSize = 10L * 1024 * 1024,
//        maxFileSize = 10L * 1024 * 1024
//        )
public class UploadFileServlet extends HttpServlet {

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
                URL uploadLink = s3Controller.getUploadLink(user, jsonObject.getString("file"));
                if (uploadLink != null) {
                    resp.getWriter().write(uploadLink.toString());
                }
                else {
                    resp.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                }
            }
            else{
                resp.setStatus(HttpStatus.SC_UNAUTHORIZED);
            }
        }
    }
}