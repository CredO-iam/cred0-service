package io.cred0.core.clients.api;

import java.util.List;

import io.cred0.core.clients.mappers.RegisteredClientDtoMapper;
import io.cred0.core.clients.service.JpaRegisteredClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/registered-clients")
@RequiredArgsConstructor
public class RegisteredClientController {

    private final JpaRegisteredClientRepository registeredClientRepository;


    @GetMapping
    public RegisteredClientsResponse findAll() {
        List<RegisteredClientDto> clients = this.registeredClientRepository.findAll()
                .stream()
                .map(RegisteredClientDtoMapper::toDto)
                .toList();
        return new RegisteredClientsResponse(clients);
    }

    @GetMapping("/{id}")
    public RegisteredClientDto findById(@PathVariable String id) {
        var client = this.registeredClientRepository.findById(id);
        if (client == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found: " + id);
        }
        return RegisteredClientDtoMapper.toDto(client);
    }

    @GetMapping("/by-client-id/{clientId}")
    public RegisteredClientDto findByClientId(@PathVariable String clientId) {
        var client = this.registeredClientRepository.findByClientId(clientId);
        if (client == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found: " + clientId);
        }
        return RegisteredClientDtoMapper.toDto(client);
    }

    @PostMapping
    public RegisteredClientDto save(@RequestBody RegisteredClientDto request) {
        var registeredClient = RegisteredClientDtoMapper.toRegisteredClient(request);
        this.registeredClientRepository.save(registeredClient);
        var persisted = this.registeredClientRepository.findById(registeredClient.getId());
        if (persisted == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Client was not persisted");
        }
        return RegisteredClientDtoMapper.toDto(persisted);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable String id) {
        this.registeredClientRepository.deleteById(id);
    }

}

