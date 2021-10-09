package com.example.UserRegistration;

import com.example.UserRegistration.user.User;
import com.example.UserRegistration.user.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class UserRegistrationApplicationTests {

	@Autowired
	private UserService userService;

	@Test
	public void ShouldSaveUser(){
		User user = new User("asd","asd","asd@gmail.com");
		userService.registerUser(user);
		Assertions.assertNotNull(userService.getbyUsername("asd"));
	}

	@Test
	public void ShouldGetUserByName(){
		User user = new User("asd","asd","asd@gmail.com");
		userService.registerUser(user);
		Assertions.assertSame(user,userService.getbyUsername("asd"));
	}

	@Test
	public void ShouldGetUserByEmail(){
		User user = new User("asd","asd","asd@gmail.com");
		userService.registerUser(user);
		Assertions.assertSame(user,userService.getbyEmail("asd@gmail.com"));
	}

	@Test
	public void ShouldDeleteUserById(){
		User user = new User("asd","asd","asd@gmail.com");
		userService.registerUser(user);
		userService.deleteUser(user.getId());
		Assertions.assertNull(userService.getbyUsername("asd"));
	}

}
