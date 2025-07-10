package com.techzo.cambiazo.exchanges.application.internal.commandservices;

import com.techzo.cambiazo.exchanges.domain.model.commands.CreateExchangeCommand;
import com.techzo.cambiazo.exchanges.domain.model.commands.UpdateExchangeStatusCommand;
import com.techzo.cambiazo.exchanges.domain.model.entities.Exchange;
import com.techzo.cambiazo.exchanges.domain.model.entities.Product;
import com.techzo.cambiazo.exchanges.domain.services.IExchangeCommandService;
import com.techzo.cambiazo.exchanges.infrastructure.persistence.jpa.IExchangeRepository;
import com.techzo.cambiazo.exchanges.infrastructure.persistence.jpa.IProductRepository;
import com.techzo.cambiazo.lockers.domain.model.commands.CreateExchangeLockerCommand;
import com.techzo.cambiazo.lockers.domain.model.entities.ExchangeLocker;
import com.techzo.cambiazo.lockers.domain.model.entities.Location;
import com.techzo.cambiazo.lockers.domain.services.IExchangeLockerCommandService;
import com.techzo.cambiazo.lockers.infrastructure.persistence.jpa.ILocationRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ExchangeCommandServiceImpl implements IExchangeCommandService {

    private final IExchangeRepository exchangeRepository;

    private final IProductRepository productRepository;

    private final ILocationRepository locationRepository;

    private final IExchangeLockerCommandService exchangeLockerCommandService;


    public ExchangeCommandServiceImpl(IExchangeRepository exchangeRepository, IProductRepository productRepository, ILocationRepository locationRepository, IExchangeLockerCommandService exchangeLockerCommandService) {
        this.exchangeRepository = exchangeRepository;
        this.productRepository = productRepository;
        this.locationRepository = locationRepository;
        this.exchangeLockerCommandService = exchangeLockerCommandService;
    }

    @Override
    public Optional<Exchange>handle(CreateExchangeCommand command) {

        Product productOwn= productRepository.findById(command.productOwnId()).orElseThrow(() -> new IllegalArgumentException("Product Own not found"));
        Product productChange = productRepository.findById(command.productChangeId()).orElseThrow(() -> new IllegalArgumentException("Product Change not found"));
        var location = locationRepository.existsById(command.locationId());
        if (!location) {
            throw new IllegalArgumentException("Location not exists");
        }

        var result=exchangeRepository.findExchangeByProductOwnIdAndProductChangeId(productOwn, productChange);
        if(result!=null){
            throw new IllegalArgumentException("Exchange already exists");
        }
        var exchange = new Exchange(command, productOwn, productChange);
        var createdExchange = exchangeRepository.save(exchange);
        return Optional.of(createdExchange);
    }

    @Override
    public Optional<Exchange>handle(UpdateExchangeStatusCommand command){
        var result = exchangeRepository.findById(command.id());
        if (result.isEmpty()) {
            throw new IllegalArgumentException("Exchange does not exist");
        }
        var exchangeToUpdate = result.get();
        try {
            Exchange exchange = exchangeRepository.findById(command.id())
                    .orElseThrow(() -> new IllegalArgumentException("Exchange not found"));

            Product productOwn = productRepository.findById(exchange.getProductOwnId())
                    .orElseThrow(() -> new IllegalArgumentException("Product Own not found"));

            Product productChange = productRepository.findById(exchange.getProductChangeId())
                    .orElseThrow(() -> new IllegalArgumentException("Product Change not found"));

            var updatedExchange = exchangeRepository.save(exchangeToUpdate.updateInformation(productOwn, productChange, command.status()));

            if("Aceptado".equals(command.status())){
                exchange.setExchangeDate(java.time.LocalDate.now());
                productOwn.setAvailable(false);
                productChange.setAvailable(false);
                productRepository.save(productOwn);
                productRepository.save(productChange);
                List<ExchangeLocker> lockers = exchangeLockerCommandService.autoGenerateExchangeLockers(exchange.getUserOwnId(), exchange.getUserChangeId(),exchange.getId());
                if(lockers.isEmpty()){
                    throw new IllegalArgumentException("No lockers generated for the exchange");
                }
            }
            //

            exchangeRepository.updateExchangeStatusToRejectedByProductOwnExcept(
                    productOwn.getId(), exchange.getId());

            exchangeRepository.updateExchangeStatusToRejectedByProductChangeExcept(
                    productChange.getId(), exchange.getId());


            return Optional.of(updatedExchange);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error updating exchange:" + e.getMessage());
        }
    }

    @Override
    public boolean handleDeleteExchange(Long id){
        Optional<Exchange>exchange = exchangeRepository.findById(id);
        if(exchange.isPresent() && exchange.get().getStatus().equals("Pendiente")){
            exchangeRepository.delete(exchange.get());
            return true;
        }else {
            throw new IllegalArgumentException("Exchange not found");
        }
    }
}
