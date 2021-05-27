package testBS;

	import java.io.File;

	/**
	 * @author 20171001234 xxx
	 *
	 *������ codes/lab1/Ŀ¼�´������½ṹ���ļ���֯��
	*����Java����ʵϰ-201710001234-xxx-ʵϰ1
	*��  ����Java����ʵϰ-20171000123-xxx-ʵϰ1
	*��  ��  ����lab1_code
	*��  ��      ����rules
	*��  ��      ����turtle
	*��  ����lab1_code
	*��      ����rules
	*��      ����turtle
	*����Java����ʵϰ-20171001235-xxx-ʵϰһ
	*��  ����lab1
	*��      ����lab1_code
	*��          ����lab1_code
	*��              ����bin
	*��              ��  ����rules
	*��              ��  ����turtle
	*��              ����rules
	*��              ����turtle
	*����Java����ʵϰ-20171001236-xxxx-ʵϰһ
	*��  ����rules
	*��  ����turtle
	*����Java����ʵϰ20171001237-xxxx-ʵϰһ
	*    ����Java����ʵϰ20171001237-xxx-ʵϰһ
	*       ����Java����ʵϰ20171001237-xxxx-ʵϰһ
	*            ����lab1_code
	*               ����123
	*                ����rules
	*                ��  ����bin
	*               ����turtle
	*                    ����bin
	*
	*/
	public class ClosestMatches {
		
		/**
		 * �������۸����Ŀ¼�£�ָ���ļ��������ԡ�
		 * Similarity   ��Ŀ¼1                        ��Ŀ¼2
		 * 100%        Java����ʵϰ-201710001234-xxx-ʵϰ1     Java����ʵϰ-201710001235-xxx-ʵϰ1
		 * 89%         Java����ʵϰ-201710001234-xxx-ʵϰ1     Java����ʵϰ-201710001236-xxx-ʵϰ1
		 * ....
		 * @param path ��ҵ�ļ����ڵ�Ŀ¼�����������ǣ�codes/lab1
		 * @param fileNameMatches���������˽��бȽϵ��ļ������ļ�������������ʽ.
		 * �� "DrawableTurtle.java"��"*.java","turtle/*.java"
		 * ���һ����Ŀ¼���ж�������������ļ���������ļ��ϲ���һ���ļ���
		 * 
		 * @param topRate:ȡֵ��Χ��[0,100],������Ƶ���ֵ
		 * 	�Ӹ������������topRate%�����б���
		 * @param removeComments:�Ƿ��Ƴ�ע������	
		 * 	 */
		
		static String FilePath = null;
		//public static void closestCodes(String path, String fileNameMatches,double topRate,boolean removeComments)
		public static void closestCodes(String path, String fileNameMatches)
		{	
			// �����ļ���������ԣ����
			FilePath = path;
			CodeFile.function(fileNameMatches);
		}
		
		/**
		 * @param args
		 */
		
		public static void main(String[] args) {
			// TODO Auto-generated method stub
			closestCodes("C:\\Users\\Administrator\\Desktop\\test","TurtleSoup.java");
		}

	}


