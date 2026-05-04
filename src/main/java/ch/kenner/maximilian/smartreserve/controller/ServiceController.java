package ch.kenner.maximilian.smartreserve.controller;

import ch.kenner.maximilian.smartreserve.base.MessageResponse;
import ch.kenner.maximilian.smartreserve.model.Service;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ServiceController {
    private final ServiceService serviceService;

    ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @GetMapping("api/service")
    public ResponseEntity<List<Service>> getAllServices() {
        return ResponseEntity.ok(serviceService.getAllServices());
    }

    @GetMapping("api/service/{id}")
    public ResponseEntity<Service> getServiceById(@PathVariable Long id) {
        return ResponseEntity.ok(serviceService.getServiceById(id));
    }

    @PostMapping("api/service")
    public ResponseEntity<Service> postService(@Valid @RequestBody Service service) {
        return ResponseEntity.ok(serviceService.insertService(service));
    }

    @DeleteMapping("api/service/{id}")
    public ResponseEntity<MessageResponse> deleteService(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(serviceService.deleteService(id));
        } catch (Throwable t) {
            return ResponseEntity.internalServerError().build();
        }
    }


}
