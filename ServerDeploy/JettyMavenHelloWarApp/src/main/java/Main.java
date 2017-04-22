import Controllers.DBSession;
import Servlets.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

/**
 * Created by alex on 22.04.17.
 */
public class Main {
    protected Server server;

    public static void main(String[] args) throws Exception {
        int port = 8080;
        Server server = new Server(port);
        new DBSession();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");


//        ServletHolder fileUploadServletHolder = new ServletHolder(new UploadFileServlet());
//        fileUploadServletHolder.getRegistration().setMultipartConfig(new MultipartConfigElement("data/tmp"));
//        context.addServlet(fileUploadServletHolder, "/upload");
        context.addServlet(UploadConfuirmServlet.class, "/confirm");
        context.addServlet(UploadFileServlet.class, "/upload");
        context.addServlet(AuthorizationServlet.class, "/auth");
        context.addServlet(RegisterServlet.class, "/register");
        context.addServlet(DownloadFileServlet.class, "/download");
        //context.addServlet(UploadFileServlet.class, "/upload");

        server.setHandler(context);
        server.start();
        server.join();
    }
}
