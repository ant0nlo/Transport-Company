package main.mapper;

import main.dto.TransportDTO;
import main.entity.Transport;

public class TransportMapper {

    // Метод за преобразуване от Transport към TransportDTO
    public static TransportDTO toDTO(Transport transport) {
        if (transport == null) {
            return null;
        }

        return new TransportDTO(
                transport.getStartLocation(),
                transport.getEndLocation(),
                transport.getDepartureDate(),
                transport.getArrivalDate(),
                transport.getCargoDescription(),
                transport.getCargoWeight(),
                transport.getPrice(),
                transport.isPaid()
        );
    }

    // Метод за преобразуване от TransportDTO към Transport
    public static Transport toEntity(TransportDTO transportDTO) {
        if (transportDTO == null) {
            return null;
        }

        Transport transport = new Transport();
        transport.setStartLocation(transportDTO.getStartLocation());
        transport.setEndLocation(transportDTO.getEndLocation());
        transport.setDepartureDate(transportDTO.getDepartureDate());
        transport.setArrivalDate(transportDTO.getArrivalDate());
        transport.setCargoDescription(transportDTO.getCargoDescription());
        transport.setCargoWeight(transportDTO.getCargoWeight());
        transport.setPrice(transportDTO.getPrice());
        transport.setPaid(transportDTO.isPaid());

        return transport;
    }

    public static void updateEntityFromDTO(TransportDTO transportDTO, Transport transport) {
        transport.setStartLocation(transportDTO.getStartLocation());
        transport.setEndLocation(transportDTO.getEndLocation());
        transport.setDepartureDate(transportDTO.getDepartureDate());
        transport.setArrivalDate(transportDTO.getArrivalDate());
        transport.setCargoDescription(transportDTO.getCargoDescription());
        transport.setCargoWeight(transportDTO.getCargoWeight());
        transport.setPrice(transportDTO.getPrice());
        transport.setPaid(transportDTO.isPaid());

    }
}