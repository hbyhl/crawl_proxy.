package cn.focus.crawl.dao;

import java.util.List;
import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.ReturnGeneratedKeys;
import net.paoding.rose.jade.annotation.SQL;
import cn.focus.crawl.model.Host;
@DAO(catalog = "proxy_crawl")
public interface HostDAO {

    @SQL("select ip FROM proxy_pool_test")
    List<Host> getIp();
    
    @SQL("SELECT  id, ip, port, urlname, level, status, createtime, updatetime, website, url FROM proxy_pool_test WHERE id = :1")
	Host get(Integer id );

	
	@SQL("SELECT  id, ip, port, urlname, level, status, createtime, updatetime, website, url FROM proxy_pool_test WHERE id IN (:1)")
	List<Host> getList(List<Integer> idList );
	
	
	@SQL("SELECT  id, ip, port, urlname, level, status, createtime, updatetime, website, url FROM proxy_pool_test WHERE id IN (:1) ORDER BY find_in_set(id , :2)")
	List<Host> getOrderList(List<Integer> idList, String orderIdsStr );
    
	@ReturnGeneratedKeys
	@SQL("INSERT INTO proxy_pool_test ("+ "#if(:1.ip!=null){ip}"+
	    				    	       "#if(:1.port!=null){,port}"+
	    				    	       "#if(:1.urlname!=null){,urlname}"+
	    				    	       "#if(:1.level!=null){,level}"+
	    				    	       "#if(:1.status!=null){,status}"+
	    				    	       "#if(:1.createtime!=null){,createtime}"+
	    				    	       "#if(:1.updatetime!=null){,updatetime}"+
	    				    	       "#if(:1.website!=null){,website}"+
	    				    	       "#if(:1.url!=null){,url}"+
	    		") VALUES("+     "#if(:1.ip!=null){:1.ip}"+
	    		    	    	       "#if(:1.port!=null){,:1.port}"+
	    		    	    	       "#if(:1.urlname!=null){,:1.urlname}"+
	    		    	    	       "#if(:1.level!=null){,:1.level}"+
	    		    	    	       "#if(:1.status!=null){,:1.status}"+
	    		    	    	       "#if(:1.createtime!=null){,:1.createtime}"+
	    		    	    	       "#if(:1.updatetime!=null){,:1.updatetime}"+
	    		    	    	       "#if(:1.website!=null){,:1.website}"+
	    		    	    	       "#if(:1.url!=null){,:1.url}"+")") 
	Long save(Host proxypooltest);
	
    	@SQL("INSERT INTO proxy_pool_test ("+ "#if(:1.ip!=null){ip}"+
                "#if(:1.port!=null){,port}"+
                "#if(:1.urlname!=null){,urlname}"+
                "#if(:1.level!=null){,level}"+
                "#if(:1.status!=null){,status}"+
                "#if(:1.createtime!=null){,createtime}"+
                "#if(:1.updatetime!=null){,updatetime}"+
                "#if(:1.website!=null){,website}"+
                "#if(:1.url!=null){,url}"+
    ") VALUES("+     "#if(:1.ip!=null){:1.ip}"+
                "#if(:1.port!=null){,:1.port}"+
                "#if(:1.urlname!=null){,:1.urlname}"+
                "#if(:1.level!=null){,:1.level}"+
                "#if(:1.status!=null){,:1.status}"+
                "#if(:1.createtime!=null){,:1.createtime}"+
                "#if(:1.updatetime!=null){,:1.updatetime}"+
                "#if(:1.website!=null){,:1.website}"+
                "#if(:1.url!=null){,:1.url}"+")") 
	    int saveList(List<Host> proxypooltest);
	
		@SQL("UPDATE proxy_pool_test SET id=:1.id " +
		    		    	       "#if(:1.ip!=null){,ip=:1.ip}"+
	    		    	       "#if(:1.port!=null){,port=:1.port}"+
	    		    	       "#if(:1.urlname!=null){,urlname=:1.urlname}"+
	    		    	       "#if(:1.level!=null){,level=:1.level}"+
	    		    	       "#if(:1.status!=null){,status=:1.status}"+
	    		    	       "#if(:1.createtime!=null){,createtime=:1.createtime}"+
	    		    	       "#if(:1.updatetime!=null){,updatetime=:1.updatetime}"+
	    		    	       "#if(:1.website!=null){,website=:1.website}"+
	    		    	       "#if(:1.url!=null){,url=:1.url}"+
	    	    " WHERE id=:1.id ")
	void update(Host proxypooltest );
	
	@SQL("DELETE FROM proxy_pool_test WHERE id= :1")
	int delete(Integer id);
	
	@SQL("SELECT COUNT(1) FROM proxy_pool_test")
	int count();
	
}