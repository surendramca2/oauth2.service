package com.surendra.oauth.server.security.web.authentication;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.util.MultiValueMap;

public class OAuthEndpointUtilsTest {

  /**
   * TestCase: Method used to test getParameters.
   * Setup: pass a HttpServletRequest request with request parameters.
   * Action: OAuthEndpointUtils.getParameters(requestMock) method called.
   * Expected: Verify that the getParameters(HttpServletRequest request) returns NonNull Map
   */
  @Test
  public void testGetParameters(){
    HttpServletRequest requestMock = Mockito.mock(HttpServletRequest.class);
    Map<String, String[]> parameterMap = new HashMap<>();
    String[] val = new String[1];
    val[0] = "val1";
    parameterMap.put("scope", val);
    parameterMap.put("param", new String[0]);
    Mockito.when(requestMock.getParameterMap()).thenReturn(parameterMap);
    MultiValueMap<String, String> parameters = OAuthEndpointUtils.getParameters(requestMock);
    assertNotNull(parameters);
  }

}
