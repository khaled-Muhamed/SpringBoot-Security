package com.spring.SpringBootSecurity.security;
import java.util.HashSet;
import java.util.Set;

import static com.spring.SpringBootSecurity.security.ApplicationUserPermission.*;

public enum ApplicationUserRole {
    STUDENT(new HashSet<>()),
    ADMIN(new HashSet<ApplicationUserPermission>(){{
        add(COURSE_READ);
        add(COURSE_WRITE);
        add(STUDENT_READ);
        add(STUDENT_WRITE);
    }});

    private final Set<ApplicationUserPermission> permissions;

    ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<ApplicationUserPermission> getPermissions() {
        return permissions;
    }
}
