package mytomcatv2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class TestServer {

	//定义一个变量，存放服务端WebContent目录的绝对路径
	public static String WEB_ROOT=System.getProperty("user.dir")+"\\"+"WebContent";
	//定义静态变量，用于存放本次请求的静态页面名称
	private static String url="";
	
	//定义一个静态类型MAP，存储服务端conf.properties中的配置信息
	private static Map<String,String> map=new HashMap<String,String>();
	
	static {
		//服务器启动之前将配置参数中的信息加载到MAP中
		//创建一个Properties对象
		Properties prop=new Properties();
		try {
			//加载WebContent目录下的conf.properties文件
			prop.load(new FileInputStream(WEB_ROOT+"\\conf.properties"));
			//将配置文件中的数据读取到MAP中	
			Set set=prop.keySet();
			Iterator iterator = set.iterator();
			while(iterator.hasNext()) {
				String key=(String)iterator.next();
				String value = prop.getProperty(key);
				map.put(key, value);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	
	public static void main(String[] args) throws IOException {
		System.out.println(map);
		//System.out.println(WEB_ROOT);
		ServerSocket serverSocket=null;
		Socket socket=null;
		InputStream is=null;
		OutputStream ops=null;
		try {
			//创建ServerSocket,监听本机器的80端口,等待来自客户端的请求
			serverSocket=new ServerSocket(8080);
			while(true) {
				//获取到客户端对应的socket
				socket= serverSocket.accept();
				//获取到输入流对象
				is = socket.getInputStream();
				//获取到输出流对象
				ops = socket.getOutputStream();
				//获取HTTP协议的请求部分,截取客户端要访问的资源名称,将这个资源名称赋值给url demo01.html aa
				parse(is);
				//判断本次请求的是静态demo.html页面还是运行在服务端一段JAVA小程序
				if(null!=url) {
					if(url.indexOf(".")!=-1) {
						//发送静态资源文件
						sendStaticResource(ops);
					}else {
						//发送动态资源
						sendDynamicResource(is,ops);
					}
				}
				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			//释放资源
			if(null!=is) {
				is.close();
				is=null;
			}
			
			if(null!=ops) {
				ops.close();
				ops=null;
			}
			
			if(null!=socket) {
				socket.close();
				socket=null;
			}
		}
		
	}
	
	
	private static void sendDynamicResource(InputStream is, OutputStream ops) throws Exception {
		//将HTTP协议的响应行和响应头发送到客户端
		ops.write("HTTP/1.1 200 ok\n".getBytes());
		ops.write("Server:Apache\n".getBytes());
		ops.write("Content-type:text/html;charset=utf-8\n".getBytes());
		ops.write("\n".getBytes());
		//判断map中的是否存在一个key,这个key是否和本次待请求的资源路径一致
		if(map.containsKey(url)) {
			//如果包含指定的key,获取到MAP中key对应的value部分
			String value=map.get(url);
			//通过反射将对应的JAVA程序加载到内存
			Class clazz=Class.forName(value);
			Servlet servlet=(Servlet)clazz.newInstance();
			//执行init方法
			servlet.init();
			//执行service方法
			servlet.Service(is, ops);
		}

	}



	//获取HTTP协议的请求部分,截取客户端要访问的资源名称,将这个资源名称赋值给url
	private static void parse(InputStream is) throws IOException {
		//定义一个变量，存放HTTP协议请求部分数据
		StringBuffer content=new StringBuffer(2048);
		//定义一个数组，存放HTTP协议请求部分数据
		byte[] buffer=new byte[2048];
		//定义一个变量i,代表读取数据到数组中之后，数据量的大小
		int i=-1;
		//读取客户端发送过来的数据,将数据读取到字节数组buffer中.i代表读取数据量的大小
		i=is.read(buffer);
		//遍历字节数组,将数组中的数据追加到content变量中
		for(int j=0;j<i;j++) {
			content.append((char)buffer[j]);
		}
		//打印HTTP协议请求部分数据
		System.out.println(content);
		//截取客户端要请求的资源路径 demo.html,赋值给url
		parseUrl(content.toString());
	}
	
	

	//截取客户端要请求的资源路径 demo.html,赋值给url
	private static void parseUrl(String content) {
		//定义2个变量，存放请求行的2个空格的位置
		int index1,index2;
		//获取http请求部分第1个空格的位置
		index1=content.indexOf(" ");
		if(index1!=-1) {
			index2=content.indexOf(" ",index1+1);
			//获取http请求部分第2个空格的位置
			if(index2>index1) {
				//截取字符串获取到本次请求资源的名称
				url=content.substring(index1+2, index2);
			}
		}
		//打印本次请求静态资源名称
		System.out.println(url);
	}


	//发送静态资源
	private static void sendStaticResource(OutputStream ops) throws IOException {
		//定义一个字节数组,用于存放本次请求的静态资源demo01.html的内容
		byte[] bytes=new byte[2048];
		//定义一个文件输入流,用户获取静态资源demo01.html中的内容
		FileInputStream fis=null;
		try {
			//创建文件对象File,代表本次要请求的资源demo01.html
			File file=new File(WEB_ROOT,url);
			//如果文件存在
			if(file.exists()) {
				//向客户端输出HTTP协议的响应行/响应头
				ops.write("HTTP/1.1 200 OK\n".getBytes());
				ops.write("Server:apache-Coyote/1.1\n".getBytes());
				ops.write("Content-Type:text/html;charset=utf-8\n".getBytes());
				ops.write("\n".getBytes());
				//获取到文件输入流对象
				fis=new FileInputStream(file);
				//读取静态资源demo01.html中的内容到数组中
				int ch=fis.read(bytes);
				while(ch!=-1) {
					//将读取到数组中的内容通过输出流发送到客户端				
					ops.write(bytes, 0, ch);
					ch=fis.read(bytes);
				}
			}else {
				//如果文件不存在
			    //向客户端响应文件不存在消息 	
				ops.write("HTTP/1.1 404 not found\n".getBytes());
				ops.write("Server:apache-Coyote/1.1\n".getBytes());
				ops.write("Content-Type:text/html;charset=utf-8\n".getBytes());
				ops.write("\n".getBytes());
				String errorMessage="file not found";
				ops.write(errorMessage.getBytes());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			//释放文件输入流对象
			if(null!=fis) {
				fis.close();
				fis=null;
			}
		}
		


		

	}
}
