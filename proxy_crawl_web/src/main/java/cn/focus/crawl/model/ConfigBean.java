package cn.focus.crawl.model;

import java.util.List;

import cn.focus.crawl.Analyzer;

public class ConfigBean {

	private List<Analyzer> beanList;
	private List<String> urlList;

	public List<Analyzer> getBeanList() {
		return beanList;
	}

	public void setBeanList(List<Analyzer> beanList) {
		this.beanList = beanList;
	}

	public List<String> getUrlList() {
		return urlList;
	}

	public void setUrlList(List<String> urlList) {
		this.urlList = urlList;
	}
	
	public int beanSize() {
		return beanList.size();
	}
	
	public Analyzer getAnalyzerBean(int i) {
		return beanList.get(i);
	}
	
	public String getMapperUrl(int i) {
		return urlList.get(i);
	}
	
	@Override
	public String toString() {
		//String 
		return super.toString();
	}
}
