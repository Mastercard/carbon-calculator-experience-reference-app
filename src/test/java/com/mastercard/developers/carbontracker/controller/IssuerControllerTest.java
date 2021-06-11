package com.mastercard.developers.carbontracker.controller;


import com.google.gson.Gson;
import com.mastercard.developers.carbontracker.service.IssuerService;
import org.junit.jupiter.api.Test;
import org.openapitools.client.model.AggregateCarbonScore;
import org.openapitools.client.model.Dashboard;
import org.openapitools.client.model.UserReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.mastercard.developers.carbontracker.util.ServiceEndpoints.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class IssuerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @Autowired
    private IssuerService issuerService;

    @Value("${test.data.userid}")
    String userId;

    @Test
    void getDashboardUrl() throws Exception {


        MvcResult mvcResult = mockMvc.perform(get(DASHBOARDS, userId)
                .contentType("application/json")).andExpect(status().isOk()).andReturn();

        String actual = mvcResult.getResponse().getContentAsString();

        Dashboard dashboardResponse = gson.fromJson(actual, Dashboard.class);
        // ASSERT
        assertNotNull(dashboardResponse);
        assertNotNull(dashboardResponse.getExpiryInMillis());
        assertNotNull(dashboardResponse.getUrl());

    }

    @Test
    void getAggregateCarbonScore() throws Exception {

        MvcResult mvcResult = mockMvc.perform(get(AGGREGATE_CARBON_SCORE, userId)
                .contentType("application/json")).andExpect(status().isOk()).andReturn();

        String actual = mvcResult.getResponse().getContentAsString();

        AggregateCarbonScore aggregateCarbonScore = gson.fromJson(actual, AggregateCarbonScore.class);
        // ASSERT
        assertNotNull(aggregateCarbonScore);
        assertNotNull(aggregateCarbonScore.getCarbonEmissionInGrams());
        assertNotNull(aggregateCarbonScore.getAggregateDate());

    }

    @Test
    void userRegistration() throws Exception {

        String userProfile = "{\n" +
                "  \"email\": {\n" +
                "    \"type\": \"home\",\n" +
                "    \"value\": \"John.Doe@mail.com\"\n" +
                "  },\n" +
                "  \"name\": {\n" +
                "    \"firstName\": \"John\",\n" +
                "    \"lastName\": \"Doe\"\n" +
                "  },\n" +
                "  \"cardholderName\": \"John Doe\",\n" +
                "  \"cardNumber\": 5344035171229750,\n" +
                "  \"cardBaseCurrency\": \"EUR\",\n" +
                "  \"expiryInfo\": {\n" +
                "    \"month\": 10,\n" +
                "    \"year\": 2024\n" +
                "  },\n" +
                "  \"billingAddress\": {\n" +
                "    \"country\": \"USA\",\n" +
                "    \"locality\": \"Rly Station\",\n" +
                "    \"postalCode\": 11746,\n" +
                "    \"region\": \"Huntington\",\n" +
                "    \"streetAddress\": \"7832 West Elm Street\",\n" +
                "    \"type\": \"work\"\n" +
                "  },\n" +
                "  \"locale\": \"en-US\"\n" +
                "}";

        MvcResult mvcResult = mockMvc.perform(post(ADD_USER)
                .contentType("application/json").content(userProfile)).andExpect(status().isOk()).andReturn();

        String actual = mvcResult.getResponse().getContentAsString();

        UserReference userReference = gson.fromJson(actual, UserReference.class);
        // ASSERT
        assertNotNull(userReference);
        assertNotNull(userReference.getUserid());
        assertNotNull(userReference.getCardNumberLastFourDigits());
        assertNotNull(userReference.getStatus());
    }

}
