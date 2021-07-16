package com.spring.SpringBootSecurity.security;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.spring.SpringBootSecurity.security.ApplicationUserPermission.*;

public enum ApplicationUserRole {
    STUDENT(new HashSet<>()),
    ADMIN(new HashSet<ApplicationUserPermission>(){{
        add(COURSE_READ);
        add(COURSE_WRITE);
        add(STUDENT_READ);
        add(STUDENT_WRITE);
    }}),
    ADMINTRAINEE(new HashSet<ApplicationUserPermission>(){{
        add(COURSE_READ);
        add(STUDENT_READ);
    }});

    private final Set<ApplicationUserPermission> permissions;

    ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<ApplicationUserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorirties(){
        //this method is used to build set<SimpleGrantedAuthority> for each Role

        //1-build the required set
        Set<SimpleGrantedAuthority> authorities =
                permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        //2-Add ROLE_ + role Name
        authorities.add(new SimpleGrantedAuthority("ROLE_"+this.name()));
        //return authorities

        //kda ent k2nk 3mlt .roles() bta3t el USER bnfsk
        return authorities;
    }
}
