package com.pathdevel.wikihow;

public class Article {
    String name;
    String url;
    String image_url;

    Article(String _url, String _name, String _image_url) {
        name = _name;
        url = _url;
        image_url = _image_url;
    }
}
