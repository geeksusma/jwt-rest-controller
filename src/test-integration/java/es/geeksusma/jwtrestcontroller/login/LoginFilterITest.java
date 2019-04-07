package es.geeksusma.jwtrestcontroller.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.geeksusma.jwtrestcontroller.config.JwtPropertiesConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginFilterITest {

    private static final String LOGIN_ENDPOINT = "/login";
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private JwtPropertiesConfiguration jwtPropertiesConfiguration;

    private ObjectMapper objectMapper;


    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        objectMapper = new ObjectMapper();

    }

    @Test
    public void should_returnOkHttpStatus_when_passwordAndEmailAreCorrect() throws Exception {

        // given
        final FetchUserLogin rightCredentialsUser = FetchUserLogin.builder()
                .username("test@geeksusma.es")
                .password("password")
                .build();

        // when
        mockMvc.perform(post(LOGIN_ENDPOINT).content(objectMapper.writeValueAsString(rightCredentialsUser)))
                // then
                .andExpect(status().isOk());
    }

    @Test
    public void should_returnRequestedUser_when_loginWasSuccessful() throws Exception {
        // given
        final FetchUserLogin rightCredentialsUser = FetchUserLogin.builder()
                .username("test@geeksusma.es")
                .password("password")
                .build();

        //when
        final MvcResult mvcResult = mockMvc.perform(post(LOGIN_ENDPOINT).content(objectMapper.writeValueAsString(rightCredentialsUser))).andReturn();

        //then
        assertThat(mvcResult.getResponse().getContentAsString()).contains("test@geeksusma.es");
    }

    @Test
    public void should_headerContainsToken_when_loginWasSuccessful() throws Exception {
        // given
        final FetchUserLogin rightCredentialsUser = FetchUserLogin.builder()
                .username("test@geeksusma.es")
                .password("password")
                .build();

        //when
        mockMvc.perform(post(LOGIN_ENDPOINT).content(objectMapper.writeValueAsString(rightCredentialsUser)))
                //then
                .andExpect(header().exists(jwtPropertiesConfiguration.getHeaderPrefix()));
    }

    @Test
    public void should_checkTokenHasPrefix_when_loginWasSuccessful() throws Exception {
        // given
        final FetchUserLogin rightCredentialsUser = FetchUserLogin.builder()
                .username("test@geeksusma.es")
                .password("password")
                .build();

        //when
        final MvcResult mvcResult = mockMvc.perform(post(LOGIN_ENDPOINT).content(objectMapper.writeValueAsString(rightCredentialsUser))).andReturn();
        final String token = mvcResult.getResponse().getHeader(jwtPropertiesConfiguration.getHeaderPrefix());

        //then
        assertThat(token).startsWith(jwtPropertiesConfiguration.getTokenPrefix());
    }

    @Test
    public void should_checkTokenFormat_when_loginWasSuccessful() throws Exception {
        // given
        final FetchUserLogin rightCredentialsUser = FetchUserLogin.builder()
                .username("test@geeksusma.es")
                .password("password")
                .build();

        //when
        final MvcResult mvcResult = mockMvc.perform(post(LOGIN_ENDPOINT).content(objectMapper.writeValueAsString(rightCredentialsUser))).andReturn();
        final String token = mvcResult.getResponse().getHeader(jwtPropertiesConfiguration.getHeaderPrefix());

        //then
        //the valid format for a token is "xxxxx.yyyyyy.zzzz" x -> is the expected algorithm, y -> the payload and z -> signature
        assert token != null;
        assertThat(token.split("\\.")).hasSize(3);
    }

}