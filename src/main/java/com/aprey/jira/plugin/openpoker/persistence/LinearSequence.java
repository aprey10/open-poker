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

package com.aprey.jira.plugin.openpoker.persistence;

import com.aprey.jira.plugin.openpoker.EstimationGrade;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public enum LinearSequence implements EstimationGrade {
    ONE(1, "1", true),
    TWO(2, "2", true),
    THREE(3, "4", true),
    FOUR(4, "5", true),
    FIVE(5, "6", true),
    SIX(6, "7", true),
    EIGHT(7, "8", true),
    NINE(8, "9", true),
    TEN(9, "10", true),
    ELEVEN(10, "11", true),
    TWELFTH(11, "12", true),
    THIRTEEN(12, "13", true);

    private final int id;
    private final String value;
    private final boolean applicable;

    private static final Map<Integer, LinearSequence> ID_TO_INSTANCE_MAP = Stream.of(LinearSequence.values())
                                                                                 .collect(toMap(
                                                                                         LinearSequence::getId,
                                                                                         Function.identity())
                                                                                         );

    LinearSequence(int id, String value, boolean applicable) {
        this.id = id;
        this.value = value;
        this.applicable = applicable;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean isApplicable() {
        return applicable;
    }

    public static EstimationGrade findById(int id) {
        return ID_TO_INSTANCE_MAP.get(id);
    }

    public static List<EstimationGrade> getValuesList() {
        return Stream.of(LinearSequence.values()).collect(toList());
    }
}
