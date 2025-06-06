package org.example;


import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.filters.CorsFilter;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;

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


                FilterDef corsFilterDef = getFilterDef();

                // Add filter definition to context
                        ctx.addFilterDef(corsFilterDef);

                        // Create filter mapping
                        FilterMap corsFilterMap = new FilterMap();
                        corsFilterMap.setFilterName("corsFilter");
                        corsFilterMap.addURLPattern("/*");  // apply filter to all URLs

                        // Add filter mapping to context
                        ctx.addFilterMap(corsFilterMap);


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

    private static FilterDef getFilterDef() {
        FilterDef corsFilterDef = new FilterDef();
        corsFilterDef.setFilterName("corsFilter");
        corsFilterDef.setFilter(new CorsFilter());
        corsFilterDef.addInitParameter("cors.allowed.origins", "*");
        corsFilterDef.addInitParameter("cors.allowed.methods", "GET,POST,HEAD,OPTIONS,PUT");
        corsFilterDef.addInitParameter("cors.allowed.headers", "Origin,Accept,X-Requested-With,Content-Type,Access-Control-Request-Method,Access-Control-Request-Headers,Authorization");
        return corsFilterDef;
    }
}
