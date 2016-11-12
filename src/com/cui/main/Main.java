package com.cui.main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

import com.cui.Utils.DBUtil;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cui.Utils.JsoupUtil;
import com.cui.Utils.NetTool;
import com.cui.dao.PageDao;

public class Main {
    // 这里写总的控制
    private static Logger logger = LoggerFactory.getLogger(Main.class);
    private static String URL_HEAD = "http://dz.a5v.biz/";//网站地址

    public static void main(String[] args) {
        downLoadPages();

    }

    private static void test(int begin, int increment) {

        String select = "select title,page_url from page limit " + begin + "," + begin + increment + ";";
        Connection connection = DBUtil.getConnection();
        Statement statement = null;
        ResultSet resultSet = null;
        String path = "e:/cao/";

        try {
            statement = connection.createStatement();
            statement.setFetchSize(increment);
            resultSet = statement.executeQuery(select);
            while (resultSet.next()) {
                downLoadImage(resultSet.getString(2).replace("?", ""), path + resultSet.getString(1).replace("\\", ""));
                System.out.println("" + begin++ + resultSet.getString(2));
                //Thread.sleep(1000 *5);
            }
            System.out.print(".");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.releaseDB(resultSet, statement, connection);
            System.out.println("释放resultset完毕！");
        }
        logger.info("下载完成");
    }

    /**
     * 输入url和文件要保存的路径，既可以下载图片
     *
     * @param url      图片路径
     * @param pathname 要保存的路径
     */
    private static void downLoadImage(String url, String pathname) {
        String cookie = null;
        String htmlPage = NetTool.doGet(url, cookie);
        Element table = JsoupUtil.getTable(htmlPage, "gbk2312", 2);
        Elements input = table.getElementsByTag("input");
        System.out.println(input.size());
        for (Iterator iterator = input.iterator(); iterator.hasNext(); ) {
            Element element = (Element) iterator.next();
            String imageUrl = element.attr("src");
            String photoName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1, imageUrl.length());
            NetTool.downloadPhoto(pathname, photoName, imageUrl);
        }
    }

    private static void downLoadPages() {
        // 第一步，获得达盖尔的网址
        int begin = 1;
        int pagenum = 100;
//        int count = 0;
        String FID = "2";//16  达盖尔
        //2 亚洲步兵
        logger.info("开始");
//		String URL = "http://dc.ididcl.co/thread0806.php?fid=8&search=&page=";
        String URL = "http://dz.a5v.biz/thread0806.php?fid=" + FID + "&search=&page=";//8 新时代
        while (begin < pagenum) {
            String newurl = URL + begin++;
//            String cookie = NetTool.getOffCookie(newurl);
            String cookie = null;
            logger.info("获取网页：" + newurl);
            String page = NetTool.doGet(newurl, cookie);

           savePage(page, 5, begin);//亚洲步兵  位置 5   其他  4
//            if (count > 60) {//有超过60个重复的 就停止更新
//                break;
//            }
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("结束");
    }


    private static int savePage(String page, int tablePlace, int pageNum) {
        logger.info("保存网址");
        Element table = JsoupUtil.getTable(page, "gbk2312", tablePlace);
        int result = 0;
        int count = 0;
//        System.out.println(table);
        Elements h3s = table.getElementsByTag("h3");
//        System.out.println(h3s);
        for (Iterator iterator = h3s.iterator(); iterator.hasNext(); ) {
            Element element = (Element) iterator.next();
            Element a_element = element.getElementsByTag("a").first();
//            System.out.println(a_element);
            result = PageDao.addUrl(URL_HEAD + a_element.attr("href"), a_element.text().replace("'", ""));
            switch (result) {
                case 0:
                    logger.error("出问题页面：" + pageNum);
                    break;
                case 1:
                    logger.info("此链接已存在：" + a_element.attr("href"));
                    break;
                case 2:
                    logger.info("保存成功");
                    count++;
                    break;
                default:
                    logger.error("未知原因");
            }

        }
        return count;
    }
}

