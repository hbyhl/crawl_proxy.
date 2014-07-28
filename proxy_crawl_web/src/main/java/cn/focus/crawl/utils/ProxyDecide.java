package cn.focus.crawl.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import cn.focus.crawl.model.Host;
import cn.focus.crawl.model.ProxyPoolTestList;

public class ProxyDecide extends Thread {

    public static List<Host> validHostList = new ArrayList<Host>();

    public static List<Host> hostList = ProxyPoolTestList.hostList;

    private static ProxyDecide[] proxyDecidePool = null;

    public static ProxyDecide[] getProxyDecidePool() {
        return proxyDecidePool;
    }

    public static boolean threadPoolFlag = false;

    public static int remNum = 0;

    public static int i = 0; // 测试用

    public static void startThreadPool(int n) {
        if (proxyDecidePool == null) {
            proxyDecidePool = new ProxyDecide[n];
            for (int i = 0; i < n; i++) {
                proxyDecidePool[i] = new ProxyDecide();
                proxyDecidePool[i].setName("------------线程" + i);
            }
        }
        if (threadPoolFlag == false) {
            threadPoolFlag = true;
            for (int i = 0; i < n; i++) {
                proxyDecidePool[i].start();
            }
        }

    }

    public static void closeThreadPool() {
        if (proxyDecidePool != null && threadPoolFlag == true) {
            threadPoolFlag = false;
        }
    }

    public boolean isValid(String ip, String port) throws ClientProtocolException, IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpHost target = new HttpHost("http://www.baidu.com/");

            HttpHost proxy = new HttpHost(ip, Integer.valueOf(port), "http");
            RequestConfig.Builder configBuilder = RequestConfig.custom();
            configBuilder.setProxy(proxy);
            configBuilder.setConnectionRequestTimeout(30000);
            configBuilder.setConnectTimeout(30000);
            configBuilder.setSocketTimeout(30000);
            RequestConfig config = configBuilder.build();
            HttpGet request = new HttpGet();
            request.setConfig(config);
            int statusCode = -1;
            CloseableHttpResponse response = httpClient.execute(target, request);
            try {

                statusCode = httpClient.execute(target, request).getStatusLine().getStatusCode();
            } finally {
                response.close();
            }
            if (statusCode != HttpStatus.SC_OK) {
                request.releaseConnection();
                return false;
            }
            
        } finally {
            httpClient.close();
        }     
        
        return true;
    }

    @Override
    public void run() {
        Host host = null;
        while (threadPoolFlag) {
            host = null;
            boolean isValid = false;
            synchronized (hostList) {
                if (hostList.size() == 0) {
                    try {
                        hostList.wait(100);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        System.out.println("测试");
                    }
                }
                if (hostList.size() > 0) {
                    host = hostList.remove(hostList.size() - 1);
                    System.out.println("-----------------------------------------------------" + "取出第" + ++remNum
                            + "个host");
                } else {
                    continue;
                }
            }

            try {
                System.out.println(Thread.currentThread().getName() + "开始处理host" + host.getIp() + ":" + host.getPort());
                isValid = this.isValid(host.getIp(), host.getPort());

            } catch (IOException e) {
                // System.out.println("isValid 发生i/o异常");
                continue;
            } finally {
                System.out.println(Thread.currentThread().getName() + "处理host" + host.getIp() + ":" + host.getPort()
                        + "完毕");
            }
            if (isValid) {
                synchronized (validHostList) {
                    validHostList.add(host);
                    System.out.println("增加第" + ++i + "个有效host");
                }
            }
        }
    }
}
