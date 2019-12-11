package com.LoboProject.test;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = AuthorizationServerConfigurerAdapter.class)
@ActiveProfiles("mvc")
public class OAuthMvcTest {

  /*  @Autowired
    private WebApplicationContext wac;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    private MockMvc mockMvc;

    private static final String CLIENT_ID = "angular";
    private static final String CLIENT_SECRET = "@ngul@r";

    private static final String CONTENT_TYPE = "application/json;charset=UTF-8";

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).addFilter(springSecurityFilterChain).build();
    }

    private String obtainAccessToken(String username, String password) throws Exception {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("client_id", CLIENT_ID);
        params.add("username", username);
        params.add("password", password);

        // @formatter:off

        ResultActions result = mockMvc.perform(post("/oauth/token")
                               .params(params)
                               .with(HttpMethod(CLIENT_ID, CLIENT_SECRET))
                               .accept(CONTENT_TYPE))
                               .andExpect(status().isOk())
                               .andExpect(content().contentType(CONTENT_TYPE));
        
        // @formatter:on

        String resultString = result.andReturn().getResponse().getContentAsString();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("access_token").toString();
    }

    @Test
    public void givenNoToken_whenGetSecureRequest_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/employee").param("email", USERNAME)).andExpect(status().isUnauthorized());
    }

    @Test
    public void givenInvalidRole_whenGetSecureRequest_thenForbidden() throws Exception {
        final String accessToken = obtainAccessToken("user1", "pass");
        System.out.println("token:" + accessToken);
        mockMvc.perform(get("/employee").header("Authorization", "Bearer " + accessToken).param("email", USERNAME)).andExpect(status().isForbidden());
    }

    @Test
    public void givenToken_whenPostGetSecureRequest_thenOk() throws Exception {
        final String accessToken = obtainAccessToken("admin", "nimda");

        String employeeString = "{\"email\":\"" + USERNAME + "\",\"name\":\"" + PASSWORD + "\",\"age\":30}";

        // @formatter:off
        
        mockMvc.perform(post("/employee")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(CONTENT_TYPE)
                .content(employeeString)
                .accept(CONTENT_TYPE))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/employee")
                .param("email", USERNAME)
                .header("Authorization", "Bearer " + accessToken)
                .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$.name", is(PASSWORD)));
        
        // @formatter:on   
    }    */

}