package com.bug_report.serviceImpl;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bug_report.dto.UserDto;
import com.bug_report.entity.UserEntity;
import com.bug_report.repository.UserRepository;
import com.bug_report.service.UserService;
import com.bug_report.util.MapObject;
import com.bug_report.util.ValidateData;

@Service
public class UserServiceImpl implements UserService{
	@Autowired
	UserRepository userRepository;
	
		
	@Override
	public Object addUser(UserDto user) {
		UserEntity userEntity=null;
		if (!ObjectUtils.isEmpty(user)) {
			 
			 List<String> errors=ValidateData.validateUser(user);
			 if (errors.isEmpty()) {
				 userEntity = MapObject.userDtoToUserEntity(user);
				 UserEntity existingUser;
				 existingUser=userRepository.findByEmail(userEntity.getEmail());
				if (!ObjectUtils.isEmpty(existingUser)) {
					return Arrays.asList("email already exist");
				}
				userRepository.save(userEntity);
				return null;
			} else {
				return errors;
			}
		}
		else {
			System.out.println("Empty object");
		}
		return Arrays.asList("error in saving data...");
	}


	@Override
	public List<UserDto> getAllUser() {
		List<UserEntity> userEntities=userRepository.findAll();
		List<UserDto> users=MapObject.userEntitysToUserDtos(userEntities);
		return users;
	}


	@Override
	public Object getUserByEmpId(long empId) {
		
		if (StringUtils.isEmpty(""+empId)) {
			return "Enter valid employee id";
		}
		UserEntity user=null;;
		try {
			user=userRepository.findById(empId).get();
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return "Record not found..";
			
		}
		return MapObject.userEntityToUserDto(user);
	}


	@Override
	public Object updateByEmail(String email,UserDto userDto) {
		if (StringUtils.isBlank(email)) {
			return "Enter valid email...";
		}
		UserEntity existringUser=userRepository.findByEmail(email);
		if (ObjectUtils.isEmpty(existringUser)) {
			return "User not found enter valid email...";
		}
		List<String> responseList=ValidateData.validateUser(userDto);
		if (responseList.isEmpty()) {
			UserEntity userEntity=(UserEntity) MapObject.mapObject(userDto, UserEntity.class);
			existringUser.setFirstName(userEntity.getFirstName());
			existringUser.setLastName(userEntity.getLastName());
			existringUser.setUserRoleId(userEntity.getUserRoleId());
			existringUser.setEmail(userEntity.getEmail());
			existringUser.setPassword(userEntity.getPassword());
			existringUser.setDepartment(userEntity.getDepartment());
			userRepository.save(existringUser);
			return null;
		} else {
			return responseList;
		}
		
	}


	@Override
	public Object updateByEmpId(long empid, UserDto userDto) {
		if (ObjectUtils.isEmpty(empid)) {
			return "Enter valid empid...";
		}
		UserEntity existringUser=null;
		try {
			existringUser=userRepository.findById(empid).get();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		if (ObjectUtils.isEmpty(existringUser)) {
			return "User not found enter valid empid...";
		}
		List<String> responseList=ValidateData.validateUser(userDto);
		if (responseList.isEmpty()) {
			UserEntity userEntity=(UserEntity) MapObject.mapObject(userDto, UserEntity.class);
			existringUser.setFirstName(userEntity.getFirstName());
			existringUser.setLastName(userEntity.getLastName());
			existringUser.setUserRoleId(userEntity.getUserRoleId());
			existringUser.setEmail(userEntity.getEmail());
			existringUser.setPassword(userEntity.getPassword());
			existringUser.setDepartment(userEntity.getDepartment());
			userRepository.save(existringUser);
			return null;
		} else {
			return responseList;
		}
	}
	
}
