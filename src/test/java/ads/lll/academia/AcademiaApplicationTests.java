package ads.lll.academia;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class AcademiaApplicationTests {

	@Test
	@Disabled("Requer banco de dados configurado")
	void contextLoads() {
	}
}
