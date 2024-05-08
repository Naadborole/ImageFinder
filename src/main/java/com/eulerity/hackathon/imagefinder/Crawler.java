package com.eulerity.hackathon.imagefinder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;

public class Crawler {
    private Document doc;
    Crawler(String url) throws IOException {
        doc = Jsoup.connect(url).get();
    }

    //Extract links inside <img> tags
    HashSet<String> getImages(){
        HashSet<String> images = new HashSet<String>();
        Elements imgs = doc.select("img");
        for (Element img : imgs) {
            images.add(img.attr("abs:src"));
        }
        return images;
    }

    String getLocation(){
        return doc.location();
    }

    HashSet<String> getLinks(){
        HashSet<String> links = new HashSet<String>();
        Elements lnks = doc.select("a[href]");
        for (Element lnk : lnks) {
            links.add(lnk.attr("abs:href"));
        }
        return links;
    }

    HashSet<String> linksWithSameDomain(HashSet<String> links) throws MalformedURLException {
        HashSet<String> lnks = new HashSet<String>();
        String base = new URL(doc.location()).getHost();
        for (String link : links) {
            try {
                String lnk = new URL(link).getHost();
                if (lnk.equals(base)) {
                    lnks.add(link);
                }
            }
            catch (MalformedURLException e) {
                continue;
            }
        }
        return lnks;
    }
}
