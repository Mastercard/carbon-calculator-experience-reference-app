package com.mastercard.developers.carbontracker.service;

import com.mastercard.developers.carbontracker.exception.ServiceException;
import org.openapitools.client.model.UserProfile;
import org.openapitools.client.model.UserReference;

public interface UserRegistrationService {

  UserReference userRegistration(UserProfile userProfile) throws ServiceException;

}
