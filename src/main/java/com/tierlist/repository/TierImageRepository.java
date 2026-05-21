package com.tierlist.repository;

import com.tierlist.model.TierImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TierImageRepository extends JpaRepository<TierImage, Long> {
    List<TierImage> findAllByOrderBySortOrderAsc();
}
