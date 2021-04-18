package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredentialListService {
    private final CredentialMapper credentialMapper;

    public CredentialListService(CredentialMapper credentialMapper) {
        this.credentialMapper = credentialMapper;
    }

    public void addCredential(Credential credential) {
        credentialMapper.insert(credential);
    }

    public List<Credential> getCredentials(int userId) {
        return credentialMapper.getAllCredentialsByUser(userId);
    }

    public int deleteCredential(int userId, int credentialId) {
        return credentialMapper.deleteCredentialByCredentialId(userId, credentialId);
    }

    public int updateCredential(Credential credential) {
        return credentialMapper.update(credential);
    }
}
