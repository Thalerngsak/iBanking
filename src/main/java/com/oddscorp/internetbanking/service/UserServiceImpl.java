package com.oddscorp.internetbanking.service;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.oddscorp.internetbanking.dao.RoleDao;
import com.oddscorp.internetbanking.dao.UserDao;
import com.oddscorp.internetbanking.domain.User;
import com.oddscorp.internetbanking.domain.security.UserRole;


@Service
@Transactional
public class UserServiceImpl implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	private UserDao userDao;

	@Autowired
    private RoleDao roleDao;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
    private AccountService accountService;
    
	@Override
	public User findByUsername(String username) {
		return userDao.findByUsername(username);
	}

	@Override
	public User findByEmail(String email) {
        return userDao.findByEmail(email);
	}

	@Override
	public boolean checkUserExists(String username, String email) {
        if (checkUsernameExists(username) || checkEmailExists(username)) {
            return true;
        } else {
            return false;
        }
	}

	@Override
	public boolean checkUsernameExists(String username) {
        if (null != findByUsername(username)) {
            return true;
        }

        return false;
	}

	@Override
	public boolean checkEmailExists(String email) {
        if (null != findByEmail(email)) {
            return true;
        }

        return false;
	}

	@Override
	public void save(User user) {
        userDao.save(user);
	}

	@Override
	public User createUser(User user, Set<UserRole> userRolse) {
		User localUser = userDao.findByUsername(user.getUsername());
		if(localUser != null) {
			logger.info("User with username {} already exist. Nothing will be done. ",user.getUsername());
		}else {
			String encryptedPassword = passwordEncoder.encode(user.getPassword());
			user.setPassword(encryptedPassword);
			
			for(UserRole ur : userRolse) {
				roleDao.save(ur.getRole());
			}
			user.getUserRoles().addAll(userRolse);
			
			user.setPrimaryAccount(accountService.createPrimaryAccount());
			user.setSavingAccount(accountService.createSavingsAccount());
			
			localUser = userDao.save(user);
		}
		return localUser;
	}
	
    public User saveUser (User user) {
        return userDao.save(user);
    }
    
    public List<User> findUserList() {
        return userDao.findAll();
    }

    public void enableUser (String username) {
        User user = findByUsername(username);
        user.setEnabled(true);
        userDao.save(user);
    }

    public void disableUser (String username) {
        User user = findByUsername(username);
        user.setEnabled(false);
        System.out.println(user.isEnabled());
        userDao.save(user);
        System.out.println(username + " is disabled.");
    }
}
