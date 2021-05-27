package client;

public class contect_flask {
	 
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String result = contect_flask.get("http://172.30.12.188:5003/test/hello");
		
		System.out.println(result);
	}
	
	public static String get(String url){
		String result = "";
		HttpGet get = new HttpGet(url);
		try{
			CloseableHttpClient httpClient = HttpClients.createDefault();
			
			HttpResponse response = httpClient.execute(get);
			result = getHttpEntityContent(response);
			
			if(response.getStatusLine().getStatusCode()!=HttpStatus.SC_OK){
				result = "服务器异常";
			}
		} catch (Exception e){
			System.out.println("请求异常");
			throw new RuntimeException(e);
		} finally{
			get.abort();
		}
		return result;
	}
	
	public static String getHttpEntityContent(HttpResponse response) throws UnsupportedOperationException, IOException{
		String result = "";
		HttpEntity entity = response.getEntity();
		if(entity != null){
			InputStream in = entity.getContent();
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));
			StringBuilder strber= new StringBuilder();
			String line = null;
			while((line = br.readLine())!=null){
				strber.append(line+'\n');
			}
			br.close();
			in.close();
			result = strber.toString();
		}
		
		return result;
		
	}

}
