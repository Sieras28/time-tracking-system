package time.tracking.system.init;

import time.tracking.system.model.User;
import time.tracking.system.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@example.com");
            admin.setRole("ADMIN");
            admin.setHourlyRate(0.0);
            admin.setName("System");
            admin.setSurname("Administrator");
            admin.setStatus("ACTIVE");

            User worker = new User();
            worker.setUsername("worker1");
            worker.setPassword(passwordEncoder.encode("work123"));
            worker.setEmail("worker1@example.com");
            worker.setRole("WORKER");
            worker.setHourlyRate(15.5);
            worker.setName("John");
            worker.setSurname("Doe");
            worker.setStatus("ACTIVE");

            User accountant = new User();
            accountant.setUsername("accountant");
            accountant.setPassword(passwordEncoder.encode("acc123"));
            accountant.setEmail("accountant@example.com");
            accountant.setRole("ACCOUNTANT");
            accountant.setHourlyRate(0.0);
            accountant.setName("Alice");
            accountant.setSurname("Smith");
            accountant.setStatus("ACTIVE");

            userRepository.save(admin);
            userRepository.save(worker);
            userRepository.save(accountant);
        }
    }
}
