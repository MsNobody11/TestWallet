package org.example.testwallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;


@SpringBootApplication
@EnableRetry
public class TestWalletApplication {

  public static void main(String[] args) {
    SpringApplication.run(TestWalletApplication.class, args);
  }

}
