package com.qwackly.user.enums;

import com.qwackly.user.model.UserEntity;

import java.util.Arrays;

public enum VerificationType {
    MOBILE{
        @Override
        public  boolean isVerified(UserEntity userEntity) {
            return userEntity.isPhoneVerified();
        }
    },
    EMAIL{
        @Override
        public  boolean isVerified(UserEntity userEntity) {
            return userEntity.isEmailVerified();
        }
    },
    DEFAULT{
        @Override
        public  boolean isVerified(UserEntity userEntity) {
            return userEntity.isEmailVerified() && userEntity.isPhoneVerified();
        }
    };

    public abstract boolean isVerified(UserEntity userEntity);

    public static VerificationType get(String type) {
        return Arrays.stream(VerificationType.values())
                .filter((siloSubtypeName) -> siloSubtypeName.toString().equalsIgnoreCase(type))
                .findFirst()
                .orElse(VerificationType.DEFAULT);
    }
}
