package com.mastercard.developers.carbontracker.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mastercard.developer.encryption.FieldLevelEncryptionConfig;
import com.mastercard.developers.carbontracker.configuration.ApiConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.IssuerApi;
import org.openapitools.client.model.AggregateCarbonScore;
import org.openapitools.client.model.Dashboard;
import org.openapitools.client.model.Error;
import org.openapitools.client.model.ErrorWrapper;
import org.openapitools.client.model.Errors;
import org.openapitools.client.model.IssuerConfiguration;
import org.openapitools.client.model.IssuerProfile;
import org.openapitools.client.model.IssuerProfileDetails;
import org.openapitools.client.model.UserProfile;
import org.openapitools.client.model.UserReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)

@Slf4j
class IssuerServiceImplTest {


  private IssuerServiceImpl issuerService;

  @Mock
  private ApiConfiguration apiConfiguration;

  @Mock
  private IssuerApi issuerApiForNonEncryptedPayload;

  private static Gson gson =
    new GsonBuilder()
      .disableHtmlEscaping()
      .create();

  private class config extends FieldLevelEncryptionConfig {

  }

  @BeforeEach
  public void setUp() throws Exception {


    apiConfiguration = mock(ApiConfiguration.class);
    when(apiConfiguration.getEncryptionKeyFile()).thenReturn(new ClassPathResource("/test.pem"));
    issuerService = new IssuerServiceImpl(apiConfiguration);
    System.out.println();
  }

  @Test
  public void testGetAuthToken() throws Exception {

    issuerApiForNonEncryptedPayload = mock(IssuerApi.class);
    ReflectionTestUtils.setField(issuerService, "issuerApiForNonEncryptedPayload", issuerApiForNonEncryptedPayload);
    when(issuerApiForNonEncryptedPayload.getAuthToken(anyString())).thenReturn(new Dashboard());
    assertNotNull(issuerService.getAuthToken("22222"));
  }

  @Test
  @DisplayName("Throws Exception")
  public void testGetAuthTokenThrowsException() throws Exception {


    issuerApiForNonEncryptedPayload = mock(IssuerApi.class);
    ReflectionTestUtils.setField(issuerService, "issuerApiForNonEncryptedPayload", issuerApiForNonEncryptedPayload);
    ApiException api = new ApiException("ApiException", null, 400, null, gson.toJson(getErrorDetail()));

    when(issuerApiForNonEncryptedPayload.getAuthToken(anyString())).thenThrow(api);
    Assertions.assertThrows(Exception.class, () -> issuerService.getAuthToken("hdclientId"));
    log.info("GetAuth Token throws exception completed");

  }


  @Test
  @DisplayName("getAggregateCarbonScore")
  public void testGetAggregateCarbonScore() throws Exception {

    issuerApiForNonEncryptedPayload = mock(IssuerApi.class);
    ReflectionTestUtils.setField(issuerService, "issuerApiForNonEncryptedPayload", issuerApiForNonEncryptedPayload);

    when(issuerApiForNonEncryptedPayload.getAggregateCarbonScore(anyString())).thenReturn(new AggregateCarbonScore());
    assertNotNull(issuerService.getAggregateCarbonScore("userId"));
  }

  @Test
  @DisplayName("getAggregateCarbonScore throws exception")
  public void testGetAggregateCarbonScoreThrowsException() throws Exception {

    issuerApiForNonEncryptedPayload = mock(IssuerApi.class);
    ReflectionTestUtils.setField(issuerService, "issuerApiForNonEncryptedPayload", issuerApiForNonEncryptedPayload);
    ApiException api = new ApiException("ApiException", null, 400, null, gson.toJson(getErrorDetail()));

    when(issuerApiForNonEncryptedPayload.getAggregateCarbonScore(anyString())).thenThrow(api);
    Assertions.assertThrows(Exception.class, () -> issuerService.getAggregateCarbonScore("hdclientId"));
    log.info("getAggregateCarbonScore throws exception completed");

  }

  @Test
  @DisplayName("userRegistration")
  public void testUserRegistration() throws Exception {

    issuerApiForNonEncryptedPayload = mock(IssuerApi.class);
    ReflectionTestUtils.setField(issuerService, "issuerApiForEncryptedPayload", issuerApiForNonEncryptedPayload);

    when(issuerApiForNonEncryptedPayload.userRegistration(any())).thenReturn(new UserReference());
    assertNotNull(issuerService.userRegistration(new UserProfile()));
  }

  @Test
  @DisplayName("UserRegistration throws exception")
  public void testUserRegistrationThrowsException() throws Exception {

    issuerApiForNonEncryptedPayload = mock(IssuerApi.class);
    ReflectionTestUtils.setField(issuerService, "issuerApiForEncryptedPayload", issuerApiForNonEncryptedPayload);

    ApiException api = new ApiException("ApiException", null, 400, null, gson.toJson(getErrorDetail()));

    when(issuerApiForNonEncryptedPayload.userRegistration(any())).thenThrow(api);
    Assertions.assertThrows(Exception.class, () -> issuerService.userRegistration(new UserProfile()));

  }

  @Test
  @DisplayName("delete user")
  public void testDeleteUser() throws Exception {

    issuerApiForNonEncryptedPayload = mock(IssuerApi.class);
    ReflectionTestUtils.setField(issuerService, "issuerApiForNonEncryptedPayload", issuerApiForNonEncryptedPayload);

    when(issuerApiForNonEncryptedPayload.deleteUsers(any())).thenReturn(new ArrayList<String>());
    assertNotNull(issuerService.deleteUsers(new ArrayList<String>()));
  }

  @Test
  @DisplayName("delete user throws exception")
  public void testDeleteUserThrowsException() throws Exception {

    issuerApiForNonEncryptedPayload = mock(IssuerApi.class);
    ReflectionTestUtils.setField(issuerService, "issuerApiForNonEncryptedPayload", issuerApiForNonEncryptedPayload);

    ApiException api = new ApiException("ApiException", null, 400, null, gson.toJson(getErrorDetail()));

    when(issuerApiForNonEncryptedPayload.deleteUsers(any())).thenThrow(api);
    Assertions.assertThrows(Exception.class, () -> issuerService.deleteUsers(new ArrayList<String>()));

  }

  @Test
  @DisplayName("Update user")
  public void testUpdateUser() throws Exception {

    issuerApiForNonEncryptedPayload = mock(IssuerApi.class);
    ReflectionTestUtils.setField(issuerService, "issuerApiForNonEncryptedPayload", issuerApiForNonEncryptedPayload);

    when(issuerApiForNonEncryptedPayload.updateIssuer(any())).thenReturn(new IssuerProfile());
    assertNotNull(issuerService.updateIssuer(new IssuerConfiguration()));
  }

  @Test
  @DisplayName("update user throws exception")
  public void testUpdateUserThrowsException() throws Exception {

    issuerApiForNonEncryptedPayload = mock(IssuerApi.class);
    ReflectionTestUtils.setField(issuerService, "issuerApiForNonEncryptedPayload", issuerApiForNonEncryptedPayload);

    ApiException api = new ApiException("ApiException", null, 400, null, gson.toJson(getErrorDetail()));

    when(issuerApiForNonEncryptedPayload.updateIssuer(any())).thenThrow(api);
    Assertions.assertThrows(Exception.class, () -> issuerService.updateIssuer(new IssuerConfiguration()));

  }

  @Test
  @DisplayName("Get user")
  public void testGetUser() throws Exception {

    issuerApiForNonEncryptedPayload = mock(IssuerApi.class);
    ReflectionTestUtils.setField(issuerService, "issuerApiForNonEncryptedPayload", issuerApiForNonEncryptedPayload);

    when(issuerApiForNonEncryptedPayload.getIssuer()).thenReturn(new IssuerProfileDetails());
    assertNotNull(issuerService.getIssuer());
  }

  @Test
  @DisplayName("Get user throws exception")
  public void testGetUserThrowsException() throws Exception {

    issuerApiForNonEncryptedPayload = mock(IssuerApi.class);
    ReflectionTestUtils.setField(issuerService, "issuerApiForNonEncryptedPayload", issuerApiForNonEncryptedPayload);

    ApiException api = new ApiException("ApiException", null, 400, null, gson.toJson(getErrorDetail()));

    when(issuerApiForNonEncryptedPayload.getIssuer()).thenThrow(api);
    Assertions.assertThrows(Exception.class, () -> issuerService.getIssuer());

  }

  private ErrorWrapper getErrorDetail() {
    List<Error> errors = new ArrayList<>();
    Error error = new Error();
    error.setDescription("UserName Already taken");
    error.setDetails("UserName Already taken");
    error.setReasonCode("REASONCODE");
    error.setRecoverable(false);
    error.setSource("CCE");
    errors.add(error);
    Errors errs = new Errors();
    errs.setError(errors);
    ErrorWrapper errorWrapper = new ErrorWrapper();
    errorWrapper.setErrors(errs);
    return errorWrapper;
  }
}
