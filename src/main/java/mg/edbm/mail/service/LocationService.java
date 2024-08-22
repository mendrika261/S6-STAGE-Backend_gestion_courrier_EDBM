package mg.edbm.mail.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mg.edbm.mail.dto.LocationDto;
import mg.edbm.mail.dto.request.ListRequest;
import mg.edbm.mail.dto.request.filter.SearchCriteria;
import mg.edbm.mail.dto.request.filter.SpecificationImpl;
import mg.edbm.mail.dto.request.type.OperationType;
import mg.edbm.mail.entity.Location;
import mg.edbm.mail.entity.User;
import mg.edbm.mail.exception.NotFoundException;
import mg.edbm.mail.repository.LocationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class LocationService {
    private final LocationRepository locationRepository;

    public Location createOrRestore(LocationDto locationDto, User author) {
        final Location location = new Location(locationDto, author);
        final Optional<Location> removedLocation = locationRepository.findIfRemoved(location.getLatitude(), location.getLongitude());
        if(removedLocation.isPresent()) {
            final Location removed = removedLocation.get();
            removed.update(locationDto, author);
            removed.restore(author);
            log.info("{} restored {}", author, removed);
            return locationRepository.save(removed);
        }
        log.info("{} created {}", author, location);
        return locationRepository.save(location);
    }

    public Page<Location> list(ListRequest listRequest) {
        listRequest.addBaseCriteria("removedAt", OperationType.EQUAL, null);
        final Pageable pageable = listRequest.toPageable();
        final Specification<Location> specification = new SpecificationImpl<>(listRequest);
        return locationRepository.findAll(specification, pageable);
    }

    public Location get(Long locationId) throws NotFoundException {
        return locationRepository.findById(locationId).orElseThrow(
                () -> new NotFoundException("La localisation #" + locationId + " n'existe pas")
        );
    }

    public Location update(Long id, LocationDto locationDto, User authenticatedUser) throws NotFoundException {
        final Location location = get(id);
        location.update(locationDto, authenticatedUser);
        log.info("{} updated {}", authenticatedUser, location);
        return locationRepository.save(location);
    }

    public Location remove(Long locationId, User authenticatedUser) throws NotFoundException {
        final Location location = get(locationId);
        location.remove(authenticatedUser);
        log.info("{} removed {}", authenticatedUser, location);
        return locationRepository.save(location);
    }
}
