package cn.focus.crawl.model; 

import cn.focus.dc.commons.model.BaseObject;
import cn.focus.dc.commons.annotation.PrimaryKey;

public class Host extends BaseObject {

    private static final long serialVersionUID = 1L;

    @PrimaryKey
    private Integer id;

    private String ip;

    private String port;

    private String urlname;

    private Integer level;

    private Integer status;

    private java.util.Date createtime;

    private java.util.Date updatetime;

    private String website;

    private String url;

    public void copy(Host proxypooltest){
               this.id = proxypooltest.id;
               this.ip = proxypooltest.ip;
               this.port = proxypooltest.port;
               this.urlname = proxypooltest.urlname;
               this.level = proxypooltest.level;
               this.status = proxypooltest.status;
               this.createtime = proxypooltest.createtime;
               this.updatetime = proxypooltest.updatetime;
               this.website = proxypooltest.website;
               this.url = proxypooltest.url;
          }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
    
    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
    
    public String getUrlname() {
        return urlname;
    }

    public void setUrlname(String urlname) {
        this.urlname = urlname;
    }
    
    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
    
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public java.util.Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(java.util.Date createtime) {
        this.createtime = createtime;
    }
    
    public java.util.Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(java.util.Date updatetime) {
        this.updatetime = updatetime;
    }
    
    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
    
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    public static Host getInstantce(Integer id,String ip,String port,String urlname,Integer level,Integer status,java.util.Date createtime,java.util.Date updatetime,String website,String url){
      Host proxypooltest = new Host();
               proxypooltest.setId(id);
               proxypooltest.setIp(ip);
               proxypooltest.setPort(port);
               proxypooltest.setUrlname(urlname);
               proxypooltest.setLevel(level);
               proxypooltest.setStatus(status);
               proxypooltest.setCreatetime(createtime);
               proxypooltest.setUpdatetime(updatetime);
               proxypooltest.setWebsite(website);
               proxypooltest.setUrl(url);
            return proxypooltest;
    }

}