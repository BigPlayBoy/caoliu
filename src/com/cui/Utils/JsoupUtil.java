package com.cui.Utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Stack;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.cui.dao.PageDao;

public class JsoupUtil {

    @SuppressWarnings("unused")
    public static void main(String[] args) {
        System.out.println("开始");
        int i = 1;
        Element table = getTable(4);
        Stack<Element> tdstack = getTdStack(table);
        // for (Iterator iterator = tdstack.iterator(); iterator.hasNext();) {
        // Element element = (Element) iterator.next();
        // System.out.println(i+++""+element);
        // }
//		Stack<String> urls = getPhotoUrl(tdstack);
//		System.out.println("输出栈中的网址");
        getNextPage();

        System.out.println("结束");
    }

    public static void addUrl(Stack<String> urls) {
        while (!urls.isEmpty()) {
            String url = urls.pop();
            System.out.println(url);
            String title = null;
            try {
                PageDao.addUrl(url, title);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static Element getTable(int index) {
        File input = new File("rescourse/net.html");
        Document doc;
        Element table = null;
        try {
            doc = Jsoup.parse(input, "gb2312", "");
            Elements tables = doc.getElementsByTag("table");
            table = tables.get(index);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return table;
    }

    /**
     * 通过链接获得
     *
     * @param url
     * @param index
     * @return
     */
    public static Element getTable(String url, String charSet,int index) {
        Document doc;
        Element table = null;
        doc = Jsoup.parse(url, charSet);
        Elements tables = doc.getElementsByTag("table");
        table = tables.get(index);
        return table;
    }

    public static Elements getTrs(Element table) {
        Elements trs = null;
        trs = table.getElementsByTag("tr");
        return trs;
    }

    /**
     * 匹配出所有的网址 放到堆栈中
     *
     * @param tdstack
     * @return
     */
    @SuppressWarnings("unused")
    public static Stack<String> getPhotoUrl(Stack<Element> tdstack) {
        Stack<String> photoUrl = new Stack<String>();
        int i = 0;
        while (!tdstack.isEmpty()) {
            Element td = tdstack.pop();
            String url = "http://dz.a5v.biz/";
            try {
                url = url + td.child(0).attributes().get("href");
//				System.out.println(i++ + url);
            } catch (Exception e) {
                System.out.println("Index: 0, Size: 0");
            }
            if (!"http://dz.a5v.biz/".equals(url)) {
                photoUrl.push(url);
            }
        }
        return photoUrl;
    }

    /**
     * 解析出包含链接的td标签
     *
     * @param table
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static Stack<Element> getTdStack(Element table) {
        Elements trs = getTrs(table);
        Stack<Element> tdstack = new Stack<Element>();
        for (Iterator iterator = trs.iterator(); iterator.hasNext(); ) {
            Element element = (Element) iterator.next();
            try {
                tdstack.push(element.child(0));
            } catch (Exception e) {
                System.out.println("没有孩子");
            }
        }
        return tdstack;
    }

    public static String getNextPage() {
        Element table = getTable(6);
        System.out.println(table);
        return null;
    }

    public static Elements getInput(String page) {
        Document doc = Jsoup.parse(page);
        Elements inputs = doc.children();
        System.out.println(inputs);
        return inputs;
    }

}
