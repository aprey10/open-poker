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

import com.aprey.jira.plugin.openpoker.Deck;
import com.aprey.jira.plugin.openpoker.EstimationUnit;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.google.common.collect.ImmutableMap;
import javax.inject.Named;

@Scanned
@Named
public class EstimationDeckService {
    private final ImmutableMap<EstimationUnit, Deck> unitToDeckMap
            = ImmutableMap.<EstimationUnit, Deck>builder()
            .put(EstimationUnit.FIBONACCI, new FibonacciDeck())
            .put(EstimationUnit.CLASSIC_PLANNING, new ClassicPlanningDeck())
            .put(EstimationUnit.T_SHIRT_SIZE, new TshirtSizeDeck())
            .put(EstimationUnit.LINEAR, new LinearDeck())
            .put(EstimationUnit.FIST_TO_FIVE, new FistToFiveDeck())
            .build();

    public Deck getDeck(EstimationUnit estimationUnit) {
        return unitToDeckMap.get(estimationUnit);
    }
}
