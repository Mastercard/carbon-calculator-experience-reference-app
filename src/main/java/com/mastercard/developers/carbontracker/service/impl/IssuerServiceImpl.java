package com.mastercard.developers.carbontracker.service.impl;

import com.mastercard.developers.carbontracker.exception.ServiceException;
import com.mastercard.developers.carbontracker.service.IssuerService;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.IssuerApi;
import org.openapitools.client.model.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import static com.mastercard.developers.carbontracker.util.JSON.deserializeErrors;

@Slf4j
@Service
public class IssuerServiceImpl implements IssuerService {

    private IssuerApi issuerApi;

    @Autowired
    public IssuerServiceImpl(ApiClient apiClient) throws ServiceException {
        log.info("Initializing Issuer Service");
        issuerApi = new IssuerApi(apiClient);
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
    public UserReference userRegistration(UserRegistrationInfo userRegistrationInfo) throws ServiceException {
        UserReference userReference;
        //TODO Encrytpion needs to be implemented

        try {
            userReference = issuerApi.userRegistration(new EncryptedPayload());
        } catch (ApiException e) {
            throw new ServiceException(e.getMessage(), deserializeErrors(e.getResponseBody()));
        }

        return userReference;
    }
}
