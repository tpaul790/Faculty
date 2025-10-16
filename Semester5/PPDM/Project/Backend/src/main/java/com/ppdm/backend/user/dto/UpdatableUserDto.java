package com.ubb.synergy.user.dto;
import com.ubb.synergy.user.UserEntity;

public interface UpdatableUserDto {
    boolean applyUpdates(UserEntity user);
}

