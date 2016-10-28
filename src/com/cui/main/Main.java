package com.cui.main;

import java.util.Iterator;
import java.util.Stack;

import com.cui.Utils.DBUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cui.Utils.JsoupUtil;
import com.cui.Utils.NetTool;
import com.cui.dao.PageDao;

public class Main {
    // 这里写总的控制
    static Logger log = LoggerFactory.getLogger(Main.class);

    @SuppressWarnings("rawtypes")
    public static void main(String[] args) {
        // 第一步，获得达盖尔的网址
        int i = 2;
        int pagenum = 101;
        log.info("开始");
        String url_head="http://dz.a5v.biz/";
//		String URL = "http://dc.ididcl.co/thread0806.php?fid=8&search=&page=";
        String URL = "http://dz.a5v.biz/thread0806.php?fid=8&search=&page=";//8 新时代
        while (i < pagenum) {
            String newurl = URL + i++;
//            String cookie = NetTool.getOffCookie(newurl);
            String cookie = null;
            log.info("获取网页：" + newurl);
            String page = NetTool.doGet(newurl, cookie);
            saveBtPage(url_head, page,i);
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("结束");
    }

    private static void savePhotoPage(String url_head, String page) {
        Element table = JsoupUtil.getTable(page, 4);
        Elements h3s = table.getElementsByTag("h3");
        System.out.println(h3s);
        for (Iterator iterator = h3s.iterator(); iterator.hasNext(); ) {
            Element element = (Element) iterator.next();
            Element a_element = element.getElementsByTag("a").first();
            PageDao.addUrl(url_head+a_element.attr("href"), a_element.text().replace("'",""));
        }
    }
    private static void saveBtPage(String url_head, String page,int i) {
        Element table = JsoupUtil.getTable(page, 4);
        int result=0;
//        System.out.println(table);
        Elements h3s = table.getElementsByTag("h3");
//        System.out.println(h3s);
        for (Iterator iterator = h3s.iterator(); iterator.hasNext(); ) {
            Element element = (Element) iterator.next();
            Element a_element = element.getElementsByTag("a").first();
//            System.out.println(a_element);
           result= PageDao.addUrl(url_head+a_element.attr("href"), a_element.text().replace("'",""));
            if(result!=2){
                log.error("出问题页面："+i);
            }
        }
    }
}

