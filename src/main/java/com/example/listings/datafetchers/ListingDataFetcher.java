package com.example.listings.datafetchers;
import com.example.listings.generated.types.Amenity;
import com.example.listings.generated.types.CreateListingResponse;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsQuery;
import com.example.listings.models.ListingModel;
import graphql.execution.DataFetcherResult;
import com.netflix.graphql.dgs.DgsMutation;

import java.io.IOException;
import java.util.Map;
import java.util.List;
import com.example.listings.datasources.ListingService;
import org.springframework.beans.factory.annotation.Autowired;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import com.netflix.graphql.dgs.InputArgument;
import com.example.listings.generated.types.CreateListingInput;

import org.dataloader.DataLoader;



@DgsComponent
public class ListingDataFetcher {

    private final ListingService listingService;

    @Autowired
    public ListingDataFetcher(ListingService listingService) {
        this.listingService = listingService;
    }
    @DgsQuery
    public DataFetcherResult<List<ListingModel>> featuredListings() throws IOException {
        List<ListingModel> listings = listingService.featuredListingsRequest();
        return DataFetcherResult.<List<ListingModel>>newResult()
                .data(listings)
                .localContext(Map.of("hasAmenityData", false))
                .build();
    }

    @DgsQuery
    public DataFetcherResult<ListingModel> listing(@InputArgument String id) {
        ListingModel listing = listingService.listingRequest(id);
        return DataFetcherResult.<ListingModel>newResult()
                .data(listing)
                .localContext(Map.of("hasAmenityData", true))
                .build();
    }
    @DgsData(parentType = "Listing")
    public Object amenities(DgsDataFetchingEnvironment dfe) throws IOException {
        ListingModel listing = dfe.getSource();
        String id = listing.getId();
        Map<String, Boolean> localContext = dfe.getLocalContext();

        if (localContext.get("hasAmenityData")) {
            return listing.getAmenities();
        }

        DataLoader<String, List<Amenity>> amenityDataLoader = dfe.getDataLoader("amenities");
        return amenityDataLoader.load(id); // returns CompletableFuture<List<Amenity>>
    }

    @DgsMutation
    public CreateListingResponse createListing(@InputArgument CreateListingInput input) {
        ListingModel createdListing = listingService.createListingRequest(input);
        CreateListingResponse response = new CreateListingResponse();

        if (createdListing != null) {
            response.setListing(createdListing);
            response.setCode(200);
            response.setMessage("success");
            response.setSuccess(true);


            return response;
        }

        response.setListing(null);
        response.setCode(500);
        response.setMessage("could not create listing");
        response.setSuccess(false);

        System.out.println(response.getListing());
        return response;
    }
}
