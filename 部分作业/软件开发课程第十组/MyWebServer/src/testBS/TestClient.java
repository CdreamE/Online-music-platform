package testBS;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TestClient {
	public static void main(String[] args) throws Exception {
		Socket socket=null;
		InputStream is=null;
		OutputStream ops=null;
		try {
			//1_建立一个Socket对象,连接itcastcn域名的80端口
			socket=new Socket("www.baidu.com",80);
			//2_获取到输出流对象
			is=socket.getInputStream();
			//3_获取到输入流对象
			ops=socket.getOutputStream();
			//4_将HTTP协议的请求部分发送到服务端  /subject/about/index.html
			ops.write("GET /subject/about/index.html HTTP/1.1\n".getBytes());
			ops.write("HOST:www.baidu.com\n".getBytes());
			ops.write("\n".getBytes());
			//5_读取来自服务端的数据打印到控制台
			int i=is.read();
			while(i!=-1) {
				System.out.print((char)i);
				i=is.read();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			//6_释放资源
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
}
