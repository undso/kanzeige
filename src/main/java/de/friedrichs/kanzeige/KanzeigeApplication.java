package de.friedrichs.kanzeige;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.command.annotation.CommandScan;

@SpringBootApplication
@CommandScan
public class KanzeigeApplication {

	public static void main(String[] args) {
		SpringApplication.run(KanzeigeApplication.class, args);
	}

}
