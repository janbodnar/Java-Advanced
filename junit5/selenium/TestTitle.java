package com.zetcode.web;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

class TestTitle {

    private static WebDriver driver;

    @BeforeAll
    static void setUp() {

        var opt = new FirefoxOptions();
        opt.setHeadless(true);

        driver = new FirefoxDriver(opt);
    }

    @Test
    public void testTitle() {

        driver.get("http://webcode.me");

        System.out.println("Page title is: " + driver.getTitle());
        Assertions.assertEquals("My html page", driver.getTitle());
    }

    @AfterAll
    static void tearDown() {

        if (driver != null) {
            driver.quit();
        }
    }
}
