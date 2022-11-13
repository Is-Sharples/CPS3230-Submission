Feature: ebay Scraping

  Scenario: Valid Login

    Given I am a user of marketalertum
    When I login using "00de4d33-5d10-4151-ad8f-39dca960ddce"
    Then I should see my alerts

  Scenario: Invalid Login
    
    Given I am a user of marketalertum
    When I login using "7ca5f131-0ff0-42cd-85e8-cae25a4ee41f"
    Then I should see the login screen again


  Scenario: Alert Layout
    Given I am a user of marketalertum
    Given I am an administrator of the website and I upload 3 alerts
    When I view a list of alerts
    Then each alert should contain an icon
    And each alert should contain a heading
    And  each alert should contain a description
    And each alert should contain an image
    And  each alert should contain a price
    And each alert should contain a link to the original product website

  Scenario: Alert Limit
    Given I am a user of marketalertum
    Given I am an administrator of the website and I upload more than 5 alerts
    When I view a list of alerts
    Then I should see 5 alerts


  Scenario Outline:  Icon Check
    Given I am a user of marketalertum
    Given I am an administrator of the website and I upload an alert of type "<alert_type>"
    When I view a list of alerts
    Then I should see 1 alerts
    And the icon displayed should be "<icon_file_name>"

    Examples:
    |alert_type|icon_file_name|
    |1         |icon-car.png |
    |2         |icon-boat.png |
    |3         |icon-property-rent.jpg|
    |4         |icon-property-sale.jpg|
    |5         |icon-toys.png         |
    |6         |icon-electronics.png  |

