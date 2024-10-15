package com.destina.Repository;






import com.destina.model.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackageRepository extends JpaRepository<Package, Long> {
    List<Package> findByLocationContaining(String location);
    List<Package> findByAgentId(Long agentId);
}
