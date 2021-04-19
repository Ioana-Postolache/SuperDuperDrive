package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialListService {
    private final CredentialMapper credentialMapper;
    private final EncryptionService encryptionService;

    public CredentialListService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public void addCredential(Credential credential) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);
        credential.setPassword(encryptedPassword);
        credential.setKey(encodedKey);
        credentialMapper.insert(credential);
    }

    public List<Credential> getCredentials(int userId) {
        return credentialMapper.getAllCredentialsByUser(userId);
    }

    public int deleteCredential(int userId, int credentialId) {
        return credentialMapper.deleteCredentialByCredentialId(userId, credentialId);
    }

    public String getCredentialKey(int userId, int credentialId) {
        return credentialMapper.getCredentialKey(userId, credentialId);
    }

    public int updateCredential(Credential credential) {
        String key = getCredentialKey(credential.getUserId(), credential.getCredentialId());
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), key);
        credential.setPassword(encryptedPassword);
        return credentialMapper.update(credential);
    }
}
