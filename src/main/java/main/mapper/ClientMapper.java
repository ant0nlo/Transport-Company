package main.mapper;

import main.dto.ClientDTO;
import main.entity.Client;


public class ClientMapper {

    // Преобразуване от Entity към DTO
    public static ClientDTO toDTO(Client client) {
        return new ClientDTO(
                client.getName(),
                client.getContactInfo()
        );
    }

    // Преобразуване от DTO към Entity
    public static Client toEntity(ClientDTO dto){
        Client client = new Client();
        client.setName(dto.getName());
        client.setContactInfo(dto.getContactInfo());
        return client;
    }

    public static void updateEntityFromDTO(ClientDTO dto, Client existingClient) {
        existingClient.setName(dto.getName());
        existingClient.setContactInfo(dto.getContactInfo());
    }
}