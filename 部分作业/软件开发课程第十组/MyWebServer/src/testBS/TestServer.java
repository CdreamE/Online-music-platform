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
			//1_����ServerSocket����,����������8080�˿ں�
			serverSocket=new ServerSocket(8080);
			while(true) {
				//2_�ȴ����Կͻ��˵������ȡ�Ϳͻ��˶�Ӧ��Socket����
				socket=serverSocket.accept();
				//3_ͨ����ȡ����Socket�����ȡ�����������
				ops = socket.getOutputStream();
				//4_ͨ����ȡ�������������HTTPЭ�����Ӧ���ַ��͵��ͻ���
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
			//5_�ͷ���Դ	
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
