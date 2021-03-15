package com.sroyc.humane.api.v1;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sroyc.humane.api.HumaneBaseController;
import com.sroyc.humane.data.dao.HumaneUserMasterFilter;
import com.sroyc.humane.exception.HumaneValidationException;
import com.sroyc.humane.services.HumaneService;
import com.sroyc.humane.util.HumaneGeneralConstant;
import com.sroyc.humane.util.ViewControllerUtil;
import com.sroyc.humane.view.models.GenericListWrapper;
import com.sroyc.humane.view.models.HumaneUserMasterVO;
import com.sroyc.humane.view.models.PrimaryRecordAware;

@RestHumaneApiV1Controller
@RequestMapping("/humane/api/v1/user")
public class HumaneUserController implements HumaneBaseController {

	private static final Logger LOGGER = LogManager.getLogger(HumaneUserController.class);

	private HumaneService humaneService;
	private HumaneConfgurableProperty configProperty;

	@Autowired
	public HumaneUserController(HumaneService humaneService, HumaneConfgurableProperty configProperty) {
		super();
		this.humaneService = humaneService;
		this.configProperty = configProperty;
	}

	@GetMapping("/emailOrUsername/{emailOrUserName}")
	public ResponseEntity<Serializable> getUserByEmail(@PathVariable("emailOrUserName") String emailOrUsername) {
		HumaneUserMasterVO user = this.humaneService.findByEmailOrUsername(emailOrUsername);
		if (user == null) {
			return ViewControllerUtil.create(HumaneGeneralConstant.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
		} else {
			return ViewControllerUtil.create("", HttpStatus.OK, user);
		}
	}

	@GetMapping("/id/{userId}")
	public ResponseEntity<Serializable> getUserById(@PathVariable("userId") String userId) {
		HumaneUserMasterVO user = this.humaneService.findByUserId(userId);
		if (user == null) {
			return ViewControllerUtil.create(HumaneGeneralConstant.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
		} else {
			return ViewControllerUtil.create("", HttpStatus.OK, user);
		}
	}

	@GetMapping("/all/{page}")
	public ResponseEntity<Serializable> findAllMatchingUsers(
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "active", required = false) Boolean active,
			@RequestParam(value = "delete", required = false) Boolean delete, @PathVariable("page") Integer page) {
		HumaneUserMasterFilter filter = new HumaneUserMasterFilter(name, active, delete);
		return ViewControllerUtil.create(
				this.humaneService.findAllWithFilter(filter, page, this.configProperty.getUserListPageSize()), page,
				this.configProperty.getUserListPageSize(), "", HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Serializable> createUser(@RequestBody HumaneUserMasterVO user) {
		Assert.isTrue(!StringUtils.hasText(user.getUserId()), "Cannot re-create for existing user");
		String userId = this.humaneService.createUser(user);
		return ViewControllerUtil.create("User Created Successfully", HttpStatus.CREATED, userId);
	}

	@PutMapping
	public ResponseEntity<Serializable> updateUser(@RequestBody HumaneUserMasterVO user) {
		Assert.isTrue(StringUtils.hasText(user.getUserId()), "User does not exists");
		if (this.humaneService.updateUser(user)) {
			return ViewControllerUtil.create(HumaneGeneralConstant.USER_UPDATION_MESSAGE, HttpStatus.OK);
		} else {
			return ViewControllerUtil.create(HumaneGeneralConstant.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/activate/{userId}/{toggle}")
	public ResponseEntity<Serializable> toggleActivation(@PathVariable("userId") String userId,
			@PathVariable("toggle") Boolean toggle) {
		if (this.humaneService.toggleActivation(userId, toggle)) {
			return ViewControllerUtil.create(HumaneGeneralConstant.USER_UPDATION_MESSAGE, HttpStatus.OK);
		} else {
			return ViewControllerUtil.create(HumaneGeneralConstant.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/delete/{userId}/{toggle}")
	public ResponseEntity<Serializable> toggleDeletion(@PathVariable("userId") String userId,
			@PathVariable("toggle") Boolean toggle) {
		if (this.humaneService.toggleDeletion(userId, toggle)) {
			return ViewControllerUtil.create(HumaneGeneralConstant.USER_UPDATION_MESSAGE, HttpStatus.OK);
		} else {
			return ViewControllerUtil.create(HumaneGeneralConstant.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/phone/{userId}")
	public ResponseEntity<Serializable> getPhoneRecords(@PathVariable("userId") String userId) {
		return ViewControllerUtil.create(this.humaneService.getPhoneNumbers(userId), "", HttpStatus.OK);
	}

	@PutMapping("/phones/{userId}")
	public ResponseEntity<Serializable> addOrUpdatePhoneRecords(@PathVariable("userId") String userId,
			@RequestBody GenericListWrapper data) {
		if (data != null) {
			this.validateRecords(data.getPhones());
			this.humaneService.replaceAllPhoneRecords(userId, data.getPhones());
			return ViewControllerUtil.create(HumaneGeneralConstant.USER_PHONE_UPDATION_MESSAGE, HttpStatus.OK);
		}
		return ViewControllerUtil.create("Empty phone records", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/address/{userId}")
	public ResponseEntity<Serializable> getAddresses(@PathVariable("userId") String userId) {
		return ViewControllerUtil.create(this.humaneService.getAddresses(userId), "", HttpStatus.OK);
	}

	@PutMapping("/addresses/{userId}")
	public ResponseEntity<Serializable> addOrUpdateAddresses(@PathVariable("userId") String userId,
			@RequestBody GenericListWrapper data) {
		if (data != null) {
			this.validateRecords(data.getAddresses());
			this.humaneService.replaceAllAddress(userId, data.getAddresses());
			return ViewControllerUtil.create(HumaneGeneralConstant.USER_ADDRESS_UPDATION_MESSAGE, HttpStatus.OK);
		}
		return ViewControllerUtil.create("Empty address records", HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = Throwable.class)
	public ResponseEntity<Serializable> defaultExceptionHandler(Throwable t, HttpServletRequest request) {
		LOGGER.error("Error thrown by request uri [{}]", request.getRequestURI(), t);
		if (t instanceof HumaneValidationException) {
			return ViewControllerUtil.create(t.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return ViewControllerUtil.create(t.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	protected void validateRecords(List<? extends PrimaryRecordAware> records) {
		if (!CollectionUtils.isEmpty(records)) {
			boolean atLeastOneIsPrimary = false;
			for (PrimaryRecordAware record : records) {
				if (!atLeastOneIsPrimary && record.isPrimary()) {
					atLeastOneIsPrimary = true;
				} else if (atLeastOneIsPrimary && record.isPrimary()) {
					throw new HumaneValidationException("More then one primary");
				}
			}
			if (!atLeastOneIsPrimary) {
				throw new HumaneValidationException("At least one must be primary");
			}
		} else {
			throw new HumaneValidationException("Must have at least one record");
		}
	}

}
