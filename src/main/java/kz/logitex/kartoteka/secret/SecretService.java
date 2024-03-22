package kz.logitex.kartoteka.secret;

import kz.logitex.kartoteka.exception.BadRequestException;
import kz.logitex.kartoteka.exception.NotFoundException;
import kz.logitex.kartoteka.model.Secret;
import kz.logitex.kartoteka.repository.SecretRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SecretService {
    private final SecretRepository secretRepository;

    public Secret createSecret(Secret request) {
        var existingSecret = secretRepository.findByName(request.getName());
        if (existingSecret.isPresent())
            throw new BadRequestException("Данная гриф секретность уже существует.");
        return secretRepository.save(request);
    }

    public Secret updateSecret(Long id, Secret request) {
        var secret = secretRepository.findById(id).orElseThrow(() -> new NotFoundException("Гриф секретность не найден с айди: " + id));
        var existingSecret = secretRepository.findByName(request.getName());
        if (existingSecret.isPresent())
            throw new BadRequestException("Данная гриф секретность уже существует.");
        secret.setName(request.getName());
        return secretRepository.save(secret);
    }

    public List<Secret> getAllSecrets() {
        return secretRepository.findAll();
    }

    public Secret getSecretById(Long id) {
        return secretRepository.findById(id).orElseThrow(() -> new NotFoundException("Гриф секретность не найден с айди: " + id));
    }
}

