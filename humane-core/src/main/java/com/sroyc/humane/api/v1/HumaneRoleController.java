package com.sroyc.humane.api.v1;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sroyc.humane.api.HumaneBaseController;
import com.sroyc.humane.exception.HumaneValidationException;
import com.sroyc.humane.services.HumaneService;
import com.sroyc.humane.util.ViewControllerUtil;
import com.sroyc.humane.view.models.HumaneRoleMasterVO;

@RestHumaneApiV1Controller
@RequestMapping("/humane/api/v1/role")
public class HumaneRoleController implements HumaneBaseController {

	private static final Logger LOGGER = LogManager.getLogger(HumaneRoleController.class);

	private HumaneService humaneService;

	@Autowired
	public HumaneRoleController(HumaneService humaneService) {
		super();
		this.humaneService = humaneService;
	}

	@GetMapping
	public ResponseEntity<Serializable> getAllRoles() {
		return ViewControllerUtil.create(this.humaneService.findAllRoles(), "", HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Serializable> createRole(@RequestBody HumaneRoleMasterVO role) {
		String roleId = this.humaneService.createRole(role);
		return ViewControllerUtil.create("Role Created successfully", HttpStatus.CREATED, roleId);
	}

	@PutMapping("/id/{roleId}/name/{name}")
	public ResponseEntity<Serializable> updateName(@PathVariable("roleId") String roleId,
			@PathVariable("name") String name) {
		this.humaneService.updateRole(roleId, name, null);
		return ViewControllerUtil.create("Role Updated successfully", HttpStatus.OK);
	}

	@PutMapping("/id/{roleId}/name/{name}/hierarchy/{hierarchy}")
	public ResponseEntity<Serializable> updateName(@PathVariable("roleId") String roleId,
			@PathVariable("hierarchy") Integer hierarchy, @PathVariable("name") String name) {
		this.humaneService.updateRole(roleId, name, hierarchy);
		return ViewControllerUtil.create("Role Updated successfully", HttpStatus.OK);
	}

	@PutMapping("/id/{roleId}/deprecate/{deprecate}")
	public ResponseEntity<Serializable> toggleDeprecation(@PathVariable("roleId") String roleId,
			@PathVariable("deprecate") boolean deprecate) {
		this.humaneService.toggleRoleDeprecation(roleId, deprecate);
		return ViewControllerUtil.create("Role Updated successfully", HttpStatus.OK);
	}

	@DeleteMapping("/id/{roleId}")
	public ResponseEntity<Serializable> delete(@PathVariable("roleId") String roleId) {
		if (this.humaneService.deleteRole(roleId)) {
			return ViewControllerUtil.create("Role deleted successfully", HttpStatus.OK);
		} else {
			return ViewControllerUtil.create("Role is being used. Delete all child(s) first",
					HttpStatus.EXPECTATION_FAILED);
		}
	}

	@ExceptionHandler(value = Throwable.class)
	public ResponseEntity<Serializable> defaultExceptionHandler(Throwable t, HttpServletRequest request) {
		LOGGER.error("Error thrown by request uri [{}]", request.getRequestURI(), t);
		if (t instanceof HumaneValidationException) {
			return ViewControllerUtil.create(t.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return ViewControllerUtil.create(t.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
