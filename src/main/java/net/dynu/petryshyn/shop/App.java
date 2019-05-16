package net.dynu.petryshyn.shop;

import com.budhash.cliche.Shell;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.logging.Level;

public class App{

    public static void main( String[] args ) throws IOException {
        //Turning off hibernate logging
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
        //Initializing application context
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:context/ShellContext.xml");
        //Starting shell
        context.getBean("shell", Shell.class).commandLoop();
    }
}
