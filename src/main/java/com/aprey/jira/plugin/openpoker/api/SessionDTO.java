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

import com.aprey.jira.plugin.openpoker.SessionStatus;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SessionDTO {

    @XmlElement
    private SessionStatus status;
    @XmlElement
    private List<UserDTO> estimators;

    public SessionStatus getStatus() {
        return status;
    }

    public List<UserDTO> getEstimators() {
        return estimators;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }

    public void setEstimators(List<UserDTO> estimators) {
        this.estimators = estimators;
    }
}
