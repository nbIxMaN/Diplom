package Servlets;

import Controllers.DBSession;
import Controllers.S3Controller;
import Models.FileInfo;
import Models.User;
import Repositories.FileRepository;
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
 * Created by alex on 13.04.17.
 */
public class UploadConfuirmServlet extends HttpServlet {
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
                boolean isSaved = s3Controller.makeFilePublic(user, jsonObject.getString("file"));
                if (isSaved) {
                    try(FileRepository fileRepository = new FileRepository(DBSession.get())){
                        FileInfo fileInfo = new FileInfo(jsonObject.getString("file"));
                        fileRepository.add(fileInfo);
                        userRepository.update(user, fileInfo);
                    }
                }
                else {
                    resp.setStatus(HttpStatus.SC_NOT_FOUND);
                }
            }
            else{
                resp.setStatus(HttpStatus.SC_UNAUTHORIZED);
            }
        }
    }
}
