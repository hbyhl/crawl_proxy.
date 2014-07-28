package cn.focus.crawl.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


public class Downloader {
	
	private final static Logger logger = Logger.getLogger(Downloader.class);
	
	public static final String reg = "<meta[^>]*?charset=([a-z|A-Z|0-9]*[\\-]*[0-9]*)[\\s|\\S]*>";
	
	public static final String DEFAULT_ENCODE = "utf-8";
    
    public static String getHtmlUTF8(String urlString) {
        return getHtml(urlString, "utf-8");
    }
    
    public static String getHtmlGB2312(String urlString) {
        return getHtml(urlString, "gb2312");
    }
    
    public static Document getDocumentUTF8(String url) {
        return getDocument(url, "utf-8");
    }
    
    public static Document getDocumentGB2312(String url) {
        return getDocument(url, "gb2312");
    }
    
    public static Document getDocument(String urlString) {
    	String html = "";
    	try {
			HttpClient httpClient = HttpClientUtils.getClient();
			HttpGet httpGet = new HttpGet(urlString);
			HttpResponse response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String tempHtml = EntityUtils.toString(entity);
				String encode = getCharSet(tempHtml);
				html = getHtml(urlString, encode);				
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} 
        return Jsoup.parse(html);
    }
    
    private static String getHtml(String urlString, String encode) {
        String html = "";
        try {
            HttpClient httpClient = HttpClientUtils.getClient();
            HttpGet httpGet = new HttpGet(urlString);
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                html = EntityUtils.toString(entity, encode).trim(); 
            }
        } catch (Exception e) {
			logger.error("Call method getHtml to analyze url [" + urlString
					+ "], charset [ " + encode + "] occured.", e);
		}
        return html;
    }

    private static Document getDocument(String url, String encode) {
        String html = getHtml(url, encode);
        return Jsoup.parse(html);
    }
    
    /**
     * According url cssQuery to analyse the html by using utf-8
     * @param url
     * @param cssQuery
     * @return Elements
     */
    public static Elements getElementsUTF8(String url, String cssQuery) {
    	Document document = getDocumentUTF8(url);
    	return getElements(document, cssQuery);		
    }
    
    /**
     * According url cssQuery to analyse the html by using GB2312
     * @param url
     * @param cssQuery
     * @return Elements
     */
    public static Elements getElementsGB2312(String url, String cssQuery) {
    	Document document = getDocumentGB2312(url);
		return getElements(document, cssQuery);
    }
    
    /**
     * According document cssQuery to analyse the html
     * @param document
     * @param cssQuery
     * @return Elements
     */
    private static Elements getElements(Document document, String cssQuery) {
    	if (null == document) {
    		return null;
    	}
    	return document.select(cssQuery); 
    }
    
    public static Elements getElements(String url, String cssQuery) {
    	Document document = getDocument(url);
    	if (null == document) {
    		return null;
    	}
    	return document.select(cssQuery);
    }
    
    public static String getFirstText(Document document, String cssQuery) {
    	Elements elements = getElements(document, cssQuery);
    	if (null == elements) {
    		return null;
    	}
    	return elements.isEmpty() ? null : elements.first().text();
    }
    
	public static String getCharSet(String content) {
		Matcher m = Pattern.compile(reg).matcher(content);
		if (m.find()) {
			return m.group(1);
		}
		return DEFAULT_ENCODE;
	}
}
