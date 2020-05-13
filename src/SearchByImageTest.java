import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.JavascriptExecutor;


public class SearchByImageTest {
	private WebDriver driver;
	private Map<String, Object> vars;
	private String baseUrl = "http://www.baidu.com";
	private String userDir;
	private Properties prop;
	private BufferedReader bufferedReader;
	
	@Before
	public void setUp() {
		userDir = System.getProperty("user.dir");
		//设置Chrome浏览器的位置
		System.setProperty("webdriver.chrome.driver", userDir + "/config/chromedriver");
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--start-maximized");
		driver = new ChromeDriver(options);
		vars = new HashMap<String, Object>();	
		prop = new Properties();
	}

	@After
	public void tearDown() {
		 driver.quit();
	}	

	@Test
	public void searchByImage() throws IOException {
		//加载到指定url
		driver.get(baseUrl);
		driver.manage().window().maximize();
	    driver.findElement(By.cssSelector(".soutu-btn")).click();
       
        String baseImagePath=userDir + "/baseimage/Golden_Gate_Bridge.jpg";
        //Search By Image
	    driver.findElement(By.cssSelector(".upload-pic")).sendKeys(baseImagePath);
	    //this is for page scroll down
	    try {
		      Thread.sleep(4000);
		 } catch (InterruptedException e) {
		      e.printStackTrace();
		 }
	    ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
	    vars.put("window_handles", driver.getWindowHandles());
	    
	    //Verify search results
	    //Assure that product description contains pre-defined image related keywords
	    bufferedReader = new BufferedReader(new FileReader(userDir + "/config/GoldenGateBridge.properties"));
	    prop.load(bufferedReader);
	    String keyWords = prop.getProperty("GoldenGateBridge");
	    List<String> keyWordsList = new ArrayList<String>();
	    keyWordsList = Arrays.asList(keyWords.split(","));
	    
	    List<WebElement> we = driver.findElements(By.cssSelector(".graph-span6"));
	    for (int i=0; i<we.size(); i++) {
	    	int j=i+1;
	    	String listDesc = ".graph-span6:nth-child(" + j + ")";
	    	String desc = driver.findElement(By.cssSelector(listDesc)).findElement(By.xpath("a/div[3]")).getText();
	    	Assert.assertTrue(desc, containsText(keyWordsList, desc));
	    }
	    
	    //Visit the result specified on a configuration file
	    bufferedReader = new BufferedReader(new FileReader(userDir + "/config/config.properties"));
	    prop.load(bufferedReader);
	    String number = prop.getProperty("VISIT_RESULT");	   

	    String cssSelect = ".graph-span6:nth-child(" + number + ")";
	    driver.findElement(By.cssSelector(cssSelect)).click();
	    vars.put("win02", waitForWindow(4000));
	    driver.switchTo().window(vars.get("win02").toString());
	    //Take a screenshot of the last visited page
	    SeleniumUtils.takeScreenshot(driver);
	
	}
	
	  public String waitForWindow(int timeout) {
		    try {
		      Thread.sleep(timeout);
		    } catch (InterruptedException e) {
		      e.printStackTrace();
		    }
		    Set<String> whNow = driver.getWindowHandles();
		    Set<String> whThen = (Set<String>) vars.get("window_handles");
		    if (whNow!=null & whThen!=null) {
		    	if(whNow.size() > whThen.size()) {
		    		whNow.removeAll(whThen);
		    	}
		    }
		    return whNow.iterator().next();
		  }
	  
	  public boolean containsText(List<String> list, String s) {
		  boolean c = false;
		  for(String i:list) {
			  if(s.indexOf(i)!=-1) {
				  c=true;
				  break;
			  }
		  }
		  return c;
	  }
	  	
}