package net.dynu.petryshyn.shop.shell;

import com.budhash.cliche.Shell;
import com.budhash.cliche.ShellFactory;
import org.springframework.beans.factory.FactoryBean;

public class ShellFactoryBean implements FactoryBean<Shell> {
    private String pathElement;
    private String hint;
    private Object[] handlers;

    public ShellFactoryBean(String pathElement, String hint, Object[] handlers) {
        this.pathElement = pathElement;
        this.hint = hint;
        this.handlers = handlers;
    }

    @Override
    public Shell getObject() throws Exception {
        return ShellFactory.createConsoleShell(pathElement, hint, handlers);
    }

    @Override
    public Class<?> getObjectType() {
        return Shell.class;
    }
}
