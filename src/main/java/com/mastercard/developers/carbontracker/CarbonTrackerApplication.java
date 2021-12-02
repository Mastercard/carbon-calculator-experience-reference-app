package com.mastercard.developers.carbontracker;

import com.mastercard.developers.carbontracker.usecases.IssuerControllerUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SuppressWarnings("squid:S3010")
@SpringBootApplication(scanBasePackages = {"com.mastercard.developers.carbontracker"})
public class CarbonTrackerApplication {

  private static IssuerControllerUseCase issuerControllerUseCase;

  @Autowired
  public CarbonTrackerApplication(IssuerControllerUseCase issuerControllerUseCaseObject) {
    issuerControllerUseCase = issuerControllerUseCaseObject;
  }

  public static void main(String[] args) {
    SpringApplication.run(CarbonTrackerApplication.class, args);
    callB2BApis();
  }

  private static void callB2BApis() {
    log.info("Calling B2B Apis for given configurations");
    issuerControllerUseCase.b2BCalls();
  }

}
