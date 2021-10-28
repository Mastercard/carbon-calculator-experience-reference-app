package com.mastercard.developers.carbontracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.mastercard.developers.carbontracker"})
public class CarbonTrackerApplication {

  public static void main(String[] args) {
    SpringApplication.run(CarbonTrackerApplication.class, args);
  }

}
