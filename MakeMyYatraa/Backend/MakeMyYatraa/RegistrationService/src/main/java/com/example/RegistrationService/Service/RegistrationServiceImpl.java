package com.example.RegistrationService.Service;

import com.example.RegistrationService.Domain.User;
import com.example.RegistrationService.Encryption.UPISecurity;
import com.example.RegistrationService.Producer.Producer;
import com.example.RegistrationService.Rabitmq.Domain.UserDTO;
import com.example.RegistrationService.Repository.RegistrationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
@Service
public class RegistrationServiceImpl implements RegistrationService{
    private RegistrationRepository repository;
    @Autowired
    private Producer producer;
    UPISecurity upiSecurity = new UPISecurity();
    final SecretKey key = upiSecurity.genrateKey();
@Autowired
    public RegistrationServiceImpl(RegistrationRepository repository) throws NoSuchAlgorithmException {
        this.repository = repository;
    }

    @Override
    public User registerUser(User user) throws Exception {
        String userId1 = uniqueAlphaNumeric(10);
        String pass = upiSecurity.encrypt(user.getPassword(), key);
        user.setUserId(userId1);
        user.setPassword(pass);
        if (!user.isOwner()) {
            return repository.save(user);
        } else {
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(user.getUserId());
            userDTO.setOwner(user.isOwner());
            userDTO.setEmail(user.getEmail());
            userDTO.setName1(user.getName1());
            userDTO.setMobNo(user.getMobNo());
            userDTO.setPassword(user.getPassword());
            userDTO.setCity(user.getCity());
            repository.save(user);
            producer.sendMessageToRabbitMq(userDTO);
        }
        return user;
    }
    @Override
    public User findUser(String userId,String password) throws Exception {
    if (repository.findById(userId).isEmpty()) {
        throw new RuntimeException();
    } else {
        User user = repository.findByUserId(userId);
        String password1 = user.getPassword();
        String pass = password.replaceAll("\"","");
        String password2 = upiSecurity.encrypt(pass,key);
        if (password1.equals(password2)) {
            return user;
        }else
        return null;
        }
    }
    private String alphaNumericRandom(int length) {
        final String s = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder stringBuilder = new StringBuilder(length);
        SecureRandom random = new SecureRandom();
        for (int i=0;i<length;i++) {
            int x = random.nextInt(s.length());
            char c = s.charAt(x);
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }
    private String uniqueAlphaNumeric(int length) {
        final String s = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder stringBuilder = new StringBuilder(length);
        UUID uuid = UUID.randomUUID();
        stringBuilder.append(uuid.toString().replace("-",""));
        for (int i=0;i<=length;i++) {
            int x = (int) (Math.random()*s.length());
            char c = s.charAt(x);
            stringBuilder.append(c);
        }
        stringBuilder.setLength(length);
        return stringBuilder.toString().toUpperCase();
    }

}
