package com.diploma.fitra.repo;

import com.diploma.fitra.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByTravelIdOrderByStartTimeAsc(Long travelId);

    void deleteAllByTravelId(Long travelId);
}
