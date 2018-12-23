package com.example.user.tourmate;

public class MemorablePlaceClass {
    private  String memorableImage;
    private String captions;

    public MemorablePlaceClass() {
    }

    public MemorablePlaceClass(String memorableImage, String captions) {
        this.memorableImage = memorableImage;
        this.captions = captions;
    }

    public String getMemorableImage() {
        return memorableImage;
    }

    public void setMemorableImage(String memorableImage) {
        this.memorableImage = memorableImage;
    }

    public String getCaptions() {
        return captions;
    }

    public void setCaptions(String captions) {
        this.captions = captions;
    }
}
