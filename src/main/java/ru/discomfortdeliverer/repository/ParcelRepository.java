package ru.discomfortdeliverer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.discomfortdeliverer.entity.ParcelEntity;

@Repository
public interface ParcelRepository extends JpaRepository<ParcelEntity, Long> {
    ParcelEntity findByName(String name);

    @Transactional
    @Modifying
    @Query("UPDATE ParcelEntity p SET p.name = ?2 WHERE p.name = ?1")
    int updateParcelNameByName(String oldName, String newName);

    @Transactional
    @Modifying
    @Query("UPDATE ParcelEntity p SET p.form = ?2, p.symbol = ?3 WHERE p.name = ?1")
    int updateParcelSymbolByName(String parcelName, String newForm, String newSymbol);

    @Transactional
    @Modifying
    @Query("UPDATE ParcelEntity p SET p.form = ?2, p.symbol = ?3 WHERE p.name = ?1")
    int updateParcelByName(String parcelName, String newForm, String symbol);
}
