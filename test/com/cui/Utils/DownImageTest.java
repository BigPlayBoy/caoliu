package com.cui.Utils;

import java.util.Stack;

public class DownImageTest {
	public static void main(String[] args) {
		int begin = 40;
		int length = 5;

		while (begin < 100) {
			Stack<String> urlStack = Image.getPhotoUrl(begin, length);
			String pageUrl = "";
			while (!urlStack.isEmpty()) {
				// title=urlStack.pop();
				pageUrl = urlStack.pop();
				System.out.println("正在打开：" + pageUrl);
				Image.downloadImage(pageUrl);
			}
			begin += length;
		}
	}

}
