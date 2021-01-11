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

import com.aprey.jira.plugin.openpoker.EstimationUnit;
import com.aprey.jira.plugin.openpoker.SessionStatus;
import net.java.ao.Entity;
import net.java.ao.OneToMany;
import net.java.ao.schema.Index;
import net.java.ao.schema.Indexes;

@Indexes(@Index(name = "issueId", methodNames = {"getIssueId"}))
public interface PokerSessionEntity extends Entity {

    String getIssueId();

    void setIssueId(String issueId);

    Long getModeratorId();

    void setModeratorId(Long moderatorId);

    SessionStatus getSessionStatus();

    void setSessionStatus(SessionStatus status);

    EstimationUnit getEstimationUnit();

    void setUnitOfMeasure(EstimationUnit estimationUnit);

    void setCompletionDate(long timestamp);

    long getCompletionDate();

    @OneToMany
    EstimateEntity[] getEstimates();
}
