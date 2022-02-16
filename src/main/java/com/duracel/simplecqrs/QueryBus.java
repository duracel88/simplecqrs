package com.duracel.simplecqrs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class QueryBus {
    private final Map<Class<Query>, QueryHandler> queryHandlerMap;

    public QueryBus(List<QueryHandler> commandHandlers) {
        this.queryHandlerMap = commandHandlers.stream()
                .collect(Collectors.toConcurrentMap(e -> e.getQueryType(), e -> e));
        log.info("Registered query handlers: {}", this.queryHandlerMap);
    }

    public <R> R query(Query<R> query) {
        return Optional.ofNullable(queryHandlerMap.get(query.getClass()))
                .map((handler) -> {
                    log.info(String.format("%s triggered with query %s", handler.getClass().getSimpleName(), query));
                    return (R) handler.query(query);
                }).orElseThrow(() -> {
                    throw new HandlerNotFoundException("Handler not found, queryType=" + query.getClass());
                });
    }
}
