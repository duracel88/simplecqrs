package com.duracel.simplecqrs;

public interface CommandHandler<C extends Command> {
    void handle(C command);

    Class<C> getCommandType();
}