/*
 * Copyright (C) 2021  Andriy Preizner
 *
 * This file is a part of Open Poker jira plugin
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.aprey.jira.plugin.openpoker;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public enum FibonacciNumber implements EstimationGrade {
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

    private static final Map<Integer, FibonacciNumber> ID_TO_INSTANCE_MAP = Stream.of(FibonacciNumber.values())
                                                                                  .collect(toMap(
                                                                                          FibonacciNumber::getId,
                                                                                          Function.identity())
                                                                                           );

    FibonacciNumber(int id, String value) {
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

    public static EstimationGrade findById(int id) {
        return ID_TO_INSTANCE_MAP.get(id);
    }

    public static List<EstimationGrade> getValuesList() {
        return Stream.of(FibonacciNumber.values()).collect(toList());
    }
}
