package com.sroyc.humane.api.v1;

import java.io.Serializable;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sroyc.humane.api.HumaneBaseController;
import com.sroyc.humane.exception.HumaneEntityViolationException;
import com.sroyc.humane.services.HumaneService;
import com.sroyc.humane.util.ViewControllerUtil;
import com.sroyc.humane.view.models.HumaneUserRoleMapVO;
import com.sroyc.humane.view.models.HumaneUserRoleView;

@RestHumaneApiV1Controller
@RequestMapping("/humane/api/v1/userrolemap")
public class HumaneUserRoleMappingController implements HumaneBaseController {

	private HumaneService humaneService;
	private HumaneConfgurableProperty configProperty;

	public HumaneUserRoleMappingController(HumaneService humaneService, HumaneConfgurableProperty configProperty) {
		this.humaneService = humaneService;
		this.configProperty = configProperty;
	}

	@PostMapping("/userId/{userId}/roleId/{roleId}")
	public ResponseEntity<Serializable> create(@PathVariable("userId") String userId,
			@PathVariable("roleId") String roleId) {
		try {
			String userRoleId = this.humaneService.createUserRoleMap(userId, roleId);
			return ViewControllerUtil.create("User mapping created successfully", HttpStatus.CREATED, userRoleId);
		} catch (HumaneEntityViolationException e) {
			return ViewControllerUtil.create(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/userId/{userId}")
	public ResponseEntity<Serializable> findByUser(@PathVariable("userId") String userId) {
		List<HumaneUserRoleMapVO> mappings = this.humaneService.findMappingsByUser(userId);
		return ViewControllerUtil.create("", HttpStatus.OK, ViewControllerUtil.create(mappings));
	}

	@GetMapping("/view/userId/{userId}")
	public ResponseEntity<Serializable> findViewByUser(@PathVariable("userId") String userId) {
		List<HumaneUserRoleView> mappings = this.humaneService.findMappingViewOfUser(userId);
		return ViewControllerUtil.create("", HttpStatus.OK, ViewControllerUtil.create(mappings));
	}

	@GetMapping("/roleId/{roleId}/page/{page}")
	public ResponseEntity<Serializable> findByRole(@PathVariable("roleId") String roleId,
			@PathVariable("page") Integer page) {
		List<HumaneUserRoleMapVO> mappings = this.humaneService.findMappingsByRole(roleId, page,
				this.configProperty.getUserRoleMapListPageSize());
		return ViewControllerUtil.create("", HttpStatus.OK, ViewControllerUtil.create(mappings));
	}

	@DeleteMapping("/userId/{userId}/roleId/{roleId}")
	public ResponseEntity<Serializable> delete(@PathVariable("userId") String userId,
			@PathVariable("roleId") String roleId) {
		try {
			this.humaneService.deleteRoleMapping(userId, roleId);
			return ViewControllerUtil.create("User mapping deleted successfully", HttpStatus.OK);
		} catch (HumaneEntityViolationException e) {
			return ViewControllerUtil.create(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

}
