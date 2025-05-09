package kg.attractor.job_search.service;

import kg.attractor.job_search.model.Role;

public interface RoleService {
    boolean existsByRoleName(String roleName);

    Role findByRoleName(String roleName);
}
