package org.example;


import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;


import java.io.File;

public class App {

    public static void main(String[] args) throws LifecycleException {


                // Initialize Tomcat
                Tomcat tomcat = new Tomcat();

                // Set port (use Render's PORT env variable or default to 8080)
                String port = System.getenv("PORT") != null ? System.getenv("PORT") : "8080";
                tomcat.setPort(Integer.parseInt(port));

                // Set base directory for temporary files
                String baseDir = "tomcat_temp";
                tomcat.setBaseDir(baseDir);

                // Add web application
                String webappDir = "app/src/main/webapp"; // Path to webapp directory
                Context ctx = tomcat.addWebapp("", new File(webappDir).getAbsolutePath());


        // Add compiled classes (Gradle builds to build/classes/java/main)
                String classesDir = "app/build/classes/java/main";
                StandardRoot resources = new StandardRoot(ctx);
                resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes", new File(classesDir).getAbsolutePath(), "/"));
                ctx.setResources(resources);

                // Start the server
                tomcat.start();
                System.out.println("Tomcat started on port " + port);

                // Keep server running
                tomcat.getServer().await();
            }
}
