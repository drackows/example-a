package pl.waw.inpar.examples.Example.A;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
@RestController
public class ExampleAApplication {

    @Value("${my.property}")
    String myProperty;

    public static void main(String[] args) {
        SpringApplication.run(ExampleAApplication.class, args);
    }

    @GetMapping("/hi")
    public String hi() {
        return String.format("hi! (hostname: %s) [my property: %s]", hostname(), myProperty);
    }

    private String hostname() {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            return addr.getHostName();
        } catch (UnknownHostException ex) {
            return "---unknown---";
        }
    }

}
