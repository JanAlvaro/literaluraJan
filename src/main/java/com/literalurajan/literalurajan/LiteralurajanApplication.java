package com.literalurajan.literalurajan;

import com.literalurajan.literalurajan.repository.AutorRepository;
import com.literalurajan.literalurajan.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteralurajanApplication implements CommandLineRunner {

	@Autowired
	private LibroRepository libroRepository;
	@Autowired
	private AutorRepository autorRepository;

	public static void main(String[] args) {
		SpringApplication.run(LiteralurajanApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal menu = new Principal (libroRepository, autorRepository);
		menu.showMenu();
	}

}
