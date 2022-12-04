package jpastudy.jpashop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

@SpringBootApplication
public class JpaProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpaProjectApplication.class, args);
	}

	@Bean
	Hibernate5Module hibernate5Module() {
		Hibernate5Module hibernate5Module = new Hibernate5Module();
		//hibernate5Module.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, true);
		return hibernate5Module;
	}

}
