package com.aprey.jira.plugin.openpoker;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public enum FibonacciNumbers implements Estimation {
    ONE(1, "1"),
    TWO(2, "2"),
    THREE(3, "3"),
    FIVE(4, "5"),
    EIGHT(5, "8"),
    THIRTEEN(6, "13"),
    TWENTY_ONE(7, "21"),
    INFINITE(8, "Infinite"),
    COFFEE(9, "Coffee"),
    QUESTION(10, "?");

    private final int id;
    private final String value;

    private static final Map<Integer, FibonacciNumbers> ID_TO_INSTANCE_MAP = Stream.of(FibonacciNumbers.values())
                                                                                   .collect(toMap(
                                                                                           FibonacciNumbers::getId,
                                                                                           Function.identity())
                                                                                           );

    FibonacciNumbers(int id, String value) {
        this.id = id;
        this.value = value;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getValue() {
        return value;
    }

    public static Estimation findById(int id) {
        return ID_TO_INSTANCE_MAP.get(id);
    }

    public static List<Estimation> getValuesList() {
        return Stream.of(FibonacciNumbers.values()).collect(toList());
    }
}
