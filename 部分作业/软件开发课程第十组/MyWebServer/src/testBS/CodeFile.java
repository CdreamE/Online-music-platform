package testBS;




	import java.io.File;
	import java.io.FileNotFoundException;
	import java.io.FileWriter;
	import java.util.HashMap;
	import java.util.Scanner;
	import testBS.ClosestMatches;


	public class CodeFile {
		//static File file1 = new File("C:\\Users\\Administrator\\Desktop\\test");
		static File file1 = new File(ClosestMatches.FilePath);
		static FileWriter fw = null;  
			
		public static void function(String checkname) {
			//file1 = fp;
			try {
				fw = new FileWriter(file1.getPath() + "/"  + "Summary.txt");   
				if(!file1.isDirectory())
				{
					System.out.print("�����ļ�Ϊ\""+file1.getName()+"\""+"\n");  
					System.out.print("�������ļ��е���ȷ·��");  
				}
				else{			
					//System.out.print("����·��Ϊ�ļ���\n");  
					File[]files = file1.listFiles();		
					for (File f:files) {		
						getFile(f,checkname);          //"TurtleSoup.java"
					}				
				}
				getPair();
				fw.flush();
	    		fw.close();
	    		
			}catch (Exception e) {
	    		e.printStackTrace();
	    	}
		}

		static int num=0;
		public static void getFile(File f,String str) {		   //����ֻ�Ǻ�׺��    ����������ʽ
			//int n=0;
			if(f.isDirectory()){    //�����������Ŀ¼�������ȡ
				File[]subfiles = f.listFiles();
				for(File fi:subfiles){
					getFile(fi,str);
				}
			}
			else{		
				if(f.getName().equals(str)) {   //f.getName().indexOf(str) >0    ������  ����.java  �������java�ļ�
					 
//					try{
//						fw.write(f.getName());
//						fw.write("\r\n");
//					}catch (Exception e) {
//			    		e.printStackTrace();
//			    	}
					
					System.out.print(f.getName()); 
					System.out.print("\n");  
					num++;
					getData(num,f);
				}
			}
		}
		
		static HashMap<Integer,String> PathMap = new HashMap<Integer,String>();
		 
		public static void getData(int n,File f) {         
			// ֮������������������ת ��ȫ����Ϊ�Ҳ�֪��װ�ļ�������
			String str = f.getPath();
			PathMap.put(n, str);	
		}

		public static void getPair() {
			int temp = 0;
			String[]p1 = new String[num];
			String[]p2 = new String[num];
			float []simi = new float[num];
			for(int i=1;i<num;i++) {
				for(int j=i+1;j<=num;j++) {
					String path1 = PathMap.get(i);
					String path2 = PathMap.get(j);			
					File f1 = new File(path1);
					File f2 = new File(path2);
					float aaa = getSimilarity(f1, f2);		
						
					p1[temp] = path1;
					p2[temp] = path2;
					simi[temp]=aaa;
//					System.out.print(aaa);  			
//					sortResult(simi,p1,p2);			
					try{
						fw.write("�ļ�1��");
						fw.write(path1.substring(file1.getPath().length())+"\r\n");
						fw.write("�ļ�2��");
						fw.write(path2.substring(file1.getPath().length())+"\r\n");
						fw.write("���ƶȣ�");
						fw.write(aaa+"\r\n");
						fw.write("\r\n");
					}catch (Exception e) {
			    		e.printStackTrace();
			    	}			
					
				}
			
			}
		}
		
//		public static void sortResult(float[]simi,String []p1,String []p2) {
//			for(int i=0; i<simi.length-1; i++)
//			{
//				for(int j=i+1; j<simi.length; j++)//Ϊʲôy�ĳ�ʼ��ֵ�� x+1��	��Ϊÿһ�αȽϣ�										//����x�Ǳ��ϵ�Ԫ�غ���һ��Ԫ�ؽ��бȽϡ�
//				{
//					if(simi[i]<simi[j])
//					{
//						float temp = simi[j];
//						simi[j] = simi[i];
//						simi[i] = temp;
//						
//						String st = p1[j];
//						p1[j] = p1[i];
//						p1[i] = st;
//						
//						st = p2[j];
//						p2[j] = p2[i];
//						p2[i] = st;
//					}
//				}
//			}
//			for(int i=0;i<simi.length;i++) {
//				System.out.print(simi[i]+",");
//	        }
//		}
		
		public static float getSimilarity(File f1, File f2){	
			HashMap<String, Float> file1 = new HashMap<String, Float>() ;
			HashMap<String, Float> file2 = new HashMap<String, Float>() ;
			file1 = readFile(f1);
			file2 = readFile(f2);
			float mod1=0;
			float mod2=0;
			float divided=0;
			for(String word : file1.keySet()){
				divided += file1.get(word)*file2.get(word);
				mod1 += file1.get(word)*file1.get(word);
			}
			for(String word : file2.keySet()){
				mod2 += file2.get(word)*file2.get(word);
			}
			mod1 = (float)Math.sqrt(mod1);
			mod2 = (float)Math.sqrt(mod2);
			float similarity = divided/(mod1*mod2);
			return similarity;
		}
		
		public static HashMap<String,Float> readFile(File f) {
			HashMap <String,Float> treeMap = new HashMap<String,Float>(){
				public Float get(Object key) {
					if(! containsKey(key))
						return (float)0;
					return super.get(key);
				}
			};
			try {
				Scanner code= new Scanner(f);
				while(code.hasNext()){;
					String str = code.next();
					treeMap.put(str,(treeMap.get(str))+1);
				}
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			return treeMap;
		}
	}

