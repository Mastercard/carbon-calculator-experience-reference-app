package com.mastercard.developers.carbontracker.service;

import org.openapitools.client.model.Contributions;
import org.openapitools.client.model.HistoricalTransactionFootprints;

public interface UserService {

    Contributions getPartnerStatsByDonorId();

    HistoricalTransactionFootprints getPaymentCardTransactionHistory();

}
