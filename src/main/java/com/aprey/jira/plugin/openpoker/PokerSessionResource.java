package com.aprey.jira.plugin.openpoker;

import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.util.UserManager;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/session/")
@Scanned
public class PokerSessionResource {

    private final PokerSessionService sessionService;
    @ComponentImport
    private final UserManager userManager;

    @Inject
    public PokerSessionResource(PokerSessionService sessionService, UserManager userManager) {
        this.sessionService = sessionService;
        this.userManager = userManager;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@Context HttpServletRequest request) {
        final String issueId = request.getParameter("issueId");
        Optional<PokerSession> activeSessionOpt = sessionService.getActiveSession(issueId);
        if (!activeSessionOpt.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        PokerSession activeSession = activeSessionOpt.get();

        SessionPingStatusDTO sessionPingStatusDTO = new SessionPingStatusDTO();
        sessionPingStatusDTO.setStatus(activeSession.getSessionStatus());
        sessionPingStatusDTO.setEstimators(Arrays.stream(activeSession.getEstimates())
                                                 .map(this::buildDTO)
                                                 .collect(Collectors.toList()));

        return Response.ok(sessionPingStatusDTO).build();
    }

    private EstimatorDTO buildDTO(Estimate estimate) {
        EstimatorDTO dto = new EstimatorDTO();
        ApplicationUser user = getUser(estimate.getEstimatorId());
        dto.setUsername(user.getUsername());
        dto.setDisplayName(user.getDisplayName());

        return dto;
    }

    private ApplicationUser getUser(Long userId) {
        return userManager.getUserById(userId).orElseThrow(() -> new UserNotFoundException(
                "User with id " + userId + " is not found"));
    }
}
