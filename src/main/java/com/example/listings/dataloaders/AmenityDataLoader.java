package com.example.listings.dataloaders;

import com.example.listings.datasources.ListingService;
import com.example.listings.generated.types.Amenity;
import com.netflix.graphql.dgs.DgsDataLoader;
import org.dataloader.BatchLoader;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@DgsDataLoader(name = "amenities")
public class AmenityDataLoader implements BatchLoader<String, List<Amenity>>{
    @Autowired
    ListingService listingService;

    @Override
    public CompletionStage<List<List<Amenity>>> load(List<String> listingIds) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return listingService.multipleAmenitiesRequest(listingIds);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
