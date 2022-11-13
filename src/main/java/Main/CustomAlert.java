package Main;

import com.google.gson.Gson;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CustomAlert {
    public String url;
    public String id;
    public String heading;
    public String description;
    public String imageUrl;
    public String postedBy;
    public int priceInCents;
    public String postDate;
    public int alertType;

    public Boolean login;
    WebDriver driver;

    public CustomAlert(String url, String id, String heading, String desc, String imageUrl, String postedBy, int priceInCents, String postDate, int alertType) {
        this.url = url;
        this.id = id;
        this.description = desc;
        this.heading = heading;
        this.imageUrl = imageUrl;
        this.postedBy = postedBy;
        this.priceInCents = priceInCents;
        this.postDate = postDate;
        this.alertType = alertType;
        login = false;
    }

    public CustomAlert() {
        login = false;
    }

    public boolean CreatePostRequest(ArrayList<CustomAlert> alerts) {
        Gson gson = new Gson();
        boolean success = false;
        for (CustomAlert alert : alerts) {
            String json = gson.toJson(alert);
            try {
                URL url = new URL("https://api.marketalertum.com/Alert");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type","application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setDoOutput(true);

                try(OutputStream os = con.getOutputStream()){
                    byte[] input = json.getBytes("utf-8");
                    os.write(input,0,input.length);
                }

                try(BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream(),"utf-8"))){
                    StringBuilder response = new StringBuilder();
                    String respondLine = null;
                    while ((respondLine = br.readLine()) != null){
                        response.append(respondLine.trim());
                    }
                    System.out.println(response.toString());
                    success = true;
                }catch(IOException e){
                    return false;
                }

            } catch (MalformedURLException e) {
                success = false;
                throw new RuntimeException(e);
            } catch (IOException e) {
                success = false;
                throw new RuntimeException(e);
            }
        }
        return success;
    }

    public WebDriver StartDriver() {
        this.driver = new ChromeDriver();
        return driver;
    }
    public ArrayList<CustomAlert> ScrapeEbayForItems( int range) {

        WebDriver driver;
        System.setProperty("webdriver.chrome.driver", "C:/Users/Drew/Desktop/WebTesting/chromedriver.exe");
        driver = StartDriver();
        CustomAlert test = new CustomAlert();

        driver.get("https://www.ebay.com/");
        driver.switchTo().activeElement().sendKeys(Keys.TAB);
        driver.switchTo().activeElement().sendKeys(Keys.TAB);
        driver.switchTo().activeElement().sendKeys(Keys.TAB);
        driver.switchTo().activeElement().sendKeys(Keys.TAB);

        WebElement searchField = driver.findElement(By.name("_nkw"));
        searchField.sendKeys("Camera");
        WebElement searchButton = driver.findElement(By.id("gh-btn"));
        searchButton.submit();


        List<WebElement> listItem = driver.findElements(By.className("s-item"));
        ArrayList<CustomAlert> alerts = new ArrayList<CustomAlert>();
        ArrayList<CustomAlert> finalAlerts = new ArrayList<CustomAlert>();

        for(int i = 0; i < range + 1;i++) {
            WebElement item = listItem.get(i);
            String url = item.findElement(By.tagName("a")).getAttribute("href");
            String heading = item.findElement(By.className("s-item__title")).getText();
            String desc = item.findElement(By.className("s-item__subtitle")).getText();
            int priceInCents = 0;
            String id = item.getAttribute("data-viewport");
            int alertType = 6;
            String imageSrc = item.findElement(By.className("s-item__image-wrapper")).findElement(By.tagName("img")).getAttribute("src");
            //No Post Date could be found from the ebay website
            String postDate = "2022-10-11T21:38:54.3080651Z";
            String postedBy = "00de4d33-5d10-4151-ad8f-39dca960ddce";

            String priceString = item.findElement(By.className("s-item__price")).getText().replace("$","").replace(",","");
            if(priceString.contains("to")){
                double higher = Double.parseDouble(priceString.substring(priceString.indexOf("to")+3));
                double lower = Double.parseDouble(priceString.substring(0,priceString.indexOf("to")));
                double tempPrice = 10* ((higher + lower)/2);
                String finalPrice = Double.toString(tempPrice);
                finalPrice = finalPrice.replace(".","");
                if(finalPrice.length() > 4){
                    priceInCents = Integer.parseInt(finalPrice.substring(0,4));
                }

            }else {
                String temp = item.findElement(By.className("s-item__price")).getText().replace("$","").replace(".","").replace(",","");
                if(temp.length() > 0){
                    priceInCents = Integer.parseInt(temp);
                }
            }
            alerts.add(new CustomAlert(url,id,heading,desc,imageSrc,postedBy,priceInCents,postDate,alertType));
        }
        driver.quit();
        alerts.remove(0);

        for (int i = 0; i < range; i++) {
            finalAlerts.add(alerts.get(i));
        }
        return finalAlerts;
    }

    public void LoginToWebsite(String userID) {
        System.setProperty("webdriver.chrome.driver", "C:/Users/Drew/Desktop/WebTesting/chromedriver.exe");
        driver = new ChromeDriver();
        driver.get("https://www.marketalertum.com/");

        driver.switchTo().activeElement().sendKeys(Keys.TAB);
        driver.switchTo().activeElement().sendKeys(Keys.TAB);
        driver.switchTo().activeElement().sendKeys(Keys.TAB);
        driver.switchTo().activeElement().sendKeys(Keys.TAB);
        List<WebElement> list = driver.findElements(By.className("nav-item"));

        WebElement temp = list.get(list.size()-1);
        temp.click();
        temp = driver.findElement(By.name("UserId"));
        temp.sendKeys(userID);
        driver.switchTo().activeElement().sendKeys(Keys.TAB);
        temp.submit();
        try{
            String tempString = driver.findElement(By.tagName("h1")).getText();
            if(tempString.contains("alerts")){
                login = true;
            }
        }catch (Exception e){

        }
        driver.quit();

    }


    public List<WebElement> CheckingAlertLayout() {

        System.setProperty("webdriver.chrome.driver", "C:/Users/Drew/Desktop/WebTesting/chromedriver.exe");
        driver = new ChromeDriver();
        driver.get("https://www.marketalertum.com/");

        driver.switchTo().activeElement().sendKeys(Keys.TAB);
        driver.switchTo().activeElement().sendKeys(Keys.TAB);
        driver.switchTo().activeElement().sendKeys(Keys.TAB);
        driver.switchTo().activeElement().sendKeys(Keys.TAB);
        List<WebElement> list = driver.findElements(By.className("nav-item"));

        WebElement temp = list.get(list.size()-1);
        temp.click();
        temp = driver.findElement(By.name("UserId"));
        temp.sendKeys("00de4d33-5d10-4151-ad8f-39dca960ddce");
        driver.switchTo().activeElement().sendKeys(Keys.TAB);
        temp.submit();

        list = driver.findElements(By.tagName("table"));

        return list;
    }

    public void DestroyDriver() {
        this.driver.quit();
    }

    public int SendDeleteRequest(String userID) {

        try {
            URL url = new URL("https://api.marketalertum.com/Alert?userId="+userID);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("DELETE");
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded" );
            con.connect();
            return con.getResponseCode();
        }catch (Exception e){

        }
        return 0;
    }

}
