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
