package com.kartik.Trading;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class RbacSecurityIntegrationTest extends BaseIntegrationTest {

	@Autowired
	private WebApplicationContext context;
	
    private MockMvc mockMvc;

    @BeforeEach
    void setupMockMvc() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }
    
    @Test
    @WithMockUser(username = "hacker@kartik.com", roles = {"USER"})
    void standardUserCannotAccessAdminEndpoint_Returns403() throws Exception {
        mockMvc.perform(get("/api/admin/asset"))
               .andExpect(status().isForbidden());
    }
    
    @Test
    void unauthenticatedRequest_Returns401() throws Exception {
        mockMvc.perform(get("/api/admin/asset"))
               .andExpect(status().isUnauthorized());
    }
}
