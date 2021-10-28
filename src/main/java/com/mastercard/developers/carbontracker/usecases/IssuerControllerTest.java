package com.mastercard.developers.carbontracker.usecases;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.mastercard.developers.carbontracker.service.IssuerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.client.model.AggregateCarbonScore;
import org.openapitools.client.model.Dashboard;
import org.openapitools.client.model.IssuerConfiguration;
import org.openapitools.client.model.IssuerProfileDetails;
import org.openapitools.client.model.UserReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.mastercard.developers.carbontracker.util.ServiceEndpoints.ADD_USER;
import static com.mastercard.developers.carbontracker.util.ServiceEndpoints.AGGREGATE_CARBON_SCORE;
import static com.mastercard.developers.carbontracker.util.ServiceEndpoints.DASHBOARDS;
import static com.mastercard.developers.carbontracker.util.ServiceEndpoints.DELETE_USER;
import static com.mastercard.developers.carbontracker.util.ServiceEndpoints.GET_ISSUER;
import static com.mastercard.developers.carbontracker.util.ServiceEndpoints.UPDATE_USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IssuerControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private Gson gson;

  @Autowired
  private IssuerService issuerService;
  private String userId = null;


  @Autowired
  ObjectMapper objectMapper;

  private String xOpenApiClientId = "x-openapi-clientid";
  private String clientId = "aqkxHK2SNxsPci-m5vBJ_EkgG_5XnIaN0ocVfVoEdb4a5922";
  private String applicationJson = "application/json";

  @Test
  @DisplayName("Test deleteUsers")
  void testDeleteUsers() throws Exception {

    String userId1 = "1653fbe5-4d70-4595-a3fa-a52249a2b6d3";

    List<String> stringList = new ArrayList<>();
    stringList.add(userId1);


    String request = objectMapper.writeValueAsString(stringList);

    MvcResult mvcResult = mockMvc
      .perform(post(DELETE_USER).content(request)
        .contentType(applicationJson)
        .header(xOpenApiClientId, clientId))
      .andExpect(status().isAccepted()).andReturn();


    String actual = mvcResult.getResponse().getContentAsString();
    assertNotNull(actual);
  }


  @Test
  @DisplayName("Test getAggregateCarbonScore")
  @Order(2)
  void getAggregateCarbonScore() throws Exception {
    MvcResult mvcResult = mockMvc.perform(get(AGGREGATE_CARBON_SCORE, userId)
      .contentType(applicationJson)).andExpect(status().isOk()).andReturn();

    String actual = mvcResult.getResponse().getContentAsString();

    AggregateCarbonScore aggregateCarbonScore = gson.fromJson(actual, AggregateCarbonScore.class);
    // ASSERT
    assertNotNull(aggregateCarbonScore);
    assertNotNull(aggregateCarbonScore.getCarbonEmissionInGrams());
    assertNotNull(aggregateCarbonScore.getAggregateDate());

  }


  @Test
  @Order(3)
  void getDashboardUrl() throws Exception {

    MvcResult mvcResult = mockMvc.perform(get(DASHBOARDS, userId)
      .contentType(applicationJson)).andExpect(status().isOk()).andReturn();

    String actual = mvcResult.getResponse().getContentAsString();

    Dashboard dashboardResponse = gson.fromJson(actual, Dashboard.class);
    // ASSERT
    assertNotNull(dashboardResponse);
    assertNotNull(dashboardResponse.getExpiryInMillis());
    assertNotNull(dashboardResponse.getUrl());

  }

  public static String getFileAsString(String filePath) throws IOException {
    ClassPathResource classPathResource = new ClassPathResource(filePath);
    return new String(readAll(classPathResource.getInputStream()), StandardCharsets.UTF_8);
  }

  public static byte[] readAll(InputStream inputStream) throws IOException {
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    int nRead;
    byte[] data = new byte[1024];
    while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
      buffer.write(data, 0, nRead);
    }
    buffer.flush();
    return buffer.toByteArray();
  }

  @Test
  @DisplayName("Verify updateServiceProvider() method")
  void testUpdateServiceProvider() throws Exception {

    IssuerConfiguration issuerConfiguration = new IssuerConfiguration();
    final String range = "5050,5051";
    issuerConfiguration.callbackUrl("https://confluence.mastercard.int/pages/viewpage.action?pageId=508046358aMkhe");
    issuerConfiguration.setSupportedAccountRange(range);

    MvcResult mvcResult = mockMvc.perform(put(UPDATE_USER).contentType("application/json").header(xOpenApiClientId, clientId)
      .content(new Gson().toJson(issuerConfiguration)))
      .andExpect(status().isOk()).andReturn();

    assertNotNull(mvcResult.getResponse().getContentAsString());


  }

  @Test
  @DisplayName("Test getIssuerDetails")
  void getIssuerDetails() throws Exception {

    MvcResult mvcResult = mockMvc.perform(get(GET_ISSUER)
      .contentType(applicationJson)).andExpect(status().isOk()).andReturn();

    String actual = mvcResult.getResponse().getContentAsString();

    IssuerProfileDetails dashboardResponse = gson.fromJson(actual, IssuerProfileDetails.class);
    // ASSERT
    assertNotNull(dashboardResponse);
    assertNotNull(dashboardResponse.getCurrencyCode());
    assertNotNull(dashboardResponse.getCountryCode());
    assertNotNull(dashboardResponse.getCountryName());
    assertNotNull(dashboardResponse.getClientId());

  }

  @Test
  @DisplayName("Test userRegistration")
  @Order(1)
  void userRegistration() throws Exception {

    String userRegisterJson = getFileAsString("user-registration-duplicate-user-request.json");

    MvcResult mvcResult = mockMvc.perform(post(ADD_USER)
      .contentType(applicationJson).header(xOpenApiClientId, clientId).content(userRegisterJson)).andExpect(status().isOk()).andReturn();

    String actual = mvcResult.getResponse().getContentAsString();


    UserReference userReference = gson.fromJson(actual, UserReference.class);
    // ASSERT
    assertNotNull(userReference);
    assertNotNull(userReference.getUserid());
//
    userId = userReference.getUserid();
    assertNotNull(userReference.getCardNumberLastFourDigits());
    assertNotNull(userReference.getStatus());

    assertEquals(200, mvcResult.getResponse().getStatus());
  }


}
