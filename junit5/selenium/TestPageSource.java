package com.zetcode.web;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

class TestPageSource {

    private static WebDriver driver;

    @BeforeAll
    static void setUp() {

        var opt = new FirefoxOptions();
        opt.setHeadless(true);

        driver = new FirefoxDriver(opt);
    }

    @Test
    public void testPageContent() {

        driver.get("http://webcode.me");

        System.out.println(driver.getPageSource());
        Assertions.assertTrue(driver.getPageSource().contains("Today is a beautiful day"));
    }

    @AfterAll
    static void tearDown() {

        if (driver != null) {
            driver.quit();
        }
    }
}
