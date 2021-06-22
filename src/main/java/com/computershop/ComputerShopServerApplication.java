package com.computershop;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.computershop.dao.Category;
import com.computershop.dao.Delivery;
import com.computershop.dao.User;
import com.computershop.repositories.CategoryRepository;
import com.computershop.repositories.DeliveryRepository;
import com.computershop.repositories.UserRepository;

@SpringBootApplication
//@ComponentScan(basePackages = {"com.computershop.repositories", 
//								"com.computershop.services",
//								"com.computershop.utils",
//								"com.computershop.filters",
//								"com.computershop.repositories.productRepos"})
//@EnableJpaRepositories(basePackages = {"com.computershop.repositories", "com.computershop.repositories.productRepos"})
public class ComputerShopServerApplication implements CommandLineRunner {

	@Value("${user.first_name}")
	private String firstName;

	@Value("${user.last_name}")
	private String lastName;

	@Value("${user.username}")
	private String username;

	@Value("${user.password}")
	private String password;

	@Value("${user.address}")
	private String address;

	@Value("${user.role}")
	private String role;

	@Value("${user.email}")
	private String email;

	@Value("${user.phone}")
	private String phone;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private DeliveryRepository deliveryRepository;


	@Autowired
	private CategoryRepository categoryRepository;

	@Bean
	public CharacterEncodingFilter encodingFilter() {
		return new CharacterEncodingFilter("UTF-8", true);
	}

	public static void main(String[] args) {
		SpringApplication.run(ComputerShopServerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if (userRepository.count() == 0) {
			User admin = new User(null, firstName, lastName, username, passwordEncoder.encode(password), address, null,
					phone, email, role, null, null);
			userRepository.save(admin);
			System.out.println(username + " account has been created!");
		}

		if (deliveryRepository.count() == 0) {
			Delivery delivery1 = new Delivery(null, "DaThemVaoGio", "Đã thêm vào giỏ", null, null, null);
			Delivery delivery2 = new Delivery(null, "ChoXacNhan", "Chờ xác nhận", null, null, null);
			Delivery delivery3 = new Delivery(null, "DangGiaoHang", "Đang giao hàng", null, null, null);
			Delivery delivery4 = new Delivery(null, "DaGiao", "Đã giao", null, null, null);
			Delivery delivery5 = new Delivery(null, "DaHuy", "Đã hủy", null, null, null);
			deliveryRepository.saveAll(Arrays.asList(delivery1, delivery2, delivery3, delivery4, delivery5));
		}
		
		if (categoryRepository.count() == 0) {
			Category category1 = new Category(null, null, "Case", null, null);
			Category category2 = new Category(null, null, "CPU", null, null);
			Category category3 = new Category(null, null, "Graphic card", null, null);
			Category category4 = new Category(null, null, "Hard disk", null, null);
			Category category5 = new Category(null, null, "Main board", null, null);
			Category category6 = new Category(null, null, "Monitor", null, null);
			Category category7 = new Category(null, null, "Power Suppy", null, null);
			Category category8 = new Category(null, null, "Ram", null, null);

			categoryRepository.saveAll(Arrays.asList(category1, category2, category3, category4, category5, category6,
					category7, category8));
		}

	}

}
