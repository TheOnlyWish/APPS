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
 * ͣ��
 * @author Administrator
 *
 */
public class FileUploadUtils {

	
		/**
		 * ����post�����ϴ��ı�
		 * @param url
		 * @param files
		 * @param note
		 * @return
		 * @throws Exception 
		 */
	    public static boolean post(String url, UploadFile uf, String json) throws Exception{
	    	HttpClient client=new DefaultHttpClient();// ����һ���ͻ��� HTTP ���� 
	    	HttpPost post = new HttpPost(url);//���� HTTP POST ����  
	    	MultipartEntityBuilder builder = MultipartEntityBuilder.create();
	    			builder.setCharset(Charset.forName("uft-8"));//��������ı����ʽ
	    	int count=0;
	    	List<File> files = uf.files;
	    	List<String> names = uf.fileNames;
	    	List<String> types = uf.fileTypes;
	    	StringBuilder fileNames = new StringBuilder();
	    	StringBuilder fileTypes = new StringBuilder();
	    	for (int i = 0; i < files.size(); i++) {
//	    				FileBody fileBody = new FileBody(file);//���ļ�ת����������FileBody
//	    				builder.addPart("file"+count, fileBody);
	    		// ����ļ�
	    		builder.addBinaryBody("file"+count, files.get(i));
	    		// �������
	    		fileNames.append(names.get(i));
	    		fileTypes.append(types.get(i));
	    		
	    		count++;
	    	}		
	    	builder.addTextBody("fileTypes", fileTypes.toString());//�����������
	    	builder.addTextBody("fileNames", fileNames.toString());//�����������
	    	builder.addTextBody("json", json);//�����������
	    	HttpEntity entity = builder.build();// ���� HTTP POST ʵ��  	
	    	post.setEntity(entity);//�����������
	    	HttpResponse response = client.execute(post);// �������� �������������Ӧ
	    	if (response.getStatusLine().getStatusCode()==200) {
	    		return true;
	    	}
	    	return false;	
	    }
	    
	
}
