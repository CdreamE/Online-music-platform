package mytomcatv1;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TestServer {

	//����һ����������ŷ����WebContentĿ¼�ľ���·��
	public static String WEB_ROOT=System.getProperty("user.dir")+"\\"+"WebContent";
	//���徲̬���������ڴ�ű�������ľ�̬ҳ������
	private static String url="";
	public static void main(String[] args) throws IOException {
		//System.out.println(WEB_ROOT);
		ServerSocket serverSocket=null;
		Socket socket=null;
		InputStream is=null;
		OutputStream ops=null;
		try {
			//����ServerSocket,������������80�˿�,�ȴ����Կͻ��˵�����
			serverSocket=new ServerSocket(8080);
			while(true) {
				//��ȡ���ͻ��˶�Ӧ��socket
				socket= serverSocket.accept();
				//��ȡ������������
				is = socket.getInputStream();
				//��ȡ�����������
				ops = socket.getOutputStream();
				//��ȡHTTPЭ������󲿷�,��ȡ�ͻ���Ҫ���ʵ���Դ����,�������Դ���Ƹ�ֵ��url
				parse(is);
				//���;�̬��Դ
				sendStaticResource(ops);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			//�ͷ���Դ
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
	
	
	//��ȡHTTPЭ������󲿷�,��ȡ�ͻ���Ҫ���ʵ���Դ����,�������Դ���Ƹ�ֵ��url
	private static void parse(InputStream is) throws IOException {
		//����һ�����������HTTPЭ�����󲿷�����
		StringBuffer content=new StringBuffer(2048);
		//����һ�����飬���HTTPЭ�����󲿷�����
		byte[] buffer=new byte[2048];
		//����һ������i,�����ȡ���ݵ�������֮���������Ĵ�С
		int i=-1;
		//��ȡ�ͻ��˷��͹���������,�����ݶ�ȡ���ֽ�����buffer��.i�����ȡ�������Ĵ�С
		i=is.read(buffer);
		//�����ֽ�����,�������е�����׷�ӵ�content������
		for(int j=0;j<i;j++) {
			content.append((char)buffer[j]);
		}
		//��ӡHTTPЭ�����󲿷�����
		System.out.println(content);
		//��ȡ�ͻ���Ҫ�������Դ·�� demo.html,��ֵ��url
		parseUrl(content.toString());
	}
	
	

	//��ȡ�ͻ���Ҫ�������Դ·�� demo.html,��ֵ��url
	private static void parseUrl(String content) {
		//����2����������������е�2���ո��λ��
		int index1,index2;
		//��ȡhttp���󲿷ֵ�1���ո��λ��
		index1=content.indexOf(" ");
		if(index1!=-1) {
			index2=content.indexOf(" ",index1+1);
			//��ȡhttp���󲿷ֵ�2���ո��λ��
			if(index2>index1) {
				//��ȡ�ַ�����ȡ������������Դ������
				url=content.substring(index1+2, index2);
			}
		}
		//��ӡ��������̬��Դ����
		System.out.println(url);
	}


	//���;�̬��Դ
	private static void sendStaticResource(OutputStream ops) throws IOException {
		//����һ���ֽ�����,���ڴ�ű�������ľ�̬��Դdemo01.html������
		byte[] bytes=new byte[2048];
		//����һ���ļ�������,�û���ȡ��̬��Դdemo01.html�е�����
		FileInputStream fis=null;
		try {
			//�����ļ�����File,������Ҫ�������Դdemo01.html
			File file=new File(WEB_ROOT,url);
			//����ļ�����
			if(file.exists()) {
				//��ͻ������HTTPЭ�����Ӧ��/��Ӧͷ
				ops.write("HTTP/1.1 200 OK\n".getBytes());
				ops.write("Server:apache-Coyote/1.1\n".getBytes());
				ops.write("Content-Type:text/html;charset=utf-8\n".getBytes());
				ops.write("\n".getBytes());
				//��ȡ���ļ�����������
				fis=new FileInputStream(file);
				//��ȡ��̬��Դdemo01.html�е����ݵ�������
				int ch=fis.read(bytes);
				while(ch!=-1) {
					//����ȡ�������е�����ͨ����������͵��ͻ���				
					ops.write(bytes, 0, ch);
					ch=fis.read(bytes);
				}
			}else {
				//����ļ�������
			    //��ͻ�����Ӧ�ļ���������Ϣ 	
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
			//�ͷ��ļ�����������
			if(null!=fis) {
				fis.close();
				fis=null;
			}
		}
		


		

	}
}
