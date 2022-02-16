package com.duracel.simplecqrs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
public class CommandBus {
    private final Map<Class<Command>, CommandHandler> commandHandlers;

    public CommandBus(List<CommandHandler> commandHandlers) {
        this.commandHandlers = commandHandlers.stream()
                .collect(Collectors.toConcurrentMap(CommandHandler::getCommandType, e -> e));
        log.info("Registered command handlers: {}", this.commandHandlers);
    }

    public void handle(Command command) {
        Optional.ofNullable(commandHandlers.get(command.getClass()))
                .ifPresentOrElse(
                        (handler) -> {
                            log.info(String.format("%s triggered with command %s", handler.getClass().getSimpleName(), command));
                            handler.handle(command);
                        },
                        () -> {
                            throw new HandlerNotFoundException("Handler not found, commandType=" + command.getClass());
                        });
    }
}
