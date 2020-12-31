package com.aprey.jira.plugin.openpoker;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SessionPingStatusDTO {

    @XmlElement
    private SessionStatus status;
    @XmlElement
    private List<EstimatorDTO> estimators;

    public SessionStatus getStatus() {
        return status;
    }

    public List<EstimatorDTO> getEstimators() {
        return estimators;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }

    public void setEstimators(List<EstimatorDTO> estimators) {
        this.estimators = estimators;
    }
}
