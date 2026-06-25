package ads.lll.academia;

import ads.lll.academia.views.Menu;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SistemaAcademiaApplication implements CommandLineRunner {

	private final Menu menu;

	public SistemaAcademiaApplication(Menu menu) {
		this.menu = menu;
	}

	public static void main(String[] args) {
		SpringApplication.run(SistemaAcademiaApplication.class, args);
	}

	@Override
	public void run(String... args) {
		menu.run();
	}
}