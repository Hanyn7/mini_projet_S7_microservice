package com.hanin.users.service;
import com.hanin.users.entities.Role;
import com.hanin.users.entities.User;


public interface UserService {
	User saveUser(User user);
	User findUserByUsername (String username);
	Role addRole(Role role);
	User addRoleToUser(String username, String rolename);
}
