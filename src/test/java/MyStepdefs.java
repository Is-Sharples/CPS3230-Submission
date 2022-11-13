import Main.CustomAlert;
import io.cucumber.java.bs.A;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class MyStepdefs {

    CustomAlert alert;
    List<WebElement> alertListFromWebsite;
    @Given("I am a user of marketalertum")
    public void iAmAUserOfMarketalertum() {
        alert = new CustomAlert();
    }

    @When("I login using {string}")
    public void iLoginUsing(String arg0) {
        alert.LoginToWebsite(arg0);
    }

    @Then("I should see my alerts")
    public void iShouldSeeMyAlerts() {
        Assertions.assertEquals(true,alert.login);
    }

    @Then("I should see the login screen again")
    public void iShouldSeeTheLoginScreenAgain() {
        Assertions.assertEquals(false, alert.login);
    }

    @Given("I am an administrator of the website and I upload {int} alerts")
    public void iAmAnAdministratorOfTheWebsiteAndIUploadAlerts(int arg0) {
        //alert = new CustomAlert();
        alert.SendDeleteRequest("00de4d33-5d10-4151-ad8f-39dca960ddce");
        alert.CreatePostRequest(alert.ScrapeEbayForItems(arg0));

    }

    @When("I view a list of alerts")
    public void iViewAListOfAlerts() {
        alertListFromWebsite = alert.CheckingAlertLayout();

    }

    @Then("each alert should contain an icon")
    public void eachAlertShouldContainAnIcon() {
        for (WebElement item: alertListFromWebsite) {
            WebElement temp = item.findElement(By.tagName("h4"));
            temp = temp.findElement(By.tagName("img"));
            Assertions.assertEquals(true,temp.getAttribute("src").contains(".png"));
        }
    }

    @And("each alert should contain a heading")
    public void eachAlertShouldContainAHeading() {
        for (WebElement item: alertListFromWebsite) {
            WebElement temp = item.findElement(By.tagName("h4"));
            Assertions.assertEquals(true,temp.getText().length() > 0);
        }
    }

    @And("each alert should contain a description")
    public void eachAlertShouldContainADescription() {
        for (WebElement item : alertListFromWebsite) {
            WebElement temp = item.findElement(By.xpath("//tbody/tr[3]"));
            Assertions.assertEquals(true, temp.getText().length() > 0);
        }
    }

    @And("each alert should contain an image")
    public void eachAlertShouldContainAnImage() {
        for (WebElement item: alertListFromWebsite ) {
            WebElement temp = item.findElement(By.xpath("//tbody/tr[2]/td/img"));
            Assertions.assertEquals(true,temp.getAttribute("src").contains("ebayimg"));
        }
    }

    @And("each alert should contain a price")
    public void eachAlertShouldContainAPrice() {
        for(WebElement item: alertListFromWebsite){
            WebElement temp = item.findElement(By.xpath("//tbody/tr[4]"));
            Assertions.assertEquals(true,temp.getText().length() > 0);
        }
    }

    @And("each alert should contain a link to the original product website")
    public void eachAlertShouldContainALinkToTheOriginalProductWebsite() {
        for(WebElement item : alertListFromWebsite){
            WebElement temp = item.findElement(By.xpath("//tbody/tr[5]"));
            Assertions.assertEquals(true,temp.findElement(By.tagName("a")).getAttribute("href").contains("www.ebay.com"));
        }
        alert.DestroyDriver();
    }


    @Then("I should see {int} alerts")
    public void iShouldSeeAlerts(int arg0) {
        Assertions.assertEquals(arg0,alertListFromWebsite.size());

    }


    @Then("I should see {int} alert")
    public void iShouldSeeAlert(int arg0) {
        alertListFromWebsite = alert.CheckingAlertLayout();
        Assertions.assertEquals(arg0,alertListFromWebsite.size());

    }

    @Given("I am an administrator of the website and I upload more than {int} alerts")
    public void iAmAnAdministratorOfTheWebsiteAndIUploadMoreThanAlerts(int arg0) {

        alert.SendDeleteRequest("00de4d33-5d10-4151-ad8f-39dca960ddce");
        alert.CreatePostRequest(alert.ScrapeEbayForItems(arg0 + 1));

    }

    @Given("I am an administrator of the website and I upload an alert of type {string}")
    public void iAmAnAdministratorOfTheWebsiteAndIUploadAnAlertOfType(String arg0) {
        alert.SendDeleteRequest("00de4d33-5d10-4151-ad8f-39dca960ddce");
        ArrayList<CustomAlert> temp =new ArrayList<>();
        temp.add(new CustomAlert("url","ItemId","heading","desc","imageUrl","00de4d33-5d10-4151-ad8f-39dca960ddce",1234,"2022-11-07T11:42:09.2046689Z",Integer.parseInt(arg0)));
        alert.CreatePostRequest(temp);
    }


    @And("the icon displayed should be {string}")
    public void theIconDisplayedShouldBe(String arg0) {
        WebElement temp = alertListFromWebsite.get(0);
        String src = temp.findElement(By.xpath("//tbody/tr[1]/td/h4/img")).getAttribute("src");
        Assertions.assertEquals(true,src.contains(arg0));
        alert.DestroyDriver();
    }
}
