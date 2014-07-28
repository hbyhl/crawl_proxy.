package cn.focus.crawl.impl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.focus.crawl.Analyzer;
import cn.focus.crawl.model.Host;
import cn.focus.crawl.model.ProxyPoolTestList;
import cn.focus.crawl.start.Main;
import cn.focus.crawl.utils.Downloader;
import cn.wanghaomiao.xpath.exception.NoSuchAxisException;
import cn.wanghaomiao.xpath.exception.NoSuchFunctionException;
import cn.wanghaomiao.xpath.exception.XpathSyntaxErrorException;
import cn.wanghaomiao.xpath.model.JXDocument;

public class XPathAnalyzer implements Analyzer {
    private final static Logger logger = Logger.getLogger(DemoAnalyzer.class);


    private static Document doc = null;

    @Override
    public void parse(String url) {
        try {
//            parseDetailed(url);
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
        doc = Downloader.getDocumentUTF8(url);
        JXDocument jxDocument = new JXDocument(doc);
        String pathLi = "//html/body/div[@class='midbox']/div[@class='main']/div[@id='data']/ul[@id='page']/li/a";
        List<Object> lis = null;
        try {
            lis = jxDocument.sel(pathLi);
        } catch (NoSuchAxisException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchFunctionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (XpathSyntaxErrorException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String newUrl = null;
        for (Object li : lis) {
            if (li instanceof Element) {
                newUrl = "http://letushide.com" + ((Element) li).attr("href");
                        urlList.add(newUrl);
            }
        }
        return urlList;
    }

    private void parseDetailed(String url) throws XPathExpressionException {
        doc = Downloader.getDocumentUTF8(url);
        JXDocument jxDocument = new JXDocument(doc);
        String pathTr = "//html/body/div[@class='midbox']/div[@class='main']/div[@id='data']/table[@class='data']/tbody/tr[position()>1]";
        List<Object> trs = null;
        try {
            trs = jxDocument.sel(pathTr);
        } catch (NoSuchAxisException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchFunctionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (XpathSyntaxErrorException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Elements tds = null;
        int i = 0;
        for (Object tr : trs) {
            Host host = new Host();
            if (tr instanceof Element) {
                tds = ((Element) tr).getElementsByTag("td");
                String ip = tds.get(1).text();
                if (Main.ips.indexOf(ip) != -1) {
                    continue;
                }
                String port = tds.get(2).text();

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
}
