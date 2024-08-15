package mg.edbm.mail.controller;

import lombok.RequiredArgsConstructor;
import mg.edbm.mail.config.SecurityConfig;
import mg.edbm.mail.dto.LocationDto;
import mg.edbm.mail.dto.request.ListRequest;
import mg.edbm.mail.entity.Location;
import mg.edbm.mail.exception.AuthenticationException;
import mg.edbm.mail.exception.NotFoundException;
import mg.edbm.mail.service.LocationService;
import mg.edbm.mail.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@Transactional
@RequiredArgsConstructor
@RequestMapping("/locations")
public class LocationController {
    private final LocationService locationService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<LocationDto> create(LocationDto locationDto) throws AuthenticationException {
        final Location location = locationService.createOrRestore(locationDto, userService.getAuthenticatedUser());
        final LocationDto mappedLocationDto = new LocationDto(location);
        return ResponseEntity.ok(mappedLocationDto);
    }

    @GetMapping
    public ResponseEntity<Page<LocationDto>> list(ListRequest listRequest) {
        final Page<LocationDto> locations = locationService.list(listRequest).map(LocationDto::new);
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/{locationId}")
    public ResponseEntity<LocationDto> get(@PathVariable Long locationId) throws NotFoundException {
        final Location location = locationService.get(locationId);
        final LocationDto locationDto = new LocationDto(location);
        return ResponseEntity.ok(locationDto);
    }

    @PutMapping("/{locationId}")
    @Secured(SecurityConfig.ROLE_ADMIN)
    public ResponseEntity<LocationDto> update(@PathVariable Long locationId, LocationDto locationDto) throws NotFoundException,
            AuthenticationException {
        final Location location = locationService.update(locationId, locationDto, userService.getAuthenticatedUser());
        final LocationDto mappedLocationDto = new LocationDto(location);
        return ResponseEntity.ok(mappedLocationDto);
    }

    @DeleteMapping("/{locationId}")
    @Secured(SecurityConfig.ROLE_ADMIN)
    public ResponseEntity<LocationDto> remove(@PathVariable Long locationId) throws NotFoundException, AuthenticationException {
        final Location location = locationService.remove(locationId, userService.getAuthenticatedUser());
        final LocationDto mappedLocationDto = new LocationDto(location);
        return ResponseEntity.ok(mappedLocationDto);
    }
}
