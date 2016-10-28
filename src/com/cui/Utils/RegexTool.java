package com.cui.Utils;

import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTool {
	// 获得网址所在的匹配区域部分
	// 从指定的字符串中 匹配需要的范围 只需要输入匹配的开始 和结尾
	// 这个可以制定开头的大概位置
	public static String getContentArea(String urlContent, String strAreaBegin, String strAreaEnd, int fromIndex) {
		int pos1 = 0, pos2 = 0;
		int i;
		
		for (i = 1; i < fromIndex; i++) {
			pos1 = urlContent.indexOf(strAreaBegin, pos1) + strAreaBegin.length();
			// System.out.println("在循环里面 pos1的值为："+pos1);
		}
		pos1 = urlContent.indexOf(strAreaBegin, pos1) + strAreaBegin.length();
		pos2 = urlContent.indexOf(strAreaEnd, pos1);
		String ContentArea = urlContent.substring(pos1, pos2).replaceAll("\t|\n", "").replaceAll(" ", "");
		return ContentArea;
	}

	// 写一个清除网页属性的函数
	// 将<table....> <tr......> <td......>分别替换成<table><tr><td>
	public static String replacettt(String string) {
		return string.replaceAll("[\\<]td.*?[\\>]", "<td>").replaceAll("[\\<]tr.*?[\\>]", "<tr>")
				.replaceAll("[\\<]table.*?[\\>]", "<table>");
		// return string;
	}
	/**
	 * 构造一个stack 存放数据 因为 取着方便
	 * 
	 * 此函数已可用
	 * 
	 * @param string
	 * @return
	 */
	public static Stack<String> trStack(String string) {
		// 提取到的效果
		// <td>&nbsp;军训&nbsp;</td><td>1</td><td>85</td><!--<td>3.7</td>--><td>&nbsp;必修&nbsp;</td><td>&nbsp;2013-2014(1)&nbsp;</td>
		// String string = null;
		// System.out.println("当前位置 trSet()");
		int first = 0, second = 0;
		// HashSet<String> trstring = new HashSet<>();
		Stack<String> stack = new Stack<>();
		// 第一行 提取的是标题栏 需要踢出去
		for (int i = 0; i < getTrNum(string, "<h3>"); i++) {
			// System.out.println("匹配前状态\n" + "first=" + first + "second=" +
			// second);
			first = string.indexOf("<h3>", first) + "<h3>".length();// 求得tr的位置
			second = string.indexOf("</h3>", first);// 从first开始 匹配段落
			String h3 = string.substring(first, second);// 提取两个tr之间的内容
			if (i == 0) {
				//这一句 不应该在循环最开始吗
				continue;
			}
			stack.push(h3);
		}
		return stack;
	}
	/**
	 * 本函数求得是一段字符串中 tr的个数 额 tr也需要输入才行 所以 其他的个数 也可以求到
	 * 
	 * @param targetStr
	 *            需匹配的字符串
	 * @param patternStr
	 *            目标字符串
	 * @return
	 */
	public static int getTrNum(String targetStr, String patternStr) {
		int n = 0;
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(targetStr);
		while (matcher.find()) {
			n++;
		}
		return n;
	}
}
