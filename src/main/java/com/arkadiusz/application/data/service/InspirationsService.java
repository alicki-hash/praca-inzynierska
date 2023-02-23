package com.arkadiusz.application.data.service;

import com.arkadiusz.application.data.entity.InspirationsPhotoModel;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class InspirationsService {


    private OkHttpClient client;
    private Response response;
    private String keyAPI = "563492ad6f9170000100000104c46adf167e4c8284e378d50d6d896a";

    List<InspirationsPhotoModel> photosList = new ArrayList<>();

    public InspirationsService() {

    }

    public JSONArray getImagesPage(int pageNumber) {
        String url = "https://api.pexels.com/v1/search?query=interior&page=" + pageNumber + "&per_page=80";

        client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", keyAPI).build();

        try {
            response = client.newCall(request).execute();
            return new JSONObject(response.body().string()).getJSONArray("photos");
        } catch (IOException e) {
            System.out.println("FAILED");
            e.printStackTrace();
        }

        return null;
    }

//    public JSONArray getPhotos(){
//        JSONArray photosArray = getImagesPage().getJSONArray("photos");
////        System.out.println(photosArray);
//        return photosArray;
//    }

    public List<InspirationsPhotoModel> collectPhotos(int pageNumber) {

        JSONArray photosArray = getImagesPage(pageNumber);

        int length = photosArray.length();

        for (int i = 0; i < length; i++) {
            JSONObject object = photosArray.getJSONObject(i);
            int id = object.getInt("id");
            String url = object.getString("url");
            String photographer = object.getString("photographer");
            String altText = object.getString("alt");

            JSONObject objectImage = object.getJSONObject("src");

            String originalUrl = objectImage.getString("original");
            String mediumUrl = objectImage.getString("medium");
            String largeUrl = objectImage.getString("large");

            InspirationsPhotoModel photo = new InspirationsPhotoModel(id, url, originalUrl, largeUrl,
                    mediumUrl, photographer, altText);
            photosList.add(photo);
        }

        return photosList;
    }

    public List<InspirationsPhotoModel> getPhotosList() {
        return photosList;
    }
}
