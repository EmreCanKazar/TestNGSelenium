package com.enuygun.pages;

import com.enuygun.utilities.WebDriverFactory;
import com.github.javafaker.CreditCardType;
import com.github.javafaker.Faker;
import org.openqa.selenium.WebDriver;

import java.sql.Driver;

public class FirstClass {

    public  static void main(String[] args){
        System.out.println("Hello");

        Faker faker=new Faker();
        System.out.println(faker.name().fullName());
        System.out.println(faker.harryPotter().character());
        System.out.println(faker.finance().creditCard(CreditCardType.MASTERCARD));

        WebDriver driver= WebDriverFactory.getDriver("chrome");
        driver.get("https://www.google.com");

        String title= driver.getTitle();
        System.out.println("title= " + title);
    }
}
