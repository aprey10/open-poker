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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public enum EstimationScale {
    CLASSIC_PLANNING("Planning Cards", EstimationUnit.CLASSIC_PLANNING),
    FIBONACCI("Fibonacci", EstimationUnit.FIBONACCI),
    LINEAR("Linear", EstimationUnit.LINEAR),
    T_SHIRT_SIZE("T-shirt size", EstimationUnit.T_SHIRT_SIZE);

    private final String name;
    private EstimationUnit estimationUnit;

    EstimationScale(String name, EstimationUnit estimationUnit) {
        this.name = name;
        this.estimationUnit = estimationUnit;
    }

    public static Optional<EstimationUnit> findByName(String name) {
        return Arrays.stream(values())
                     .filter(e -> e.name.equals(name))
                     .map(EstimationScale::getEstimationUnit)
                     .findFirst();
    }

    public static Optional<EstimationScale> findByUnit(EstimationUnit unit) {
        return Arrays.stream(values())
                     .filter(e -> e.estimationUnit.equals(unit))
                     .findFirst();
    }

    public static List<EstimationScale> getValues() {
        return Arrays.stream(values()).collect(Collectors.toList());
    }
}
