package com.pokedyno.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PokemonDetail {
    private String id;
    private String name;
    private Sprites sprites;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Sprites getSprites() {
        return sprites;
    }

    public void setSprites(Sprites sprites) {
        this.sprites = sprites;
    }

    public static class Sprites {
        @JsonProperty("other")
        private OtherSprites otherSprites;

        public OtherSprites getOther() {
            return otherSprites;
        }

        public void setOtherSprites(OtherSprites otherSprites) {
            this.otherSprites = otherSprites;
        }
    }

    public static class OtherSprites {
        @JsonProperty("official-artwork")
        private OfficialArtwork officialArtwork;

        public OfficialArtwork getOfficialArtwork() {
            return officialArtwork;
        }

        public void setOfficialArtwork(OfficialArtwork officialArtwork) {
            this.officialArtwork = officialArtwork;
        }
    }

    public static class OfficialArtwork {
        @JsonProperty("front_default")
        private String frontDefault;

        public String getFrontDefault() {
            return frontDefault;
        }

        public void setFrontDefault(String frontDefault) {
            this.frontDefault = frontDefault;
        }
    }
}
