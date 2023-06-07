package com.example.datingapp.client.dictionary;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DictionaryService {

    @GET("/api/dictionary/countries")
    Call<List<CountryDto>> getAllCountries();
}
