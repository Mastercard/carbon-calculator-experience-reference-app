package com.mastercard.developers.carbontracker.usecases;

import com.mastercard.developers.carbontracker.exception.ServiceException;
import com.mastercard.developers.carbontracker.service.GetDashboardService;
import com.mastercard.developers.carbontracker.service.IssuerService;
import com.mastercard.developers.carbontracker.service.UserRegistrationService;
import com.mastercard.developers.carbontracker.util.CreditCardGenerator;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.openapitools.client.model.Address;
import org.openapitools.client.model.AggregateCarbonScore;
import org.openapitools.client.model.CardExpiry;
import org.openapitools.client.model.Email;
import org.openapitools.client.model.IssuerConfiguration;
import org.openapitools.client.model.IssuerProfile;
import org.openapitools.client.model.IssuerProfileDetails;
import org.openapitools.client.model.UserName;
import org.openapitools.client.model.UserProfile;
import org.openapitools.client.model.UserReference;
import org.openapitools.client.model.Dashboard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class IssuerControllerUseCase {

    private final IssuerService issuerService;

    private final UserRegistrationService userRegistrationService;

    private final GetDashboardService getDashboardService;

    @Value("${binRange}")
    private String binRange;

    @Value("${lang}")
    private String lang;

    @Autowired
    public IssuerControllerUseCase(IssuerService issuerService, UserRegistrationService userRegistrationService, GetDashboardService getDashboardService) {
        this.issuerService = issuerService;
        this.userRegistrationService = userRegistrationService;
        this.getDashboardService = getDashboardService;
    }

    public void b2BCalls() {

        log.info("Fetching Issuer details for given client");
        getIssuerDetail();
        updateIssuerDetails();
        String userId = userRegistration();
        getAggregateScores(userId);
        getDashboardUrl(userId, lang);
        deleteUser(userId);

    }

    private void getIssuerDetail() {
        try {
            log.info("Fetching issuer details ");
            IssuerProfileDetails issuerProfileDetails = issuerService.getIssuer();
            log.info("Response received for get Issuer details {} ", issuerProfileDetails);
        } catch (ServiceException e) {
            log.info("Exception while fetching issuer details" + e.getMessage());
        }
    }

    private void updateIssuerDetails() {
        try {
            log.info("Updating issuer details ");
            IssuerConfiguration issuerConfiguration = new IssuerConfiguration();
            issuerConfiguration.setSupportedAccountRange("5051");
            IssuerProfile issuerProfile = issuerService.updateIssuer(issuerConfiguration);
            log.info("Response received after updating the issuer {}", issuerProfile);
        } catch (ServiceException e) {
            log.info("Exception while updating issuer details" + e.getMessage());
        }
    }

    private String userRegistration() {
        String userid = null;
        try {
            log.info("Creating a new User");
            UserReference userReference = userRegistrationService.userRegistration(getUserProfile());
            if (userReference != null) {
                userid = userReference.getUserid();
                log.info("Response for User creation is :{}", userReference);
            }
        } catch (ServiceException ex) {
            log.info("Exception occurred while creating a new user {}", ex.getServiceErrors());
        }

        return userid;
    }

    public void getAggregateScores(String userId) {
        log.info("Fetching Aggregate Scores for given userId {}", userId);
        try {
            AggregateCarbonScore aggregateCarbonScore = issuerService.getAggregateCarbonScore(userId);
            log.info("Aggregate Carbon score for userID {} is {}", userId, aggregateCarbonScore);
        } catch (ServiceException ex) {
            log.info("Exception occurred while fetching aggregate scores :{}", ex.getServiceErrors());
        }
    }

    public void getDashboardUrl(String userId, String lang) {
        log.info("Fetching DashBoard url for user id {}", userId);
        try {
            Dashboard dashboard = getDashboardService.getAuthToken(userId, lang);
            log.info("Dashboard url for given userId {} is {} ", userId, dashboard);
        } catch (ServiceException ex) {
            log.info("Exception occuered while calling get Dashboard url for userID {} is {} ", userId, ex.getServiceErrors());
        }

    }

    public void deleteUser(String userId) {
        log.info("Deleting user for given user {} ", userId);
        try {
            List<String> list = new ArrayList<>();
            list.add(userId);
            ResponseEntity<List<String>> response = issuerService.deleteUsers(list);
            log.info("Response code received for deleting the user is {}", response.getStatusCode());
        } catch (ServiceException ex) {
            log.info("Exception occurred while deleting the given user " + ex.getServiceErrors());
        }
    }

    public UserProfile getUserProfile() {
        UserProfile userProfile = new UserProfile();

        Email email = new Email();
        email.setType("home");
        email.setValue("PPCT" + new RandomString().nextString() + "@gmail.com");
        userProfile.setEmail(email);
        UserName userName = new UserName();
        userName.setFirstName("dummmyUserrz");
        userName.setLastName("Useraf");
        userProfile.setName(userName);

        userProfile.setCardholderName("dummyaqa User");
        userProfile.setCardBaseCurrency("USD");
        String cardNumber = new CreditCardGenerator().generate(binRange, 16);
        userProfile.setCardNumber(cardNumber);

        CardExpiry cardExpiry = new CardExpiry();
        cardExpiry.setMonth("09");
        cardExpiry.setYear("2024");
        Address address = new Address();

        address.setCountry("USA");
        address.setLocality("rly station");
        address.setPostalCode("11746");
        address.setRegion("Huntington");
        address.setStreetAddress("7832 West Elm Street");
        address.setType("work");
        userProfile.setBillingAddress(address);
        userProfile.setExpiryInfo(cardExpiry);
        userProfile.setLocale("en-US");

        return userProfile;
    }

}
