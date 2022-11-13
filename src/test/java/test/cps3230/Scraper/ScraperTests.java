package test.cps3230.Scraper;

import Main.CustomAlert;
import org.junit.jupiter.api.*;


import java.util.ArrayList;

public class ScraperTests {


    @Test
    public void GetListItemsFromScraping(){
        CustomAlert testAlert = new CustomAlert();
        Assertions.assertNotEquals(0,testAlert.ScrapeEbayForItems(5).size());
    }



    @Test
    public void SendValidPostRequest() {
        ArrayList<CustomAlert> alerts = new ArrayList<CustomAlert>();
        CustomAlert test = new CustomAlert();

        alerts.add(new CustomAlert("https://www.amazon.co.uk/Windows-Display-Ultrabook-Processor-Bluetooth","1234","Jumper Windows 11 Laptop","description","https://m.media-amazon.com/images/I/712Xf2LtbJL._AC_SX679_.jpg","00de4d33-5d10-4151-ad8f-39dca960ddce",12345,"2022-11-07T11:42:09.2046689Z",6));

        Assertions.assertEquals(true,test.CreatePostRequest(alerts));
    }

    @Test
    public void SendInvalidPostRequest() {
        ArrayList<CustomAlert> alerts = new ArrayList<CustomAlert>();
        CustomAlert test = new CustomAlert();

        alerts.add(new CustomAlert("","","","","","",123,"",1234));
        Assertions.assertEquals(false, test.CreatePostRequest(alerts));
    }

    @Test
    public void SendValidDeleteRequest() {
        CustomAlert temp = new CustomAlert();
        Assertions.assertEquals(200,temp.SendDeleteRequest("00de4d33-5d10-4151-ad8f-39dca960ddce"));
    }

    @Test
    public void sendInvalidDeleteRequest(){
        CustomAlert temp = new CustomAlert();
        Assertions.assertEquals(400, temp.SendDeleteRequest(""));
    }
}
