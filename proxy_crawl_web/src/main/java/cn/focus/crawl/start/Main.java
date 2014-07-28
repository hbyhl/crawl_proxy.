package cn.focus.crawl.start;

import java.util.List;

import net.paoding.rose.scanning.context.RoseAppContext;
import cn.focus.crawl.Analyzer;
import cn.focus.crawl.model.ConfigBean;
import cn.focus.crawl.model.Host;
import cn.focus.crawl.model.ProxyPoolTestList;
import cn.focus.crawl.service.TestService;
import cn.focus.crawl.utils.ProxyDecide;

public class Main {
    public static List<String> ips = null;

    private ConfigBean config;

    private TestService testService;

    public ConfigBean getConfig() {
        return config;
    }

    public void setConfig(ConfigBean config) {
        this.config = config;
    }

    public TestService getTestService() {
        return testService;
    }

    public void setTestService(TestService testService) {
        this.testService = testService;
    }

    public void start() {
        int ProxyThreadNum = 10;
        ips = testService.getIp();
        ProxyDecide.startThreadPool(ProxyThreadNum);
        for (int i = 0; i < config.beanSize(); i++) {
            Analyzer analyzer = config.getAnalyzerBean(i);
            analyzer.parse(config.getMapperUrl(i));
        }

        try {
            while (ProxyPoolTestList.hostList.size() > 0) {
                System.out.println("===========");
                Thread.sleep(10000);
            }
            ProxyDecide.closeThreadPool();

            for (int i = 0; i < ProxyThreadNum; i++) {
                ProxyDecide[] proxyDecidePool = ProxyDecide.getProxyDecidePool();
                while (proxyDecidePool[i].isAlive()) {
                    Thread.sleep(10000);
                    System.out.println("等待第" + i + "个线程关闭");
                }
            }

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        for (Host host : ProxyDecide.validHostList) {
            testService.insert(host);
        }

    }
}
