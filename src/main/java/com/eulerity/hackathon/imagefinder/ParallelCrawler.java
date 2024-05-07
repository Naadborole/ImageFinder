package com.eulerity.hackathon.imagefinder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParallelCrawler implements Runnable {
    String url;
    SharedResources sharedResources;

    ParallelCrawler(String url, SharedResources sharedResources) {
        this.url = url;
        this.sharedResources = sharedResources;
    }

    @Override
    public void run() {
        try {
            System.out.println("Thread called with: " + url);
            Scrapper sc = new Scrapper(url);
            List<String> lnks = new ArrayList<String>(sc.linksWithSameDomain(sc.getLinks()));
            for (String lnk : lnks) {
                if(!sharedResources.links.contains(lnk)){
                    sharedResources.links.add(lnk);
                    sharedResources.pool.submit(new ParallelCrawler(lnk, sharedResources));
                }
            }
            List<String> imgs = new ArrayList<String>(sc.getImages());
            sharedResources.imgs.addAll(imgs);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}