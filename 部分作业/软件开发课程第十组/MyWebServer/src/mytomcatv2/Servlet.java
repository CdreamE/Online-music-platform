package mytomcatv2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
//���з���˵�JAVAС����Ҫʵ�ֵĽӿ�
public interface Servlet {
	//��ʼ��
	public void init();
	//����
	public void Service(InputStream is,OutputStream ops) throws IOException;
	//����
	public void destroy();
}
