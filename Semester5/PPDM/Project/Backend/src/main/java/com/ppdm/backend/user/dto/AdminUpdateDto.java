package com.ubb.synergy.user.dto;

import com.ubb.synergy.user.UserEntity;
import com.ubb.synergy.user.UserRole;
import lombok.Data;

@Data
public class AdminUpdateDto extends UserUpdateDto {
    private UserRole userRole;

    public AdminUpdateDto() {}

    @Override
    public boolean applyUpdates(UserEntity user) {
        user.setUserRole(userRole);
        return super.applyUpdates(user);
    }
}

