package com.hanin.users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.hanin.users.entities.Role;
import com.hanin.users.entities.User;
import com.hanin.users.repos.RoleRepository;
import com.hanin.users.repos.UserRepository;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
	 @Autowired
	    UserRepository userRep;
	    @Autowired
	    RoleRepository roleRep;
	    @Autowired
	    BCryptPasswordEncoder bCryptPasswordEncoder;
	    @Override
	    public User saveUser(User user) {
	        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
	        return userRep.save(user);
	    }

	    @Override
	    public List<User> findAllUsers() {
	        return userRep.findAll();
	    }

	    @Override
	    public void deleteUser(long id) {
	        userRep.deleteById(id);
	    }

	    @Override
	    public User removeRoleFromUser(long id, Role r) {
	        User user=userRep.findUserById(id);
	        List<Role> listOfrole=user.getRoles();

	        listOfrole.remove(r);
	        userRep.save(user);
	        return user;
	    }

	    @Override
	    public List<Role> findAllRoles() {
	        return roleRep.findAll();
	    }

	    @Override
	    public Role findRoleById(Long id) {
	        return roleRep.findRoleById(id);
	    }

	    @Override
	    public User findUserById(Long id) {
	        return userRep.findById(id).get();
	    }

	    @Override
	    public Role addRole(Role role) {
	        return roleRep.save(role);
	    }

	    @Override
	    public User addRoleToUser(long id, Role r) {
	        User usr = userRep.findUserById(id);

	        List<Role> roles = usr.getRoles();
	        roles.add(r);

	        usr.setRoles(roles);

	        return userRep.save(usr);
	    }

	    @Override
	    public User findUserByUsername(String username) {
	        return userRep.findByUsername(username);
	    }

}




















