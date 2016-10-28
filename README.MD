#:w0529
##https://github.com/BigPlayBoy/cao.git
##可以获取帖子的内容 但是下载图片会出现不响应(就是什么都不出现  发生在保存图片的时候)
##下载图片使用的是HttpClient  获取帖子的网址和照片的网址 还是使用URI
##添加分支 http  完成http get/post的功能 
-------------------------------
#已完成：
##	与数据库的连接
##	获取链接(使用的urlconnection 需要改写成httpclient  )
-------------------------------
#未完成：
##	httpclient工具类
	
##	添加hibernate？？对JDBC进行了轻量级的对象封装  对象持久化
	
	
##	openSession与getCurrentSession的区别
##	1.	getCurrentSession在事务提交或回滚之后会自动关闭，openSession需要手动关闭(不关闭的话 会造成连接池溢出)
##	2. openSession每次创建新的session对象 getCurrentSession使用现有的session对象(有点像单例模式)


#10:28
##现在可以获得用户的发帖列表，图片下载没有做，bt文件下载也没有做


