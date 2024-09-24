package infnet.gontijo.transporte_petfriends;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class TransportePetFriendsApplication implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(TransportePetFriendsApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(TransportePetFriendsApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
    }
}