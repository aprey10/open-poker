package com.aprey.jira.plugin.openpoker.api;

import com.aprey.jira.plugin.openpoker.EstimationGrade;
import com.aprey.jira.plugin.openpoker.PokerSession;
import java.util.List;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class SessionViewDTO {
    private final PokerSession session;
    private final List<EstimationGrade> estimationGrades;
    private final boolean alreadyVoted;
}
