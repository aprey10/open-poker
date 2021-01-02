package com.aprey.jira.plugin.openpoker.persistence;

import com.aprey.jira.plugin.openpoker.Estimate;
import com.aprey.jira.plugin.openpoker.FibonacciNumber;
import com.aprey.jira.plugin.openpoker.PokerSession;
import com.aprey.jira.plugin.openpoker.User;
import com.aprey.jira.plugin.openpoker.UserNotFoundException;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.util.UserManager;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@Scanned
class EntityToObjConverter {

    @ComponentImport
    private final UserManager userManager;

    @Inject
    public EntityToObjConverter(UserManager userManager) {
        this.userManager = userManager;
    }

    public Estimate toObj(EstimateEntity entity) {
        return Estimate.builder()
                       .estimator(buildUser(entity.getEstimatorId()))
                       .grade(FibonacciNumber.findById(entity.getGradeId()).getValue())
                       .build();
    }

    public PokerSession toObj(PokerSessionEntity entity) {
        return PokerSession.builder()
                           .status(entity.getSessionStatus())
                           .issueId(entity.getIssueId())
                           .moderator(buildUser(entity.getModeratorId()))
                           .completionDate(entity.getCompletionDate())
                           .estimates(buildEstimates(entity.getEstimates()))
                           .build();
    }

    private List<Estimate> buildEstimates(EstimateEntity[] entities) {
        return Arrays.stream(entities).map(this::toObj).collect(Collectors.toList());
    }

    private User buildUser(long userId) {
        ApplicationUser applicationUser = userManager.getUserById(userId).orElseThrow(() -> new UserNotFoundException(
                "User with id " + userId + " is not found"));

        return User.builder()
                   .username(applicationUser.getUsername())
                   .fullName(applicationUser.getDisplayName())
                   .id(applicationUser.getId())
                   .build();
    }
}
