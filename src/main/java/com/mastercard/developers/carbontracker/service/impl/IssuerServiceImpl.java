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
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.openapitools.client.model.AggregateCarbonScore;
import org.openapitools.client.model.Dashboard;
import org.openapitools.client.model.UserReference;
import org.openapitools.client.model.UserProfile;

import static com.mastercard.developers.carbontracker.util.JSON.deserializeErrors;

@Slf4j
@Service
public class IssuerServiceImpl implements IssuerService {

    private IssuerApi issuerApi;

    @Autowired
    public IssuerServiceImpl(ApiConfiguration apiConfiguration) throws ServiceException {
        log.info("Initializing Issuer Service");
        issuerApi = new IssuerApi(setup(apiConfiguration));
    }

     private ApiClient setup(ApiConfiguration apiConfiguration) throws ServiceException {
        OkHttpClient client = new OkHttpClient().newBuilder().addInterceptor(
                new OkHttpFieldLevelEncryptionInterceptor(
                        EncryptionHelper.encryptionConfig(apiConfiguration.getEncryptionKeyFile()))).addInterceptor(
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
}
