package com.example.emili.application1.Donnee;

/**
 * Created by emili on 24/07/2017.
 */


public class Message {
    private String text;
    private String nom;
    private String photoUrl;
    private int no_photoUrl = -1;

    public Message(){

    }
    public Message(String nom, String text, String photoUrl){
        this.text = text;
        this.nom = nom;
        this.photoUrl = photoUrl;
    }

    public String getText(){
        return text;
    }

    public void setText(String text){

        this.text = text;
    }

    public String getNom(){
        return nom;
    }
    public void setNom(String nom){

        this.nom = nom;
    }

    public String getPhotoUrl(){
        return photoUrl;
    }


    public void setPhotoUrl(String photoUrl){

        this.photoUrl = photoUrl;
    }


}
