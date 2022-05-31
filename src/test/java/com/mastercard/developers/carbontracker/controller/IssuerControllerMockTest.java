package com.mastercard.developers.carbontracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mastercard.developers.carbontracker.service.GetDashboardService;
import com.mastercard.developers.carbontracker.service.IssuerService;
import com.mastercard.developers.carbontracker.service.UserRegistrationService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openapitools.client.model.AggregateCarbonScore;
import org.openapitools.client.model.Dashboard;
import org.openapitools.client.model.Email;
import org.openapitools.client.model.IssuerConfiguration;
import org.openapitools.client.model.IssuerProfile;
import org.openapitools.client.model.IssuerProfileDetails;
import org.openapitools.client.model.UserProfile;
import org.openapitools.client.model.UserReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static com.mastercard.developers.carbontracker.util.ServiceEndpoints.ADD_USER;
import static com.mastercard.developers.carbontracker.util.ServiceEndpoints.AGGREGATE_CARBON_SCORE;
import static com.mastercard.developers.carbontracker.util.ServiceEndpoints.DASHBOARDS;
import static com.mastercard.developers.carbontracker.util.ServiceEndpoints.DELETE_USER;
import static com.mastercard.developers.carbontracker.util.ServiceEndpoints.GET_ISSUER;
import static com.mastercard.developers.carbontracker.util.ServiceEndpoints.UPDATE_USER;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class IssuerControllerMockTest {

  @Mock
  private IssuerService issuerService;

  @Mock
  private UserRegistrationService userRegistrationService;

  @InjectMocks
  private IssuerController issuerController;

  @Mock
  private GetDashboardService getDashboardService;

  private MockMvc mockMvc;

  ObjectMapper objectMapper;

  private static final Gson gson =
    new GsonBuilder()
      .disableHtmlEscaping()
      .create();

  private final String X_OPENAPI_CLIENTID = "x-openapi-clientid";
  private final String CLIENTID = "aqkxHK2SNxsPci-m5vBJ_EkgG_5XnIaN0ocVfVoEdb4a5922";

  @SuppressWarnings("deprecation")
  @BeforeAll
  void setUp() {
    MockitoAnnotations.initMocks(this);
    objectMapper = new ObjectMapper();
    mockMvc = MockMvcBuilders.standaloneSetup(issuerController).build();
  }

  @Test
  @DisplayName("Get Auth token")
  void testGetAuthToken() throws Exception {
    Dashboard dashboard = new Dashboard();
    dashboard.setExpiryInMillis("timeStamp");
    dashboard.setUrl(DASHBOARDS);
    when(getDashboardService.getAuthToken(any(),any())).thenReturn(dashboard);
    MvcResult mvcResult = mockMvc.perform(get(DASHBOARDS, "userId")
      .contentType("application/json")).andExpect(status().isOk()).andReturn();

    String actual = mvcResult.getResponse().getContentAsString();

    Dashboard dashboardResponse = gson.fromJson(actual, Dashboard.class);
    // ASSERT
    assertNotNull(dashboardResponse);
  }

  @Test
  @DisplayName("Get AggregateScore")
  void testGetAggregateCarbonScore() throws Exception {
    AggregateCarbonScore aggregateCarbonScore = new AggregateCarbonScore();
    aggregateCarbonScore.setAggregateDate("Date");
    aggregateCarbonScore.setCarbonEmissionInGrams("50");
    when(issuerService.getAggregateCarbonScore(any())).thenReturn(aggregateCarbonScore);
    MvcResult mvcResult = mockMvc.perform(get(AGGREGATE_CARBON_SCORE, "userId")
      .contentType("application/json")).andExpect(status().isOk()).andReturn();

    String actual = mvcResult.getResponse().getContentAsString();

    AggregateCarbonScore response = gson.fromJson(actual, AggregateCarbonScore.class);
    // ASSERT
    assertNotNull(response);
  }


  @Test
  @DisplayName("Add User")
  void testAddUser() throws Exception {
    UserProfile userProfile = new UserProfile();
    userProfile.setEmail(new Email());
    when(userRegistrationService.userRegistration(any())).thenReturn(new UserReference());
    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(ADD_USER)
      .contentType("application/json").header(X_OPENAPI_CLIENTID, CLIENTID).content(gson.toJson(userProfile))).andExpect(status().isOk()).andReturn();

    String actual = mvcResult.getResponse().getContentAsString();
    UserReference response = gson.fromJson(actual, UserReference.class);
    // ASSERT
    assertNotNull(response);

  }

  @Test
  @DisplayName("Verify updateServiceProvider() method")
  void testUpdateServiceProvider() throws Exception {
    when(issuerService.updateIssuer(any())).thenReturn(new IssuerProfile());
    IssuerConfiguration issuerConfiguration = new IssuerConfiguration();
    final String range = "5050,5051";
    issuerConfiguration.setSupportedAccountRange(range);

    MvcResult mvcResult = mockMvc.perform(put(UPDATE_USER).contentType("application/json").header(X_OPENAPI_CLIENTID, CLIENTID)
      .content(new Gson().toJson(issuerConfiguration)))
      .andExpect(status().isOk()).andReturn();

    assertNotNull(mvcResult.getResponse().getContentAsString());

  }


  @Test
  @DisplayName("Test getIssuerDetails")
  void getIssuerDetails() throws Exception {
    IssuerProfileDetails issuerProfileDetails = new IssuerProfileDetails();
    issuerProfileDetails.setClientId("clientId");
    issuerProfileDetails.setCurrencyCode("USD");
    issuerProfileDetails.setCountryCode("USA");
    when(issuerService.getIssuer()).thenReturn(issuerProfileDetails);
    MvcResult mvcResult = mockMvc.perform(get(GET_ISSUER)
      .contentType("application/json")).andExpect(status().isOk()).andReturn();

    String actual = mvcResult.getResponse().getContentAsString();

    IssuerProfileDetails dashboardResponse = gson.fromJson(actual, IssuerProfileDetails.class);
    // ASSERT
    assertNotNull(dashboardResponse);
    assertNotNull(dashboardResponse.getCurrencyCode());
    assertNotNull(dashboardResponse.getCountryCode());
    assertNotNull(dashboardResponse.getClientId());

  }

  @Test
  @DisplayName("Test deleteUsers")
  void testDeleteUsers() throws Exception {
    String userId1 = "1653fbe5-4d70-4595-a3fa-a52249a2b6d3";

    List<String> stringList = new ArrayList<>();
    stringList.add(userId1);

    String request = objectMapper.writeValueAsString(stringList);
    ResponseEntity<List<String>> res = new ResponseEntity<>(HttpStatus.ACCEPTED);
    when(issuerService.deleteUsers(any())).thenReturn(res);

    MvcResult mvcResult = mockMvc
      .perform(MockMvcRequestBuilders.post(DELETE_USER).content(request)
        .contentType("application/json")
        .header(X_OPENAPI_CLIENTID, CLIENTID))
      .andExpect(status().isAccepted()).andReturn();

    String actual = mvcResult.getResponse().getContentAsString();
    assertNotNull(actual);
  }

}
