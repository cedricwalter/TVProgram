package com.waltercedric.tvprogram.plugins.sources;

import com.rometools.rome.feed.synd.SyndCategory;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
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

    private static List<TVProgram> cache = null;
    private static String cacheDate = null;

    private static final HashMap<String, String> channels = new HashMap<>();

    static {
        channels.put("TF1", "TF1");
        channels.put("M6", "M6");
        channels.put("C8", "C8");
        channels.put("W9", "W9");
        channels.put("ter", "6ter");
        channels.put("TMC", "TMC");
        channels.put("HD1", "HD1");
        channels.put("Gulli", "Gulli");
    }

    public synchronized List<TVProgram> getTodayProgram() {
        if (cache == null) {
            cacheDate = getDate();
            cache = getProgramList();
        }

        if (!getDate().equals(cacheDate)) {
            cacheDate = getDate();
            cache = getProgramList();
        }

        return cache;
    }

    private List<TVProgram> getProgramList() {
        List<TVProgram> programs = new ArrayList<>();

        try {
            System.out.println("Starting to build today's program");
            URL feedUrl = getTodayFeedURL();

            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedUrl));

            List<SyndEntry> entries = feed.getEntries();
            for (SyndEntry entry : entries) {
                TVProgram tvProgram = getTvProgramFromEntry(entry);

                // use previous program on same channel to calculate end time
                if (programs.size() > 0) {
                    LocalTime startTime = tvProgram.getStartTime();

                    TVProgram previous = programs.get(programs.size() - 1);
                    if (previous.getChannel().equals(tvProgram.getChannel())) {
                        previous.setEndTime(startTime);
                    }
                }

                programs.add(tvProgram);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        System.out.println("Finished building today's program");
        return programs;
    }


    private TVProgram getTvProgramFromEntry(SyndEntry entry) {
        SyndCategory o = entry.getCategories().get(0);

        String category = Jsoup.parse(o.getName()).text();
        String description = Jsoup.parse(entry.getDescription().getValue()).text();
        description = description.replaceAll("\\>", " et");

        String text = Jsoup.parse(entry.getTitle()).text();
        String[] split = text.split("\\|");
        String title = split[2].trim();
        LocalTime startTime = LocalTime.parse(split[1].trim() + ":00");
        String channel = translateChannelForMoreClarity(split[0].trim());

        //endTime do not exist in feed, will be later updated when we have the whole list, so use startTime for now
        return new TVProgram(channel, title, category, description, startTime, startTime);
    }

    private String translateChannelForMoreClarity(String channel) {
        if (channels.containsKey(channel)) {
            return channels.get(channel);
        }
        return channel;
    }

    private URL getTodayFeedURL() throws MalformedURLException {
        String date = getDate();

        String spec = "https://webnext.fr/epg_cache/programme-tv-rss_" + date + ".xml";
        System.out.println("Fetching RSS from " + spec);

        return new URL(spec);
    }

    private String getDate() {
        LocalDateTime now = LocalDateTime.now();

        String monthValue = String.valueOf(now.getMonthValue());
        if (now.getMonthValue() < 10) {
            monthValue = "0" + now.getMonthValue();
        }

        String dayOfMonth = String.valueOf(now.getDayOfMonth());
        if (now.getDayOfMonth() < 10) {
            dayOfMonth = "0" + now.getDayOfMonth();
        }

        return now.getYear() + "-" + monthValue + "-" + dayOfMonth;
    }

}
