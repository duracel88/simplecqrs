package com.duracel.simplecqrs;

public interface QueryHandler<R, C extends Query<R>> {
    R query(C query);
    Class<C> getQueryType();

}
