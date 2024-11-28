package com.example.campus;

import com.example.campus.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestConfig.class)
class CampusApplicationTests {

	@Test
	void contextLoads() {
	}

}
