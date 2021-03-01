package com.webservice.app.controller;

import java.net.URI;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.webservice.app.exceptionhandler.UserNotFoundException;
import com.webservice.app.model.User;
import com.webservice.app.service.UserService;

@RestController
public class UserController {

	@Autowired
	UserService service;
	
	@Autowired
	MessageSource messageSource;

	@GetMapping("/users")
	public MappingJacksonValue getUserDetails() {
		Set<User> setOfUsers = service.getAllUsers();
		Set<String> properties = new HashSet<>(Arrays.asList("id","name"));
		SimpleBeanPropertyFilter filter = new SimpleBeanPropertyFilter
				.FilterExceptFilter(properties);
		FilterProvider filters = new SimpleFilterProvider().addFilter("UserBeanFilter", filter);
		MappingJacksonValue mapping = new MappingJacksonValue(setOfUsers);
		mapping.setFilters(filters);
		return mapping;
	}

	@GetMapping("/users/{id}")
	public EntityModel<User> getUserDetailsById(@PathVariable long id) {
		User user = service.getUserById(id);
		if (null == user) {
			throw new UserNotFoundException("User not found with id - " + id);
		}
		EntityModel<User> resource = EntityModel.of(user);
		WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).getUserDetails());
		resource.add(linkTo.withRel("all-users"));		
		return resource;
	}

	@GetMapping("/users/{id}/{name}")
	public User getUserDetailsByIdAndName(@PathVariable long id, @PathVariable String name) {
		return new User(id, name, new Date());
	}

	@PostMapping("/users")
	public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
		User theUser = service.createUser(user);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(theUser.getId())
				.toUri();

		return ResponseEntity.created(location).build();
	}

	@DeleteMapping("/users/{id}")
	public ResponseEntity<Object> deleteUserById(@PathVariable("id") long id) {
		if (service.deleteUser(id)) {
			return ResponseEntity.ok().build();
		} else {
			throw new UserNotFoundException("User not found with id - " + id);
		}

	}
	
	
//	@GetMapping("/helloworld-i18n")
//	public String helloWorld(@RequestHeader(name="Accept-Language", required = false) Locale locale) {
//		return messageSource.getMessage("good.morning.message", null, locale);
//	}
	
	@GetMapping("/helloworld-i18n")
	public String helloWorld() {
		return messageSource.getMessage("good.morning.message", null, LocaleContextHolder.getLocale());
	}


}
