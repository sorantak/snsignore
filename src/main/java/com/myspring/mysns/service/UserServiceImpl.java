package com.myspring.mysns.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.myspring.mysns.domain.ResponseData;
import com.myspring.mysns.domain.TokenVO;
import com.myspring.mysns.domain.UserVO;
import com.myspring.mysns.repository.UserDAO;
import com.myspring.mysns.util.RandomToken;

@Service("userService")
public class UserServiceImpl implements UserService {
	
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	ResponseData responseData;
	
	@Autowired
	UserVO userVO;
	
	@Autowired
	TokenVO tokenVO;
	
	@Override
	public ResponseData findAllUsersList() throws DataAccessException {
		logger.info("call findAllUsersList() method in UserService");
		List<UserVO> userList = userDAO.findAllUsersList();
		
		responseData.setCode(HttpStatus.OK);
		responseData.setMessage("SUCCESS");
		responseData.setData(userList);
		
		return responseData;
	}

	@Override
	public ResponseData findUserById(Long id) throws DataAccessException {
		logger.info("call findUserById() method in UserService");
		
		userVO = userDAO.findUserById(id);
		
		responseData.setCode(HttpStatus.OK);
		responseData.setMessage("SUCCESS");
		responseData.setData(userVO);
		
		return responseData;
	}

	@Override
	public ResponseData saveUser(UserVO userVO) throws DataAccessException {
		logger.info("call saveUser() method in UserService");
		
		userDAO.saveUser(userVO);
		
		Long id = userVO.getId();
		userVO = userDAO.findUserById(id);
		
		responseData.setCode(HttpStatus.OK);
		responseData.setMessage("SUCCESS");
		responseData.setData(userVO);
		
		return responseData;
	}
	
	@Override
	public ResponseData FindUserByToken(UserVO userVO) throws DataAccessException {
		logger.info("call authorizeUserByToken() method in UserService");
		
		userDAO.logInByUser(userVO);
		Long id = userVO.getId();
		
		StringBuffer token = RandomToken.createToken();
		
		String tokenToString = token.toString();
		
		tokenVO.setUserId(id);
		tokenVO.setToken(tokenToString);
		
		userDAO.createToken(tokenVO);
		
		userDAO.viewUserByToken(tokenToString);
		
		responseData.setCode(HttpStatus.OK);
		responseData.setMessage("SUCCESS");
		responseData.setData(tokenVO);
		
		return responseData;
	}

}
