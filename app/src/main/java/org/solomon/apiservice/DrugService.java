package org.solomon.apiservice;

import org.solomon.storage.models.Drug;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;


public interface DrugService {

    @GET("/get_drugs/")
    void getDrugs(Callback<List<Drug>> drugListCB);
}
