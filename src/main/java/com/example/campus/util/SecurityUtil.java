package com.example.campus.util;

import com.example.campus.entity.Permission;
import com.example.campus.exception.UserNotFoundException;
import com.example.campus.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SecurityUtil {

    private static UserService userService;

    @Autowired
    public SecurityUtil(UserService userService) {
        SecurityUtil.userService = userService;
    }

    public static String getUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }

    public static boolean isAuthorized(String requester, List<String> requiredPermissions) throws UserNotFoundException {
        List<Permission> userPermissions = userService.getPermissionsByUsername(requester);
        Set<String> userPermissionNames = userPermissions.stream()
                .map(Permission::getName)
                .collect(Collectors.toSet());

        return userPermissionNames.containsAll(requiredPermissions);
    }
}
