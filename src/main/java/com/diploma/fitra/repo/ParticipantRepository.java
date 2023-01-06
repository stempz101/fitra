package com.diploma.fitra.repo;

import com.diploma.fitra.model.Participant;
import com.diploma.fitra.model.Travel;
import com.diploma.fitra.model.key.ParticipantKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, ParticipantKey> {

    List<Participant> findAllByTravel(Travel travel);

    long countByTravel(Travel travel);
}
