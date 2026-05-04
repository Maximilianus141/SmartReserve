package ch.kenner.maximilian.smartreserve.controller;

import ch.kenner.maximilian.smartreserve.base.MessageResponse;
import ch.kenner.maximilian.smartreserve.model.Service;
import ch.kenner.maximilian.smartreserve.model.ServiceRepository;


import java.util.List;


@org.springframework.stereotype.Service
public class ServiceService {

    private final ServiceRepository serviceRepository;

    public ServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public List<Service> getAllServices() {
        return serviceRepository.findAll();
    }

    public Service getServiceById(Long id) {
        return serviceRepository.findById(id).orElse(null);
    }

    public Service insertService(Service service) {
        return serviceRepository.save(service);
    }

    public Service updateservice(Service service, Long id) {
        return serviceRepository.findById(id)
                .map(departmentOrig -> {
                    departmentOrig.setName(service.getName());
                    return serviceRepository.save(departmentOrig);
                })
                .orElseGet(() -> serviceRepository.save(service));
    }

    public MessageResponse deleteService(Long id) {
        serviceRepository.deleteById(id);
        return new MessageResponse("Service " + id + " deleted");
    }
}
