package com.eulerity.hackathon.imagefinder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.net.URL;

//This class describes the resources which will be shared among all threads i.e.
// all threads will access only one thread pool and image store (HashMap).
public class SharedResources {
    ExecutorService pool;
    ConcurrentHashMap<String, String> imgs;
    ConcurrentHashMap.KeySetView<Object, Boolean> links;

    SharedResources() {
        pool = Executors.newFixedThreadPool(100);
        imgs = new ConcurrentHashMap<String, String>();
        links = ConcurrentHashMap.newKeySet();
    }

    public static boolean isValidURL(String url)
    {
        try {
            new URL(url).toURI();
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    List<String> getImages() throws IOException {
        try {
            //Wait for 2 seconds to finish execution
            //Can be changed to give the application more time to search for images.
            //In case it is set to LONG_MAX it may run infinetly.
            if(!pool.awaitTermination(2,TimeUnit.SECONDS)){
                pool.shutdown();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        List<String> imList =  new ArrayList<String>();
        for (String imLink : imgs.keySet()) {
            if(isValidURL(imLink) && imgs.get(imLink).equals("Image")){
                imList.add(imLink);
            }
        }
        return imList;
    }

    //Get the images with the type of image as a JSON Array
    JsonArray getImagesAsJSON(){
        JsonArray arr = new JsonArray(imgs.size());
        for (String imLink : imgs.keySet()) {
            if(isValidURL(imLink)){
                JsonObject jsobj = new JsonObject();
                jsobj.add("link", new JsonPrimitive(imLink));
                jsobj.add("type", new JsonPrimitive(imgs.get(imLink)));
                arr.add(jsobj);
            }
        }
        return arr;
    }

}
