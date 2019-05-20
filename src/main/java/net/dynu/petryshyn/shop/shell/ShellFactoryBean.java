package net.dynu.petryshyn.shop.shell;

import com.budhash.cliche.InputConverter;
import com.budhash.cliche.Shell;
import com.budhash.cliche.ShellFactory;
import org.springframework.beans.factory.FactoryBean;

public class ShellFactoryBean implements FactoryBean<Shell> {
    private String pathElement;
    private String hint;
    private Object[] handlers;
    private InputConverter[] inputConverters;

    public ShellFactoryBean(String pathElement, String hint, Object[] handlers, InputConverter[] inputConverters) {
        this.pathElement = pathElement;
        this.hint = hint;
        this.handlers = handlers;
        this.inputConverters = inputConverters;
    }

    @Override
    public Shell getObject() throws Exception {
        Shell shell = ShellFactory.createConsoleShell(pathElement, hint, handlers);
        for(InputConverter converter : inputConverters){
            shell.getInputConverter().addConverter(converter);
        }
        return shell;
    }

    @Override
    public Class<?> getObjectType() {
        return Shell.class;
    }
}
