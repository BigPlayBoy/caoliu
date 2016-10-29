package com.cui.main;

import java.util.Iterator;

import com.cui.Utils.downLoadImage;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cui.Utils.JsoupUtil;
import com.cui.Utils.NetTool;
import com.cui.dao.PageDao;

public class Main {
    // 这里写总的控制
    private static Logger log = LoggerFactory.getLogger(Main.class);
    private static String URL_HEAD = "http://dz.a5v.biz/";//网站地址

    public static void main(String[] args) {
//        downLoadPages();
        String url="http://dz.a5v.biz/htm_data/16/1611/2112534.html";
        String pathname = "e:/down";
        downLoadImage(url, pathname);
    }

    /**
     * 输入url和文件要保存的路径，既可以下载图片
     * @param url 图片路径
     * @param pathname 要保存的路径
     */
    private static void downLoadImage(String url, String pathname) {
        String cookie=null;
        String htmlPage= NetTool.doGet(url,cookie);
        Element table = JsoupUtil.getTable(htmlPage,"gbk2312", 2);
        Elements input=table.getElementsByTag("input");
        System.out.println(input.size());
        for(Iterator iterator = input.iterator(); iterator.hasNext();){
            Element element = (Element) iterator.next();
//            System.out.println("链接："+element.attr("src"));
            String imageUrl = element.attr("src");
            String photoName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1, imageUrl.length());
            downLoadImage.downloadPhoto(pathname,photoName,imageUrl);
        }
    }

    private static void downLoadPages() {
        // 第一步，获得达盖尔的网址
        int begin = 2;
        int pagenum = 101;
        log.info("开始");
//		String URL = "http://dc.ididcl.co/thread0806.php?fid=8&search=&page=";
        String URL = "http://dz.a5v.biz/thread0806.php?fid=8&search=&page=";//8 新时代
        while (begin < pagenum) {
            String newurl = URL + begin++;
//            String cookie = NetTool.getOffCookie(newurl);
            String cookie = null;
            log.info("获取网页：" + newurl);
            String page = NetTool.doGet(newurl, cookie);
            savePage(page, 4, begin);
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("结束");
    }


    private static void savePage(String page, int tablePlace, int pageNum) {
        Element table = JsoupUtil.getTable(page,"gbk2312", tablePlace);
        int result = 0;
//        System.out.println(table);
        Elements h3s = table.getElementsByTag("h3");
//        System.out.println(h3s);
        for (Iterator iterator = h3s.iterator(); iterator.hasNext(); ) {
            Element element = (Element) iterator.next();
            Element a_element = element.getElementsByTag("a").first();
//            System.out.println(a_element);
            result = PageDao.addUrl(URL_HEAD + a_element.attr("href"), a_element.text().replace("'", ""));
            if (result != 2) {
                log.error("出问题页面：" + pageNum);
            }
        }
    }
}

