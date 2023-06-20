package com.upc.leadyourway.service;

import com.upc.leadyourway.dto.RentDto;
import com.upc.leadyourway.model.Rent;

import java.util.List;

public interface RentService {
    public abstract Rent create(RentDto rent);
    public abstract Rent getById(Long rent_id);
    public abstract void delete(Long rent_id);

    public abstract List<Rent> getByBicycleId(Long bicycle_id);
}
