package com.eulerity.hackathon.imagefinder;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@WebServlet(
        name = "ImageFinder",
        urlPatterns = {"/main"}
)
public class ImageFinder extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected static final Gson GSON = new GsonBuilder().create();

    @Override
    protected final void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/json");
        String path = req.getServletPath();
        String url = req.getParameter("url");
        System.out.println("Got request of:" + path + " with query param:" + url);
        //Create shared resources which will contain image links
        SharedResources sharedResources = new SharedResources();
        ParallelCrawler parallelCrawler = new ParallelCrawler(url, sharedResources);
        sharedResources.pool.submit(parallelCrawler);
        List<String> imgs = sharedResources.getImages();
        /*System.out.println(sharedResources.getImagesAsJSON());*/
        System.out.println("Done.");
        resp.getWriter().print(sharedResources.getImagesAsJSON());

    }
}
