package com.mastercard.developers.carbontracker.util;

public class ServiceEndpoints {

	public static final String CONTEXT_PATH = "/cts";

	public static final String ADD_USER = CONTEXT_PATH + "/issuers/users";
	public static final String AGGREGATE_CARBON_SCORE = CONTEXT_PATH + "/issuers/users/{userid}/aggregate-carbon-scores";
	public static final String DASHBOARDS = CONTEXT_PATH + "/issuers/users/{userid}/dashboards";
	public static final String DELETE_USER = CONTEXT_PATH + "/issuers/user-deletions";
	public static final String UPDATE_USER = CONTEXT_PATH + "/issuers";
	public static final String GET_ISSUER = CONTEXT_PATH + "/issuers";
}
