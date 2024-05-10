package com.eulerity.hackathon.imagefinder;


import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.mockito.Mockito;

import com.eulerity.hackathon.imagefinder.ImageFinder;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import org.mockito.cglib.core.CollectionUtils;

public class ImageFinderTest {

	public HttpServletRequest request;
	public HttpServletResponse response;
	public StringWriter sw;
	public HttpSession session;

	@Before
	public void setUp() throws Exception {
		request = Mockito.mock(HttpServletRequest.class);
		response = Mockito.mock(HttpServletResponse.class);
    sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw, true);
		Mockito.when(response.getWriter()).thenReturn(pw);
		Mockito.when(request.getRequestURI()).thenReturn("/foo/foo/foo");
		Mockito.when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/foo/foo/foo"));
		session = Mockito.mock(HttpSession.class);
		Mockito.when(request.getSession()).thenReturn(session);
	}
	
  @Test
  public void test() throws IOException, ServletException {
		Mockito.when(request.getServletPath()).thenReturn("/main");
		Mockito.when(request.getParameter("url")).thenReturn(new String("https://naadborole.github.io/TestWebsite/index.html"));
		new ImageFinder().doPost(request, response);
 		String t = sw.toString();
	  	JsonElement element = new Gson().fromJson(sw.toString(), JsonElement.class);
	    Assert.assertTrue(element.isJsonArray());
		List<String> imgs = new ArrayList<String>();
		for(JsonElement jsele : element.getAsJsonArray()){
			JsonObject jsobj = jsele.getAsJsonObject();
			imgs.add(jsobj.get("link").getAsString());
		}
		List<String> expected = Arrays.asList("https://picsum.photos/id/1/200/300", "https://picsum.photos/id/10/200/300", "https://picsum.photos/id/13/200/300", "https://picsum.photos/id/16/200/300", "https://picsum.photos/id/26/200/300", "https://picsum.photos/id/28/200/300");
		Assert.assertTrue(imgs.size() == expected.size());
		Assert.assertTrue(imgs.containsAll(expected));
  }
}



