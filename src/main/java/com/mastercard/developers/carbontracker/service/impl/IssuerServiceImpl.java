package com.mastercard.developers.carbontracker.service.impl;

import com.mastercard.developer.interceptors.OkHttpFieldLevelEncryptionInterceptor;
import com.mastercard.developer.interceptors.OkHttpOAuth1Interceptor;
import com.mastercard.developers.carbontracker.configuration.ApiConfiguration;
import com.mastercard.developers.carbontracker.exception.ServiceException;
import com.mastercard.developers.carbontracker.service.IssuerService;
import com.mastercard.developers.carbontracker.util.EncryptionHelper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.IssuerApi;
import org.openapitools.client.model.AggregateCarbonScore;
import org.openapitools.client.model.Dashboard;
import org.openapitools.client.model.IssuerConfiguration;
import org.openapitools.client.model.IssuerProfile;
import org.openapitools.client.model.IssuerProfileDetails;
import org.openapitools.client.model.UserProfile;
import org.openapitools.client.model.UserReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mastercard.developers.carbontracker.util.JSON.deserializeErrors;

@Slf4j
@Service
public class IssuerServiceImpl implements IssuerService {

    private IssuerApi issuerApi;
    private IssuerApi issuerApiforUpdate;

    @Autowired
    public IssuerServiceImpl(ApiConfiguration apiConfiguration) throws ServiceException {
        log.info("Initializing Issuer Service");
        issuerApi = new IssuerApi(setup(apiConfiguration));
        issuerApiforUpdate = new IssuerApi(setupForUpdateUser(apiConfiguration));
    }

     private ApiClient setup(ApiConfiguration apiConfiguration) throws ServiceException {
        OkHttpClient client = new OkHttpClient().newBuilder().
                addInterceptor(
                new OkHttpFieldLevelEncryptionInterceptor(

                        EncryptionHelper.encryptionConfig(apiConfiguration.getEncryptionKeyFile()))).
                addInterceptor(
                new OkHttpOAuth1Interceptor(apiConfiguration.getConsumerKey(), apiConfiguration.getSigningKey()))

                .build();

        return new ApiClient().setHttpClient(client).setBasePath(apiConfiguration.getBasePath());
    }

    private ApiClient setupForUpdateUser(ApiConfiguration apiConfiguration) throws ServiceException {
        OkHttpClient client = new OkHttpClient().newBuilder().

        addInterceptor(
        new OkHttpOAuth1Interceptor(apiConfiguration.getConsumerKey(), apiConfiguration.getSigningKey()))

                .build();

        return new ApiClient().setHttpClient(client).setBasePath(apiConfiguration.getBasePath());
    }

    @Override
    public Dashboard getAuthToken(String userPpctId) throws ServiceException {
        Dashboard dashboard;
        try {
            dashboard = issuerApi.getAuthToken(userPpctId);
        } catch (ApiException e) {
            throw new ServiceException(e.getMessage(), deserializeErrors(e.getResponseBody()));
        }
        return dashboard;
    }

    @Override
    public AggregateCarbonScore getAggregateCarbonScore(String userPpctId) throws ServiceException {

        AggregateCarbonScore aggregateCarbonScore;
        try {
            aggregateCarbonScore = issuerApi.getAggregateCarbonScore(userPpctId);
        } catch (ApiException e) {
            throw new ServiceException(e.getMessage(), deserializeErrors(e.getResponseBody()));
        }

        return aggregateCarbonScore;
    }

    @Override
    public UserReference userRegistration(UserProfile userProfile) throws ServiceException {
        UserReference userReference;
        try {
            userReference = issuerApi.userRegistration(userProfile);
        } catch (ApiException e) {
            throw new ServiceException(e.getMessage(), deserializeErrors(e.getResponseBody()));
        }

        return userReference;
    }

    @Override
    public ResponseEntity<List<String>> deleteUsers(List<String> requestBody) throws ServiceException {
        List<String> deletedUsers;
        try {
            deletedUsers = issuerApiforUpdate.deleteUsers(requestBody);

        } catch (ApiException e) {
            throw new ServiceException(e.getMessage(), deserializeErrors(e.getResponseBody()));
        }
        return new ResponseEntity<>(deletedUsers, HttpStatus.ACCEPTED);
    }


    @Override
    public IssuerProfile updateIssuer(IssuerConfiguration issuerConfiguration) throws ServiceException {
            IssuerProfile profile ;
            try {
                profile = issuerApiforUpdate.updateIssuer(issuerConfiguration);

            } catch (ApiException e) {
                throw new ServiceException(e.getMessage(), deserializeErrors(e.getResponseBody()));

            }
            return profile;
        }
    @Override
    public IssuerProfileDetails getIssuer() throws ServiceException {

        IssuerProfileDetails issuerProfileDetails;
        try {
            issuerProfileDetails = issuerApiforUpdate.getIssuer();
        } catch (ApiException e) {
            throw new ServiceException(e.getMessage(), deserializeErrors(e.getResponseBody()));
        }

        return issuerProfileDetails;
    }
}
