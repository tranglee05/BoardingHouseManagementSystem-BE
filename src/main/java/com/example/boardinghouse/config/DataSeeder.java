package com.example.boardinghouse.config;

import com.example.boardinghouse.Modules.subscription.SubscriptionPackage;
import com.example.boardinghouse.Modules.subscription.SubscriptionPackageRepository;
import com.example.boardinghouse.Modules.user.user.User;
import com.example.boardinghouse.Modules.user.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final SubscriptionPackageRepository subscriptionPackageRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public DataSeeder(UserRepository userRepository, SubscriptionPackageRepository subscriptionPackageRepository) {
        this.userRepository = userRepository;
        this.subscriptionPackageRepository = subscriptionPackageRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        seedAdminUser();
        seedSubscriptionPackages();
    }

    private void seedAdminUser() {
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .fullName("System Administrator")
                    .email("admin@gmail.com")
                    .role("landlord") // Admin có thể đóng vai trò landlord cao nhất
                    .build();
            userRepository.save(admin);
            System.out.println("Seeded Admin user: admin / admin123");
        }
    }

    private void seedSubscriptionPackages() {
        if (subscriptionPackageRepository.findByName("Gói Miễn phí").isEmpty()) {
            SubscriptionPackage freePackage = SubscriptionPackage.builder()
                    .name("Gói Miễn phí")
                    .price(BigDecimal.ZERO)
                    .maxRooms(5)
                    .durationMonths(120) // 10 năm
                    .description("Gói trải nghiệm cho người mới bắt đầu (Tối đa 5 phòng).")
                    .build();
            subscriptionPackageRepository.save(freePackage);
            System.out.println("Seeded Subscription Package: Gói Miễn phí");
        }

        if (subscriptionPackageRepository.findByName("Gói Pro").isEmpty()) {
            SubscriptionPackage proPackage = SubscriptionPackage.builder()
                    .name("Gói Pro")
                    .price(new BigDecimal("199000"))
                    .maxRooms(9999)
                    .durationMonths(1)
                    .description("Gói chuyên nghiệp không giới hạn phòng (199k/tháng).")
                    .build();
            subscriptionPackageRepository.save(proPackage);
            System.out.println("Seeded Subscription Package: Gói Pro");
        }
    }
}
