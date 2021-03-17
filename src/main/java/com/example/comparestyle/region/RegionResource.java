package com.example.comparestyle.region;

import org.springframework.hateoas.EntityModel;

public class RegionResource extends EntityModel<Region> {
    public static EntityModel<Region> modelOf(Region region) {
        EntityModel<Region> regionResource = EntityModel.of(region);
        return regionResource;
    }
}
