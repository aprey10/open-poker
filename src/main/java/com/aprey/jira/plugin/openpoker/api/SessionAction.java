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

package com.aprey.jira.plugin.openpoker.api;

import java.util.Optional;
import java.util.stream.Stream;

public enum SessionAction {
    START_SESSION("Start Estimation"),
    VOTE("Vote"),
    STOP_SESSION("Stop Estimation"),
    RE_ESTIMATE("Re-estimate"),
    APPLY_ESTIMATE("Apply Estimate"),
    DELETE("End Session");

    private final String actionName;

    private SessionAction(String actionName) {
        this.actionName = actionName;
    }

    public String getActionName() {
        return actionName;
    }

    public static Optional<SessionAction> getAction(String actionName) {
        return Stream.of(SessionAction.values())
                     .filter(a -> a.getActionName().equals(actionName))
                     .findAny();
    }
}
