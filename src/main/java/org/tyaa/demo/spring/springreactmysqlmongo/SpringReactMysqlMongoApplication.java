package org.tyaa.demo.spring.springreactmysqlmongo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.tyaa.demo.spring.springreactmysqlmongo.entities.Category;
import org.tyaa.demo.spring.springreactmysqlmongo.entities.Product;
import org.tyaa.demo.spring.springreactmysqlmongo.entities.Role;
import org.tyaa.demo.spring.springreactmysqlmongo.entities.User;
import org.tyaa.demo.spring.springreactmysqlmongo.repositories.CategoryHibernateDAO;
import org.tyaa.demo.spring.springreactmysqlmongo.repositories.ProductHibernateDAO;
import org.tyaa.demo.spring.springreactmysqlmongo.repositories.RoleRepository;
import org.tyaa.demo.spring.springreactmysqlmongo.repositories.UserRepository;

import java.math.BigDecimal;

@SpringBootApplication
public class SpringReactMysqlMongoApplication {

	@Value("${tests.unit.strings.image-base64-msft}")
	private String msftImageString;

	@Value("${tests.unit.strings.image-base64-orcl}")
	private String orclImageString;

	@Value("${tests.unit.strings.image-base64-eth}")
	private String ethImageString;

	public static void main(String[] args) {
		SpringApplication.run(SpringReactMysqlMongoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(
			RoleRepository roleRepository,
			UserRepository userRepository,
			PasswordEncoder passwordEncoder,
			CategoryHibernateDAO categoryDAO,
			ProductHibernateDAO productDAO
	) {
		return args -> {
			roleRepository.save(Role.builder().name("ROLE_ADMIN").build());
			roleRepository.save(Role.builder().name("ROLE_USER").build());
			Role adminRole = roleRepository.findRoleByName("ROLE_ADMIN");
			Role userRole = roleRepository.findRoleByName("ROLE_USER");
			userRepository.save(
					User.builder()
							.name("admin")
							.password(passwordEncoder.encode("AdminPassword1"))
							.role(adminRole)
							.build()
			);
			userRepository.save(
					User.builder()
							.name("one")
							.password(passwordEncoder.encode("UserPassword1"))
							.role(userRole)
							.build()
			);
			userRepository.save(
					User.builder()
							.name("two")
							.password(passwordEncoder.encode("UserPassword2"))
							.role(userRole)
							.build()
			);
			userRepository.save(
					User.builder()
							.name("three")
							.password(passwordEncoder.encode("UserPassword3"))
							.role(userRole)
							.build()
			);
			Category stockCategory = Category.builder().name("stock").build();
			Category cryptoCategory = Category.builder().name("crypto").build();
			Category eMoneyCategory = Category.builder().name("e-money").build();
			categoryDAO.save(stockCategory);
			categoryDAO.save(cryptoCategory);
			categoryDAO.save(eMoneyCategory);
			Product stockMSFTProduct =
					Product.builder()
							.name("MSFT")
							.description("Microsoft Stock")
							.price(new BigDecimal(203.92))
							.quantity(1000)
							.category(stockCategory)
							.image(msftImageString)
							.build();
			Product stockORCLProduct =
					Product.builder()
							.name("ORCL")
							.description("Oracle Stock")
							.price(new BigDecimal(55.82))
							.quantity(2000)
							.category(stockCategory)
							.image(orclImageString)
							.build();
			Product stockORCLProduct2 =
					Product.builder()
							.name("ORCL")
							.description("Oracle Stock")
							.price(new BigDecimal(56.12))
							.quantity(1000)
							.category(stockCategory)
							.image(orclImageString)
							.build();
			Product cryptoEthereumProduct =
					Product.builder()
							.name("ETH")
							.description("Ethereum Cryptocurrency")
							.price(new BigDecimal(232.48))
							.quantity(500)
							.category(cryptoCategory)
							.image(ethImageString)
							.build();
			productDAO.save(stockMSFTProduct);
			productDAO.save(stockORCLProduct);
			productDAO.save(stockORCLProduct2);
			productDAO.save(cryptoEthereumProduct);
		};
	}
}
