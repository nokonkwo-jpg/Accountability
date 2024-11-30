package com.example.plaidbankapp.service;

import com.example.plaidbankapp.entity.Transaction;
import com.example.plaidbankapp.repo.TransactionRepository;
import com.plaid.client.ApiClient;
import com.plaid.client.model.*;
import com.plaid.client.request.PlaidApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PlaidService {
    private final PlaidApi plaidApi;
    @Autowired
    private final TransactionRepository transactionRepository;

    public PlaidService(@Value("${plaid.client_id}") String clientId,
                        @Value("${plaid.secret}") String secret,
                        @Value("${plaid.env}") String environment, TransactionRepository transactionRepository) {
        // Define the environment
        String basePath;
        switch (environment.toLowerCase()) {
            case "sandbox":
                basePath = ApiClient.Sandbox;
                break;
            case "development":
                basePath = "https://development.plaid.com";
                break;
            case "production":
                basePath = ApiClient.Production;
                break;
            default:
                throw new IllegalArgumentException("Invalid Plaid environment");
        }

        // Prepare API keys in a map
        Map<String, String> apiKeys = Map.of(
                "clientId", clientId,
                "secret", secret,
                "plaidVersion", "2020-09-14"
        );

        // Create ApiClient with the API keys map
        ApiClient apiClient = new ApiClient(apiKeys);
        apiClient.setPlaidAdapter(basePath);

        this.plaidApi = apiClient.createService(PlaidApi.class);
        this.transactionRepository = transactionRepository;
    }

    // Method to create a Link Token for Plaid Link
    public String createLinkToken(String clientUserId) throws Exception {
        LinkTokenCreateRequestUser user = new LinkTokenCreateRequestUser()
                .clientUserId(clientUserId);

        LinkTokenCreateRequest request = new LinkTokenCreateRequest()
                .user(user)
                .clientName("PlaidBankApp")
                .products(List.of(Products.AUTH, Products.TRANSACTIONS))
                .countryCodes(List.of(CountryCode.US))
                .language("en");

        Response<LinkTokenCreateResponse> response = plaidApi.linkTokenCreate(request).execute();
        if (response.isSuccessful() && response.body() != null) {
            return response.body().getLinkToken();
        } else {
            throw new RuntimeException("Failed to create link token: " +
                    (response.errorBody() != null ? response.errorBody().string() : "Unknown error"));
        }
    }

    // Method to exchange the public token for an access token
    public String exchangePublicToken(String publicToken) throws Exception {
        ItemPublicTokenExchangeRequest request = new ItemPublicTokenExchangeRequest()
                .publicToken(publicToken);

        Response<ItemPublicTokenExchangeResponse> response = plaidApi.itemPublicTokenExchange(request).execute();
        if (response.isSuccessful() && response.body() != null) {
            return response.body().getAccessToken();
        } else {
            throw new RuntimeException("Failed to exchange public token: " +
                    (response.errorBody() != null ? response.errorBody().string() : "Unknown error"));
        }
    }

    // Method to get account info using the access token
    public Map<String, Object> getAccountInfo(String accessToken) throws Exception {
        AccountsGetRequest request = new AccountsGetRequest()
                .accessToken(accessToken);

        Response<AccountsGetResponse> response = plaidApi.accountsGet(request).execute();
        if (response.isSuccessful() && response.body() != null) {
            return Map.of("accounts", response.body().getAccounts());
        } else {
            throw new RuntimeException("Failed to get account info: " +
                    (response.errorBody() != null ? response.errorBody().string() : "Unknown error"));
        }
    }

    // Method to get transactions using the access token
    public Map<String, Object> getTransactions(String accessToken) throws Exception {
        TransactionsGetRequest request = new TransactionsGetRequest()
                .accessToken(accessToken)
                .startDate(LocalDate.now().minusMonths(3)) // Fetch transactions for the last 3 months
                .endDate(LocalDate.now());

        Response<TransactionsGetResponse> response = plaidApi.transactionsGet(request).execute();
        if (response.isSuccessful() && response.body() != null) {
            //Save transactions to DB
            List<Transaction> transactionsList = response.body().getTransactions().stream().map(transaction -> {
                Transaction tx = new Transaction();
                tx.setPlaidTransactionId(transaction.getTransactionId());
                tx.setAmount(transaction.getAmount());
                tx.setCategory(String.valueOf(transaction.getCategory()));
                tx.setDate(transaction.getDate());
                tx.setName(transaction.getName());
                return tx;
            })
                    .toList();

            transactionRepository.saveAll(transactionsList);

            return Map.of("transactions", response.body().getTransactions());
        } else {
            throw new RuntimeException("Failed to get transactions: " +
                    (response.errorBody() != null ? response.errorBody().string() : "Unknown error"));
        }
    }

    // Method to get liabilities using the access token
    /**public Map<String, Object> getLiabilities(String accessToken) throws Exception {
        LiabilitiesGetRequest request = new LiabilitiesGetRequest()
                .accessToken(accessToken);

        Response<LiabilitiesGetResponse> response = plaidApi.liabilitiesGet(request).execute();
        if (response.isSuccessful() && response.body() != null) {
            return Map.of("liabilities", response.body().getLiabilities());
        } else {
            throw new RuntimeException("Failed to get liabilities: " +
                    (response.errorBody() != null ? response.errorBody().string() : "Unknown error"));
        }
    }*/
}

