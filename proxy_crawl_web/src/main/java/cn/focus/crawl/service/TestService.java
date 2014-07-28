package cn.focus.crawl.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import cn.focus.crawl.dao.HostDAO;
import cn.focus.crawl.model.Host;


public class TestService {

    @Autowired
    private HostDAO proxyPoolTestDAO;

    public void insert(Host host){
        proxyPoolTestDAO.save(host);
    }
    
    public List<String> getIp(){

        List<Host> list =  proxyPoolTestDAO.getIp();
        List<String> ips = new ArrayList<String>();
        while(list.size()>0)
        {
            ips.add(list.remove(list.size()-1).getIp());
        }
        return ips;
    }
}
