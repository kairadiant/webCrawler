
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.Connection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class webCrawler {
	public static ArrayList<String> imgUrls = new ArrayList<String>();
	
  
    private static void webCrawl(int depth, String url, ArrayList<String> urlsVisited) {
    	if(depth <=5) {
    		Document doc = request(url, urlsVisited);
    		
    		if(doc != null) {
    			for(Element link: doc.select("a[href]")) {
    				String nextUrl = link.absUrl("href");
    				if(!link.text().contains("://")) {
    					Elements imgsUrl = doc.select("img[src]");
    					for(Element imgLink : imgsUrl) {
    						String imgUrl = imgLink.attr("src");
    						Pattern pattern = Pattern.compile("(jpg)$", Pattern.CASE_INSENSITIVE);
    						Matcher matcher = pattern.matcher(imgUrl);
    						if(matcher.find()) {
    							System.out.println("IM URL: "+ imgUrl);
    							imgUrls.add(imgUrl);
    						}
    					}
    				}
    				if(urlsVisited.contains(nextUrl) == false) {
    					webCrawl(depth++,nextUrl,urlsVisited);
    				}
    			}
    		}
    	}
    }
    private static Document request(String url, ArrayList<String> urls) {
    	try {
    		Connection con = Jsoup.connect(url);
    		Document doc = con.get();
    		
    		if(con.response().statusCode() == 200) {
    			//System.out.println("Link: "+ url);
    			urls.add(url);
    			
    			return doc;
    		}
    		return null;
    	}catch(IOException e) {
    		return null;
    	}
    }
    public static void main(String [] args) {
    	String url = "https://en.wikipedia.org/wiki/Wolfenstein_3D";
    	webCrawl(1, url, new ArrayList<String>());
    }
} 