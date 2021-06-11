package com.mastercard.developers.carbontracker.service;

import com.mastercard.developers.carbontracker.exception.ServiceException;
import org.openapitools.client.model.AggregateCarbonScore;
import org.openapitools.client.model.Dashboard;
import org.openapitools.client.model.UserReference;
import org.openapitools.client.model.UserProfile;

public interface IssuerService {

    Dashboard getAuthToken(String userPpctId) throws ServiceException;

    AggregateCarbonScore getAggregateCarbonScore(String userPpctId) throws ServiceException;

    UserReference userRegistration (UserProfile userProfile) throws ServiceException;

}
