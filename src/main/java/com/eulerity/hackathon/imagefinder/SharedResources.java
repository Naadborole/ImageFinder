package com.eulerity.hackathon.imagefinder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class SharedResources {
    ExecutorService pool;
    ConcurrentHashMap.KeySetView<Object, Boolean> imgs;
    ConcurrentHashMap.KeySetView<Object, Boolean> links;

    SharedResources() {
        pool = Executors.newFixedThreadPool(100);
        imgs = ConcurrentHashMap.newKeySet();
        links = ConcurrentHashMap.newKeySet();
    }

    List<String> getImages() throws IOException {
        return Arrays.asList(imgs.toArray(new String[0]));
    }
}
