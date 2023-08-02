package de.friedrichs.kanzeige.shell;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.commands.Quit;

@ShellComponent
@Slf4j
public class CustomExitCommand implements Quit.Command {

    @Autowired
    private ApplicationContext context;

    @ShellMethod(value = "Exit the shell.", key = {"quit", "exit", "terminate"})
    public void quit() {
        log.info("Exiting the Application");
        SpringApplication.exit(context, () -> 0);
        log.info("Good Bye!!!!!!!!");
        System.exit(0);
    }
}
