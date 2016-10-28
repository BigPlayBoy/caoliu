package com.cui.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.util.Date;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.cui.bean.StudentBean;

public class testStudent {
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
	public void testSaveStudent() throws IOException {
		// 生成学生对象
		System.out.println("begin");
		StudentBean student = new StudentBean();
		student.setSname("张三");
		student.setGender("男");
		student.setSid(1);
		student.setBirthday(new Date());
		
		//Address address=new Address("710068","13102073685","上海");
		//student.setAddress(address);
		
		// 获得照片文件
		File f = new File("e:" + File.separator + "photo.jpg");
		InputStream input = new FileInputStream(f);
		// 创建一个blob对象
		Blob image = Hibernate.getLobCreator(session).createBlob(input, input.available());
		student.setPicture(image);
		
		
		session.save(student);

	}
	
	@Test
	public void testReadStudent()throws Exception{
		StudentBean s=(StudentBean) session.get(StudentBean.class, 0);
		//获得Blob对象
		Blob image=s.getPicture();
		//获得照片的输入流
		InputStream input =image.getBinaryStream();
		//创建输出流
		File f=new File("e:"+File.separator+"1.jpg");
		//获得输出流
		OutputStream output=new FileOutputStream(f);
		//创建缓冲区
		byte[] buff=new byte[input.available()];
		input.read(buff);
		output.write(buff);
		//关闭文件操作
		input.close();
		output.close();
		
	}
	
	@Test
	public void testGetStudent(){
		StudentBean s=(StudentBean) session.get(StudentBean.class, 1);
		System.out.println(s);
	}
	@Test 
	public void testLoadStudent(){
		StudentBean s=(StudentBean) session.load(StudentBean.class, 1);
		System.out.println(s);
	}
	@Test 
	public void testUpdateStudent(){
		StudentBean s=(StudentBean) session.load(StudentBean.class, 1);
		s.setGender("女");
		session.update(s);
	}
	@Test
	public void testDeleteStudent(){
		StudentBean s=(StudentBean) session.load(StudentBean.class, 1);
		session.delete(s);
	}
}
