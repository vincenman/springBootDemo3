package com.luv2code.springboot.cruddemo.rest;

import io.micrometer.core.instrument.util.StringUtils;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luv2code.springboot.cruddemo.entity.UserAccount;
import com.luv2code.springboot.cruddemo.entity.UserAccountWithAction;
import com.luv2code.springboot.cruddemo.service.UserAccountService;

@RestController
@RequestMapping("/api")
public class UserAccountRestController {

	private UserAccountService userAccountService;

	@Autowired
	public UserAccountRestController(UserAccountService theUserAccountService) {
		userAccountService = theUserAccountService;
	}

	// expose "/userAccounts" and return list of userAccounts
	@GetMapping("/userAccounts")
	public List<UserAccount> findAll() {
		return userAccountService.findAll();
	}

	// add mapping for GET /userAccounts/{userAccountsId}

	@GetMapping("/userAccounts/{userAccountId}")
	public UserAccount getUserAccount(@PathVariable int userAccountId) {

		UserAccount theUserAccount = userAccountService.findById(userAccountId);

		if (theUserAccount == null) {
			throw new RuntimeException("theUserAccount id not found - "
					+ userAccountId);
		}

		return theUserAccount;
	}

	// add mapping for POST /userAccounts - add new userAccounts

	@PostMapping("/userAccounts")
	public UserAccount addUserAccount(
			@Valid @RequestBody UserAccount theUserAccount) {

		// also just in case they pass an id in JSON ... set id to 0
		// this is to force a save of new item ... instead of update

		theUserAccount.setId(0);

		userAccountService.save(theUserAccount);

		return theUserAccount;
	}

	// add mapping for PUT /userAccounts - update existing userAccounts

	@PutMapping("/userAccounts")
	public UserAccount updateUserAccount(
			@RequestBody UserAccountWithAction theUserAccountWithAction) {

		String actionType = theUserAccountWithAction.getActionType();

		if (StringUtils.isBlank(actionType)) {

			UserAccount userAccount = new UserAccount();
			userAccount.setId(theUserAccountWithAction.getId());
			userAccount.setFirstName(theUserAccountWithAction.getFirstName());
			userAccount.setLastName(theUserAccountWithAction.getLastName());
			userAccount.setBalance(theUserAccountWithAction.getBalance());

			userAccountService.save(userAccount);
			return userAccount;
		} else if (actionType.equals("balance")) {
			UserAccount userAccount = new UserAccount();
			userAccount = updateTheBalance(theUserAccountWithAction,
					theUserAccountWithAction.getBalance());
			return userAccount;
		} else {
			throw new RuntimeException("the Action Type is invalid - "
					+ actionType);
		}

	}

	private UserAccount updateTheBalance(UserAccount theUserAccount,
			double newBalance) {
		UserAccount theUserAccountInDB = getUserAccount(theUserAccount.getId());	

		theUserAccountInDB.setBalance(newBalance);

		userAccountService.save(theUserAccountInDB);

		return theUserAccountInDB;
	}

}








