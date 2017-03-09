package com.waltercedric.tvprogram.plugins.sources;

import com.rometools.fetcher.impl.FeedFetcherCache;
import com.rometools.fetcher.impl.HashMapFeedInfoCache;
import com.rometools.fetcher.impl.HttpURLFeedFetcher;
import com.rometools.rome.feed.synd.SyndCategory;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.waltercedric.tvprogram.TVProgram;
import org.jsoup.Jsoup;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * use https://webnext.fr/programme-tv-rss
 */
public class Webnext implements TVProgramBuilder {

    private static HashMap<String, String> channels = new HashMap<>();

    static {
        channels.put("TF1", "TF 1");
        channels.put("M6", "M 6");
        channels.put("C8", "C 8");
        channels.put("W9", "W 9");
        channels.put("ter", "six ter");
        channels.put("TMC", "T M C");
        channels.put("HD1", "HD un");
        channels.put("Gulli", "Gu li");
    }

    public List<TVProgram> getTodayProgram() {
        List<TVProgram> programs = new ArrayList<>();

        try {
            URL feedUrl = getTodayFeedURL();

            FeedFetcherCache feedInfoCache = new HashMapFeedInfoCache();
            // retrieve the feed the first time
            // any subsequent request will use conditional gets and only
            // retrieve the resource if it has changed
            SyndFeed feed = new HttpURLFeedFetcher(feedInfoCache).retrieveFeed(feedUrl);
            List<SyndEntry> entries = feed.getEntries();
            for (SyndEntry entry : entries) {
                TVProgram tvProgram = getTvProgramFromEntry(entry);

                programs.add(tvProgram);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return programs;
    }

    private TVProgram getTvProgramFromEntry(SyndEntry entry) {
        SyndCategory o = entry.getCategories().get(0);

        String category = Jsoup.parse(o.getName()).text();
        String description = Jsoup.parse(entry.getDescription().getValue()).text();

        String text = Jsoup.parse(entry.getTitle()).text();
        String[] split = text.split("\\|");
        String title = split[2].trim();
        LocalTime startTime = LocalTime.parse(split[1].trim() + ":00");
        String channel = translateChannelForMoreClarity(split[0].trim());

        return new TVProgram(channel, title, category, description, startTime, startTime);
    }

    private String translateChannelForMoreClarity(String channel) {
        if (channels.containsKey(channel)) {
            return channels.get(channel);
        }
        return channel;
    }

    private URL getTodayFeedURL() throws MalformedURLException {
        LocalDateTime now = LocalDateTime.now();

        String monthValue = String.valueOf(now.getMonthValue());
        if (now.getMonthValue() < 10) {
            monthValue = "0" + now.getMonthValue();
        }

        String dayOfMonth = String.valueOf(now.getDayOfMonth());
        if (now.getDayOfMonth() < 10) {
            dayOfMonth = "0" + now.getDayOfMonth();
        }

        return new URL("https://webnext.fr/epg_cache/programme-tv-rss_" + now.getYear() + "-" + monthValue + "-" + dayOfMonth + ".xml");
    }

}
