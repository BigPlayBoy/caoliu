package com.cui.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class DBTest {
	public static void main(String[] args) {
		Connection con=null;
		PreparedStatement pres=null;
		try {
			 con=DBUtil.getConnection();
			 pres=con.prepareStatement("insert into page(title,page_url) values(?,?);");
			 pres.setString(1, "123456");
			 pres.setString(2, "wwwww");
			 pres.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("ok");
	}
}
