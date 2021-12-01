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

public enum TshirtSize implements EstimationGrade {
    XS(1, "XS", false),
    S(2, "S", false),
    M(3, "M", false),
    L(4, "L", false),
    XL(5, "XL", false),
    XXL(6, "XXL", false),
    XXXL(7, "XXXL", false),
    COFFEE(8, "Coffee", false),
    QUESTION(9, "?", false);

    private final int id;
    private final String value;
    private final boolean applicable;

    private static final Map<Integer, TshirtSize> ID_TO_INSTANCE_MAP = Stream.of(TshirtSize.values())
                                                                             .collect(toMap(
                                                                                     TshirtSize::getId,
                                                                                     Function.identity()));

    TshirtSize(int id, String value, boolean applicable) {
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
        return Stream.of(TshirtSize.values()).collect(toList());
    }
}
