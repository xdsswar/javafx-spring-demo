/*
 * Copyright © 2024. XTREME SOFTWARE SOLUTIONS
 *
 * All rights reserved. Unauthorized use, reproduction, or distribution
 * of this software or any portion of it is strictly prohibited and may
 * result in severe civil and criminal penalties. This code is the sole
 * proprietary of XTREME SOFTWARE SOLUTIONS.
 *
 * Commercialization, redistribution, and use without explicit permission
 * from XTREME SOFTWARE SOLUTIONS, are expressly forbidden.
 */

package xss.it.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import xss.it.backend.entity.User;

import java.util.Optional;

/**
 * @author XDSSWAR
 * Created on 06/03/2024
 */
public interface UserRepo extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}
