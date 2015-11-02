package com.jingyunbank.etrade.user.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.jingyunbank.core.Range;
import com.jingyunbank.etrade.api.exception.DataSavingException;
import com.jingyunbank.etrade.api.exception.DataUpdatingException;
import com.jingyunbank.etrade.api.user.IUserService;
import com.jingyunbank.etrade.api.user.bo.Users;

@Service("userService")
public class UserService implements IUserService{

	@Override
	public Optional<Users> getByUid(String id) {
		return null;
	}

	@Override
	public Optional<Users> getByPhone(String phone) {
		return null;
	}

	@Override
	public Optional<Users> getByUname(String username) {
		return null;
	}

	@Override
	public Optional<Users> getByEmail(String email) {
		return null;
	}

	@Override
	public Optional<Users> getByKey(String key) {
		return null;
	}

	@Override
	public boolean save(Users user) throws DataSavingException {
		return false;
	}

	@Override
	public boolean update(Users user) throws DataUpdatingException {
		return false;
	}

	@Override
	public List<Users> list(Range range) {
		return null;
	}

	@Override
	public List<Users> list(Date start, Date end) {
		return null;
	}

	@Override
	public boolean phoneExists(String phone) {
		return false;
	}

	@Override
	public boolean unameExists(String uname) {
		return false;
	}

	@Override
	public boolean emailExists(String email) {
		return false;
	}

	@Override
	public boolean exists(String key) {
		return false;
	}

}
