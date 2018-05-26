package com.example.furyou.lbcvision;

public class Product {
    private String photo;
    private String description;
    private String prix;
    private String category;
    private String adresse;
    private String date;

    public Product(String photo, String description, String prix, String category, String adresse, String date)
    {
        this.photo = photo;
        this.description = description;
        this.prix = prix;
        this.category = category;
        this.adresse = adresse;
        this.date = date;
    }

    public String getPhoto()
    {
        return this.photo;
    }

    public String getDescription()
    {
        return this.description;
    }

    public String getPrix()
    {
        return this.prix;
    }

    public String getCategory()
    {
        return this.category;
    }

    public String getAdresse()
    {
        return this.adresse;
    }

    public String getDate()
    {
        return this.date;
    }
}
