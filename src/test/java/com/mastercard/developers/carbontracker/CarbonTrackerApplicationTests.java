package com.mastercard.developers.carbontracker;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class CarbonTrackerApplicationTests {

  public static final boolean loading = true;

  @Test
  void contextLoads() {
    log.info("Loading context");
    Assertions.assertTrue(loading);
  }

}
