package es.geeksusma.jwtrestcontroller.security;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class WebPasswordEncoderTest {

    private WebPasswordEncoder webPasswordEncoder;

    @Before
    public void setUp() {
        webPasswordEncoder = new WebPasswordEncoder();
    }

    @Test
    public void should_returnRawPassword_when_Encode() {
        //given
        final CharSequence charSequence = "test";

        //when
        final String encode = webPasswordEncoder.encode(charSequence);

        //then
        assertThat(encode).isEqualTo("test");
    }

    @Test
    public void should_returnTrue_when_RawAndEncodedPasswordMatches() {
        //given
        final CharSequence raw = "test";
        final String encoded = "test";

        //when
        final Boolean matches = webPasswordEncoder.matches(raw, encoded);

        //then
        assertThat(matches).isTrue();
    }

    @Test
    public void should_returnFalse_when_RawAndEncodedPasswordDiffers() {
        //given
        final CharSequence raw = "test1";
        final String encoded = "test";

        //when
        final Boolean matches = webPasswordEncoder.matches(raw, encoded);

        //then
        assertThat(matches).isFalse();
    }

}