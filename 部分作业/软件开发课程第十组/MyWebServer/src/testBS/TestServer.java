package testBS;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TestServer {
	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket=null;
		Socket socket=null;
		OutputStream ops=null;
		try {
			//1_创建ServerSocket对象,监听本机的8080端口号
			serverSocket=new ServerSocket(8080);
			while(true) {
				//2_等待来自客户端的请求获取和客户端对应的Socket对象
				socket=serverSocket.accept();
				//3_通过获取到的Socket对象获取到输出流对象
				ops = socket.getOutputStream();
				//4_通过获取到的输出流对象将HTTP协议的响应部分发送到客户端
				ops.write("HTTP/1.1 200 OK\n".getBytes());
				ops.write("Content-Type:text/html;charset=utf-8\n".getBytes());
				ops.write("Server:Apache-Coyote/1.1\n".getBytes());
				ops.write("\n\n".getBytes());
				StringBuffer buf=new StringBuffer();
				buf.append("<html>");
				buf.append("<head><title>I am title</title></head>");
				buf.append("<body>");
				buf.append("<h1> I am header 1</h1>");
				buf.append("<a href='http://www.baidu.com'>baidu</a>");
				buf.append("</body>");
				buf.append("</html>");
				ops.write(buf.toString().getBytes());
				ops.flush();				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			//5_释放资源	
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
