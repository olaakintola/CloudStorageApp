package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CredentialService {

    private final CredentialMapper credentialMapper;
    private final EncryptionService encryptionService;
    private final UserService userService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService, UserService userService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
        this.userService = userService;
    }

    public int addCredential(Credential credential, String username){
        User user = this.userService.getUser(username);
        Integer userid = user.getUserid();
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[16];
        secureRandom.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = this.encryptionService.encryptValue(credential.getPassword(), encodedKey);
        return credentialMapper.insertCredential(new Credential(null, credential.getUrl(), credential.getUsername(), encodedKey, encryptedPassword, userid.intValue() ));
    }

    public void updateCredential(Credential credential, String username){
        Integer credentialid = credential.getCredentialid();
        String url = credential.getUrl();
        String urlUsername = credential.getUsername();
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[16];
        secureRandom.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = this.encryptionService.encryptValue(credential.getPassword(), encodedKey);
        User user = this.userService.getUser(username);
        Integer userid = user.getUserid();
        this.credentialMapper.updateCredential(credentialid, url, urlUsername, encodedKey, encryptedPassword, userid);
    }

    public void deleteCredential(Integer credentialid){
        this.credentialMapper.deleteCredential(credentialid);
    }

    public List<Credential> displayAllCredentials(Integer userid){
        return credentialMapper.retrieveAllCredentials(userid);
    }

    public Map<Integer, String> getUnecryptedPassword(Integer userid){
        Map<Integer, String> passwordMap = new HashMap<>();
        List<Credential> credentialList = credentialMapper.retrieveAllCredentials(userid);
        for(Credential credential : credentialList){
            passwordMap.put(credential.getCredentialid(), this.encryptionService.decryptValue(credential.getPassword(), credential.getKey()) );
        }
        return passwordMap;
    }

}
