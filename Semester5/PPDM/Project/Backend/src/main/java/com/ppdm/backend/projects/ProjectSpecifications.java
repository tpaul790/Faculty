package com.ppdm.backend.projects;

import com.ppdm.backend.user.UserEntity;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Join;

public class ProjectSpecifications {

    public static Specification<ProjectEntity> isMember(Long userId) {
        return (root, query, criteriaBuilder) -> {
            Join<ProjectEntity, UserEntity> membersJoin = root.join("members");

            return criteriaBuilder.equal(membersJoin.get("id"), userId);
        };
    }

    public static Specification<ProjectEntity> isActive() {
        return(root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("active"), true);
    }

    public static Specification<ProjectEntity> inName(final String text) {
        return (root, query, criteriaBuilder) -> {

            jakarta.persistence.criteria.Expression<String> lowerName = criteriaBuilder.lower(root.get("name"));
            String lowerText = text.toLowerCase();

            return criteriaBuilder.like(lowerName, "%" + lowerText + "%");
        };
    }
}
