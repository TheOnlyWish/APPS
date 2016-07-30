package com.king.loadnote.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;

import com.king.loadnote.domain.Note;
import com.king.loadnote.domain.UploadFile;

/**
 * 停用
 * @author Administrator
 *
 */
public class FileUploadUtils {

	
		/**
		 * 发送post请求上传文本
		 * @param url
		 * @param files
		 * @param note
		 * @return
		 * @throws Exception 
		 */
	    public static boolean post(String url, UploadFile uf, String json) throws Exception{
	    	HttpClient client=new DefaultHttpClient();// 开启一个客户端 HTTP 请求 
	    	HttpPost post = new HttpPost(url);//创建 HTTP POST 请求  
	    	MultipartEntityBuilder builder = MultipartEntityBuilder.create();
	    			builder.setCharset(Charset.forName("uft-8"));//设置请求的编码格式
	    	int count=0;
	    	List<File> files = uf.files;
	    	List<String> names = uf.fileNames;
	    	List<String> types = uf.fileTypes;
	    	StringBuilder fileNames = new StringBuilder();
	    	StringBuilder fileTypes = new StringBuilder();
	    	for (int i = 0; i < files.size(); i++) {
//	    				FileBody fileBody = new FileBody(file);//把文件转换成流对象FileBody
//	    				builder.addPart("file"+count, fileBody);
	    		// 添加文件
	    		builder.addBinaryBody("file"+count, files.get(i));
	    		// 添加名称
	    		fileNames.append(names.get(i));
	    		fileTypes.append(types.get(i));
	    		
	    		count++;
	    	}		
	    	builder.addTextBody("fileTypes", fileTypes.toString());//设置请求参数
	    	builder.addTextBody("fileNames", fileNames.toString());//设置请求参数
	    	builder.addTextBody("json", json);//设置请求参数
	    	HttpEntity entity = builder.build();// 生成 HTTP POST 实体  	
	    	post.setEntity(entity);//设置请求参数
	    	HttpResponse response = client.execute(post);// 发起请求 并返回请求的响应
	    	if (response.getStatusLine().getStatusCode()==200) {
	    		return true;
	    	}
	    	return false;	
	    }
	    
	
}
