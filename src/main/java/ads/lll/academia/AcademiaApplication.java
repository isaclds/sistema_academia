package ads.lll.academia;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjetoApplication implements CommandLineRunner {

	private final Menu menu;

	public ProjetoApplication(Menu menu) {
		this.menu = menu;
	}

	static void main(String[] args) {
		SpringApplication.run(ProjetoApplication.class, args);
	}

	@Override
	public void run(String... args) {
		menu.run();
	}
}