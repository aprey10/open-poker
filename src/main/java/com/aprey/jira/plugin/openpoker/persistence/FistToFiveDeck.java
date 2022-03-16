/*
 * Copyright (C) 2022  Public Domain
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
import com.aprey.jira.plugin.openpoker.Deck;
import java.util.List;

public class FistToFiveDeck implements Deck {

    @Override
    public List<EstimationGrade> getGrades() {
        return FistToFive.getValuesList();
    }

    @Override
    public EstimationGrade getGrade(int gradeId) {
        return FistToFive.findById(gradeId);
    }
}
