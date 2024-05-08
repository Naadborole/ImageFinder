package com.eulerity.hackathon.imagefinder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.net.URL;

public class SharedResources {
    ExecutorService pool;
    ConcurrentHashMap.KeySetView<Object, Boolean> imgs;
    ConcurrentHashMap.KeySetView<Object, Boolean> links;

    SharedResources() {
        pool = Executors.newFixedThreadPool(100);
        imgs = ConcurrentHashMap.newKeySet();
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
            if(!pool.awaitTermination(2,TimeUnit.SECONDS)){
                pool.shutdown();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        List<String> imList =  new ArrayList<String>();
        for (String imLink : imgs.toArray(new String[0])) {
            if(isValidURL(imLink)){
                imList.add(imLink);
            }
        }
        return imList;
    }
}
