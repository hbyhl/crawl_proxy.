package cn.focus.crawl.impl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.focus.crawl.Analyzer;
import cn.focus.crawl.model.Host;
import cn.focus.crawl.model.ProxyPoolTestList;
import cn.focus.crawl.start.Main;
import cn.focus.crawl.utils.Downloader;

public class DemoAnalyzer implements Analyzer {

    private final static Logger logger = Logger.getLogger(DemoAnalyzer.class);

    public void parse(String url) {
        try {
            List<String> urlList = getAllurl(url);
            for (String inerURL : urlList) {
                System.out.println(inerURL);
                parseDetailed(inerURL);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static List<String> getAllurl(String url) {
        List<String> urlList = new ArrayList();
        Elements liElements = Downloader.getElementsUTF8(url, "ul.page li");
        for (Element liElement : liElements) {
            String s = liElement.getElementsByTag("a").get(0).attr("href");
            urlList.add("http://letushide.com" + s);
        }
        return urlList;
    }

    private void parseDetailed(String url) {
        logger.info("begin parse url + [" + url + "]");
        Elements tableElements = Downloader.getElementsUTF8(url, "table.data");
        Elements tbodyElements = tableElements.get(0).getElementsByTag("tbody");
        Elements trElements = tbodyElements.get(0).getElementsByTag("tr");
        int i = 0;
        for (Element trElement : trElements) {
            Host host = new Host();
            Elements tdElements = trElement.getElementsByTag("td");
            String ip = tdElements.get(1).getElementsByTag("a").html();
            if (Main.ips.indexOf(ip) != -1) {
                continue;
            }
            String port = tdElements.get(2).html();
            host.setIp(ip);
            host.setPort(port);
            host.setUpdatetime(new Date(System.currentTimeMillis()));
            host.setWebsite(url);
            synchronized (ProxyPoolTestList.hostList) {
                ProxyPoolTestList.hostList.add(host);
                System.out.println("增加第" + ++i + "个host");
                ProxyPoolTestList.hostList.notify();
            }
        }

    }
}
