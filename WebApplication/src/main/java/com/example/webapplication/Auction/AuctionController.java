package com.example.webapplication.Auction;

import com.example.webapplication.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auctions")
public class AuctionController {
    private final AuctionService auctionService;

    @Autowired
    public AuctionController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    @CrossOrigin(origins = "*")
    @PostMapping()
    public ResponseEntity<Auction> registerAuctionToBase(@RequestBody Auction auctionForRegistration) {
        return new ResponseEntity<>(auctionService.registerAuctionToBase(auctionForRegistration), HttpStatus.CREATED);
    }

    @GetMapping("/getAllActiveAuctions")
    public List<Auction> getAllActiveAuctions(){
        return auctionService.getAllActiveAuctions();
    }

    @CrossOrigin(origins = "*")
    @PreAuthorize("hasAuthority('AUCTION')")
    @DeleteMapping("/deleteAuction")
    public ResponseEntity<?> deleteAuction(@RequestBody Auction auctionForDelete){
        return new ResponseEntity<>(auctionService.deleteAuction(auctionForDelete),HttpStatus.OK);
    }
}