package com.example.plaidbankapp.controller;

import com.example.plaidbankapp.service.PlaidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
public class PlaidController {

    @Autowired
    private PlaidService plaidService;

    // Endpoint to create the link token
    @GetMapping("/api/create-link-token")
    public Map<String, String> createLinkToken() throws Exception {
        String linkToken = plaidService.createLinkToken("unique_user_id");
        return Collections.singletonMap("link_token", linkToken);
    }

    // Endpoint to exchange the public token for an access token
    @PostMapping("/api/exchange-public-token")
    public Map<String, String> exchangePublicToken(@RequestBody Map<String, String> requestBody) throws Exception {
        String publicToken = requestBody.get("public_token");
        String accessToken = plaidService.exchangePublicToken(publicToken);
        return Collections.singletonMap("access_token", accessToken);
    }

    // Endpoint to fetch user account info
    @PostMapping("/api/get-account-info")
    public Map<String, Object> getAccountInfo(@RequestBody Map<String, String> requestBody) throws Exception {
        String accessToken = requestBody.get("access_token");
        return plaidService.getAccountInfo(accessToken);
    }

    // Endpoint to fetch user transactions
    @PostMapping("/api/get-transactions")
    public Map<String, Object> getTransactions(@RequestBody Map<String, String> requestBody) throws Exception {
        String accessToken = requestBody.get("access_token");
        return plaidService.getTransactions(accessToken);
    }

    // Endpoint to fetch user liabilities
    @PostMapping("/api/get-liabilities")
    public Map<String, Object> getLiabilities(@RequestBody Map<String, String> requestBody) throws Exception {
        String accessToken = requestBody.get("access_token");
        return plaidService.getLiabilities(accessToken);
    }
}
