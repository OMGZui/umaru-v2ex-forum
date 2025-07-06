package com.example.v2exclone.util;

import com.example.v2exclone.dto.GitHubOAuth2UserInfo;
import com.example.v2exclone.dto.OAuth2UserInfo;
import com.example.v2exclone.entity.AuthProvider;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equalsIgnoreCase(AuthProvider.GITHUB.toString())) {
            return new GitHubOAuth2UserInfo(attributes);
        } else {
            throw new IllegalArgumentException("Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }
}
