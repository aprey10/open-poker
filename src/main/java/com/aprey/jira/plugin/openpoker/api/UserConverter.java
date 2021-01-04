package com.aprey.jira.plugin.openpoker.api;

import com.atlassian.jira.avatar.Avatar;
import com.atlassian.jira.avatar.AvatarService;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import javax.inject.Inject;
import javax.inject.Named;

@Scanned
@Named
public class UserConverter {

    @ComponentImport
    private final AvatarService avatarService;

    @Inject
    public UserConverter(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    public UserDTO buildUserDto(ApplicationUser targetUser, ApplicationUser currentUser) {
        UserDTO dto = new UserDTO();
        dto.setUsername(targetUser.getUsername());
        dto.setDisplayName(targetUser.getDisplayName());
        dto.setAvatarUrl(avatarService.getAvatarURL(currentUser, targetUser, Avatar.Size.SMALL).toString());

        return dto;
    }
}
