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
			//1_����һ��Socket����,����itcastcn������80�˿�
			socket=new Socket("www.baidu.com",80);
			//2_��ȡ�����������
			is=socket.getInputStream();
			//3_��ȡ������������
			ops=socket.getOutputStream();
			//4_��HTTPЭ������󲿷ַ��͵������  /subject/about/index.html
			ops.write("GET /subject/about/index.html HTTP/1.1\n".getBytes());
			ops.write("HOST:www.baidu.com\n".getBytes());
			ops.write("\n".getBytes());
			//5_��ȡ���Է���˵����ݴ�ӡ������̨
			int i=is.read();
			while(i!=-1) {
				System.out.print((char)i);
				i=is.read();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			//6_�ͷ���Դ
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
