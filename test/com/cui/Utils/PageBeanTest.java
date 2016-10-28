package com.cui.Utils;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.cui.bean.PageBean;

public class PageBeanTest {
	private SessionFactory sessionFactory;
	private Session session;
	private Transaction transaction;

	@Before
	public void init() {
		// 创建配置
		Configuration config = new Configuration().configure();
		// 创建服务注册对象
		ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(config.getProperties())
				.buildServiceRegistry();
		// 创建会话工厂对象
		sessionFactory = config.buildSessionFactory(serviceRegistry);
		// 会话对象
		session = sessionFactory.openSession();
		// 开启事务
		transaction = session.beginTransaction();
	}

	@After
	public void destory() {

		transaction.commit();// 提交事务
		session.close();
		sessionFactory.close();

	}

	@Test
	public void testPageBean() {
		// 生成一个对象
//		PageBean page = new PageBean(1, "title", "url", "utl_md5", new Date());
		PageBean page=new PageBean();
		page.setCreate_date("123");
		page.setPage_URL("URl");
		// 保存对象
		session.save(page);
	}
}
