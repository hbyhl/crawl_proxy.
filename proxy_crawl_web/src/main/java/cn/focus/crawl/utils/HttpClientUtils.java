package cn.focus.crawl.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class HttpClientUtils {
	
	private static Logger logger = Logger.getLogger(HttpClientUtils.class);

    private static List<CloseableHttpClient> clientList = new ArrayList<CloseableHttpClient>();

    private static int clientCount = 0;

    private static Random rand = new Random();
    
    public final static String PROXY_URL = "http://focus-crawls.apps.sohuno.com/proxy/list";

    private HttpClientUtils() {
    }

    /**
     * 代理集合,可从数据库或配置文件中读取
     */
    private static List<HttpHost> proxyList = new ArrayList<HttpHost>() {
        private static final long serialVersionUID = -3275380625027992912L;
        {
        	JSONObject obj = JSONObject.parseObject(HttpClientUtils.get(PROXY_URL));
        	JSONArray array = obj.getJSONArray("data");
        	for (int i = 0 ; i < array.size(); i++) {
        		JSONObject host = array.getJSONObject(i);
        		add(new HttpHost(host.getString("ip"), host.getIntValue("port")));        		
        	}
        	//add(new HttpHost("127.0.0.1", 8087));
        	//add(new HttpHost("216.239.33.102", 80));
        	//add(new HttpHost("216.239.33.103", 80));
        	//add(new HttpHost("216.239.35.102", 80));
        	//add(new HttpHost("216.239.35.103", 80));
        }
    };

    /**
     * User-Agent集合
     */
    private static List<String> userAgentList = new ArrayList<String>() {
        private static final long serialVersionUID = -2649066726207050120L;
        {
            add(new String("Mozilla/5.0 (Windows; U; MSIE 7.0; Windows NT 6.0; en-US)"));
            add(new String("Mozilla/5.0 (X11; Ubuntu; Linux i686; rv:15.0) Gecko/20100101 Firefox/15.0.1"));
            add(new String("Mozilla/5.0 (Windows NT 6.2; WOW64; rv:15.0) Gecko/20120910144328 Firefox/15.0.2"));
            add(new String("Opera/9.80 (X11; Linux x86_64; U; fr) Presto/2.9.168 Version/11.50"));
            add(new String("Opera/9.80 (Macintosh; Intel Mac OS X 10.6.8; U; de) Presto/2.9.168 Version/11.52"));
            add(new String("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0; Sleipnir/2.9.8)"));
            add(new String("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.0; Trident/5.0)"));
            add(new String("Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; Trident/6.0)"));
            add(new String("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8) AppleWebKit/536.25 (KHTML, like Gecko) Version/6.0 Safari/536.25"));
            add(new String("Opera/9.60 (Windows NT 5.1; U; ja) Presto/2.1.1"));
            add(new String("Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.2; Trident/6.0)"));
            add(new String("Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; EasyBits GO v1.0; InfoPath.1; .NET CLR 2.0.50727; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)"));
            add(new String("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0; Sleipnir/2.9.8)"));
            add(new String("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.92 Safari/537.1 LBBROWSER"));
            add(new String("Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; InfoPath.2; .NET4.0C; .NET4.0E)"));
            add(new String("Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; InfoPath.2; .NET4.0C; .NET4.0E)"));
            
        }
    };

    static {
        Registry<ConnectionSocketFactory> reg = RegistryBuilder.<ConnectionSocketFactory> create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", SSLConnectionSocketFactory.getSocketFactory()).build();

        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(reg);
        cm.setMaxTotal(800);//连接池最大并发连接数
        cm.setDefaultMaxPerRoute(200);// 单路由最大并发数
        HttpClientBuilder builder = HttpClients.custom().setConnectionManager(cm);

        for (int i = 0, j = proxyList.size(); i < j; i++) {
            for (int m = 0, n = userAgentList.size(); m < n; m++) {
                builder.setProxy(proxyList.get(i));
                builder.setUserAgent(userAgentList.get(m));
                clientList.add(builder.build());
            }
        }

        clientCount = clientList.size();
    }

    public static CloseableHttpClient getClient() {
        return clientList.get(rand.nextInt(clientCount));
    }
    
	/**
	 * 获得proxy_list集合
	 * @param url
	 * @param params
	 * @return
	 */
	public static String get(String url, Map<String, Object> params) {		
		CloseableHttpClient client = HttpClientBuilder.create().build();
		String copyUrl = url;
		if (params != null) {
            copyUrl = copyUrl + "?" + stringify(params);
		}
		try {
			HttpGet get = new HttpGet(copyUrl);
			HttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity);
		} catch (ClientProtocolException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return "";
	}
	
	/**
	 * 获得proxy_list集合
	 * @param url
	 * @return
	 */
	public static String get(String url) {
		return get(url, null);
	}

	/**
	 * 拼接后缀参数
	 * @param paramsMap
	 * @return
	 */
	public static String stringify(Map<String, Object> paramsMap) {
		if (null == paramsMap) {
			return null;
		}
		StringBuilder builder = new StringBuilder();
		for (Map.Entry<String, Object> entry : paramsMap.entrySet()) {
			builder.append(entry.getKey()).append("=").append(entry.getValue())
					.append("&");
		}
		return builder.deleteCharAt(builder.length() - 1).toString();
	}
}
