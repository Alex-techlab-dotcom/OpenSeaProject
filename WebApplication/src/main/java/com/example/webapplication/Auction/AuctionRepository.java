package com.example.webapplication.Auction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuctionRepository extends JpaRepository<Auction,Long> {
    @Query(nativeQuery = true, value="select a.auctionEndTime, from Auction a where a.auctionEndTime after :now")
    List<Auction> findAllWithAuctionEndTimeAfter(@Param("now") LocalDateTime now);
}
