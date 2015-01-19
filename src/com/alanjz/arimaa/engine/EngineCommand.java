package com.alanjz.arimaa.engine;

import java.util.Arrays;
import java.util.function.Consumer;

public class EngineCommand {
    private final String name;
    private final int numParams;
    private final int optionalParams;
    private final Consumer<String[]> consumer;

    public EngineCommand(String name, int numParams, int optionalParams, Consumer<String[]> consumer) {
        this.name = name;
        this.numParams = numParams;
        this.optionalParams = optionalParams;
        this.consumer = consumer;
    }

    public EngineCommand(String name, int numParams, Consumer<String[]> consumer) {
        this(name, numParams, 0, consumer);
    }

    public String getName() {
        return name;
    }

    public int numParams() {
        return numParams;
    }

    public int optionalParams() {
        return optionalParams;
    }

    public boolean processCommand(String[] command) {
        if(command.length > 0 && command[0].equals(getName())) {
            if(command.length >= numParams() + 1 && command.length <= numParams() + optionalParams() + 1) {
                consumer.accept(command);
            }
            else {
                AEIEngine.log(AEIEngine.LogLevel.ERROR,
                        "Call to `" + getName() + "' expects " + numParams() +
                                (optionalParams() > 0? "(+" + optionalParams() + ")" : "") +
                                " arguments got " + (command.length-1) + ".");
            }
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[] {
                getName(), numParams(), optionalParams()
        });
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof EngineCommand)) {
            return false;
        }
        EngineCommand e = (EngineCommand) o;
        return e.getName().equals(getName());
    }
}
