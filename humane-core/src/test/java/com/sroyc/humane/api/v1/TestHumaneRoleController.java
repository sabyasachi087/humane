package com.sroyc.humane.api.v1;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.sroyc.humane.services.HumaneService;
import com.sroyc.humane.services.TestDataCreator;
import com.sroyc.humane.util.UniqueSequenceGenerator;
import com.sroyc.humane.view.models.HumaneRoleMasterVO;

@WebMvcTest(controllers = HumaneRoleController.class)
class TestHumaneRoleController extends AbstractTestController {

	private static final String ROLE_API_URI = "/humane/api/v1/role";

	@MockBean
	private HumaneService humaneService;

	@BeforeEach
	void init() {
		Mockito.reset(this.humaneService);
	}

	@Test
	void test_createRole() throws Exception {
		String roleId = UniqueSequenceGenerator.CHAR16.next();
		Mockito.when(this.humaneService.createRole(Mockito.any(HumaneRoleMasterVO.class))).thenReturn(roleId);
		HumaneRoleMasterVO vo = TestDataCreator.createRoleVO();
		vo.setRoleId(null);
		String body = objectMapper().writeValueAsString(vo);
		this.getMockMvc()
				.perform(
						MockMvcRequestBuilders.post(ROLE_API_URI).content(body).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isCreated()).andExpect(jsonPath("$.data").value(roleId));
	}

	@Test
	void test_updateName() throws Exception {
		String roleId = "R1";
		String desc = "Some description";
		getMockMvc().perform(MockMvcRequestBuilders.put(ROLE_API_URI + "/id/{roleId}/name/{name}", roleId, desc))
				.andDo(print()).andExpect(status().isOk());
		Mockito.verify(this.humaneService, Mockito.times(1)).updateRole(roleId, desc, null);
		Mockito.reset(this.humaneService);
		String error = "Mock test error";
		Mockito.when(this.humaneService.updateRole("R2", desc, null)).thenThrow(new RuntimeException(error));
		getMockMvc().perform(MockMvcRequestBuilders.put(ROLE_API_URI + "/id/{roleId}/name/{name}", "R2", desc))
				.andDo(print()).andExpect(jsonPath("$.message").value(error));
		Mockito.reset(this.humaneService);
		getMockMvc().perform(MockMvcRequestBuilders.put(ROLE_API_URI + "/id/{roleId}/name/{name}/hierarchy/{hierarchy}",
				roleId, desc, 1)).andDo(print()).andExpect(status().isOk());
		Mockito.verify(this.humaneService, Mockito.times(1)).updateRole(roleId, desc, 1);
	}

	@Test
	void test_toggleDeprecation() throws Exception {
		String roleId = "R1";
		boolean deprecate = true;
		getMockMvc().perform(
				MockMvcRequestBuilders.put(ROLE_API_URI + "/id/{roleId}/deprecate/{deprecate}", roleId, deprecate))
				.andDo(print()).andExpect(status().isOk());
		Mockito.verify(this.humaneService, Mockito.times(1)).toggleRoleDeprecation("R1", deprecate);
	}

	@Test
	void test_delete() throws Exception {
		String roleId = "R1";
		Mockito.when(this.humaneService.deleteRole("R1")).thenReturn(true);
		Mockito.when(this.humaneService.deleteRole("R2")).thenReturn(false);
		getMockMvc().perform(MockMvcRequestBuilders.delete(ROLE_API_URI + "/id/{roleId}", roleId)).andDo(print())
				.andExpect(status().isOk());
		roleId = "R2";
		getMockMvc().perform(MockMvcRequestBuilders.delete(ROLE_API_URI + "/id/{roleId}", roleId)).andDo(print())
				.andExpect(status().is(417));
	}

}
