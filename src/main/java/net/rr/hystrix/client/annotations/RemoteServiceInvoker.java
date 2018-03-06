package net.rr.hystrix.client.annotations;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class RemoteServiceInvoker {

  @Autowired
  private RestTemplate restTemplate;

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  public String callService(Long timeout, Double errors, Double input) {
    return restTemplate
        .getForObject(
            "http://localhost:8080/remote?timeout=" + timeout + "&errors=" + errors + "&input="
                + input,
            String.class);
  }

  @HystrixCommand(fallbackMethod = "handleBadService", commandProperties = {
      @HystrixProperty(name = "execution.isolation.thread.interruptOnTimeout", value = "true")
  })
  public String callServiceHystrix(Long timeout, Double errors, Double input) {
    return restTemplate
        .getForObject(
            "http://localhost:8080/remote?timeout=" + timeout + "&errors=" + errors + "&input="
                + input,
            String.class);
  }

  private String handleBadService(Long timeout, Double errors, Double input) {
    System.out.println("From fallback...");
    return "-1";
  }
}
