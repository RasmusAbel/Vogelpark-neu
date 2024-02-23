package de.bird.vogelpark.repositories;

import de.bird.vogelpark.beans.BirdParkBasicInfo;
import org.springframework.data.repository.CrudRepository;
public interface BirdParkBasicDataRepository extends CrudRepository <BirdParkBasicInfo, Long>{
}
