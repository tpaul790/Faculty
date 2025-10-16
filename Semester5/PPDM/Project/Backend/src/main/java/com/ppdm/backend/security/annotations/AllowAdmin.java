package com.ubb.synergy.security.annotations;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority(T(com.ubb.synergy.user.UserRole).ADMIN)")
@Target(ElementType.METHOD)
public @interface AllowAdmin {
}
