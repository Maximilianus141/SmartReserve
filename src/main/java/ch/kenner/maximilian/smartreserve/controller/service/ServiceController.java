package ch.kenner.maximilian.smartreserve.controller.service;

import ch.kenner.maximilian.smartreserve.base.MessageResponse;
import ch.kenner.maximilian.smartreserve.model.service.Service;
import ch.kenner.maximilian.smartreserve.security.Roles;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SecurityRequirement(name = "bearerAuth")
@Validated
public class ServiceController {
    private final ServiceService serviceService;

    ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @GetMapping("api/service")
    @RolesAllowed(Roles.Guest)
    public ResponseEntity<List<Service>> getAllServices() {
        return ResponseEntity.ok(serviceService.getAllServices());
    }

    @GetMapping("api/service/{id}")
    @RolesAllowed(Roles.Guest)
    public ResponseEntity<Service> getServiceById(@PathVariable Long id) {
        return ResponseEntity.ok(serviceService.getServiceById(id));
    }

    @PostMapping("api/service")
    @RolesAllowed(Roles.Admin)
    public ResponseEntity<Service> postService(@Valid @RequestBody Service service) {
        return ResponseEntity.ok(serviceService.insertService(service));
    }

    @PutMapping("api/service/{id}")
    @RolesAllowed(Roles.Admin)
    public ResponseEntity<Service> putService(@PathVariable Long id, @Valid @RequestBody Service service) {
        return ResponseEntity.ok(serviceService.updateservice(service, id));
    }

    @DeleteMapping("api/service/{id}")
    @RolesAllowed(Roles.Admin)
    public ResponseEntity<MessageResponse> deleteService(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(serviceService.deleteService(id));
        } catch (Throwable t) {
            return ResponseEntity.internalServerError().build();
        }
    }


}
