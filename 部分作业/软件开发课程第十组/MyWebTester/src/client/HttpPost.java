package client;

public class HttpPost {
	public String doPost()
    {
    	String uriAPI = "http://XXXXXX";//Post方式没有参数在这里
    	String result = "";
    	HttpPost httpRequst = new HttpPost(uriAPI);//创建HttpPost对象
    	
    	List <NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("str", "I am Post String"));
 
    	
    	try {
			httpRequst.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
//httpRequst.setEntity(new StringEntity("{\"id\":0,\"method\":\"LoadCompanyAllInfo\",\"params\":[{\"CompanyId\":4055,\"CompanyName\":\"\"}]}"));
			HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequst);
		    if(httpResponse.getStatusLine().getStatusCode() == 200)
		    {
		    	HttpEntity httpEntity = httpResponse.getEntity();
		    	result = EntityUtils.toString(httpEntity);//取出应答字符串
		    }
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = e.getMessage().toString();
		}
		catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = e.getMessage().toString();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = e.getMessage().toString();
		}
    	return result;
    }
}
//发送GBK编码  UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(valuePairs, &quot;GBK&quot;); httpPost.setEntity(formEntity);