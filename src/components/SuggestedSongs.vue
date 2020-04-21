<template>
  <v-row v-if="recomendations">
    <v-col cols="12" class="pb-0">
      <span class="theme--light v-label v-label--active caption small-spacing">
        Recomended Songs - based on playlist and genres list
      </span>
    </v-col>
    <v-col cols="12" class="pt-0">
      <v-row class="grey lighten-5">
        <v-col
          cols="6"
          sm="3"
          md="2"
          v-for="track in recomendations.slice(0, 12)"
          :key="track.element.id"
        >
          <v-card>
            <v-hover v-slot:default="{ hover }">
              <v-img :src="getTrackThumbnail(track.element.album.img)">
                <v-fade-transition>
                  <v-overlay
                    v-if="hover"
                    absolute
                    color="primary"
                    class="text-center"
                    :opacity="0.75"
                  >
                    <v-layout>
                      <v-row>
                        <v-col cols="12" class="pb-0">
                          {{ track.element.name }} -
                          {{ track.element.artist }}
                        </v-col>
                        <v-col cols="12">
                          <v-btn
                            @click="addSongToTracks(track.element.id)"
                            dark
                            icon
                            :ripple="false"
                          >
                            <v-icon dark>mdi-heart-outline</v-icon>
                          </v-btn>
                        </v-col>
                      </v-row>
                    </v-layout>
                  </v-overlay>
                </v-fade-transition>
              </v-img>
            </v-hover>
          </v-card>
        </v-col>
      </v-row>
    </v-col>
  </v-row>
</template>

<script>
import axios from "axios";
import { apiUrl } from "../config/backend";

export default {
  props: ["eventId", "recomendations"],
  methods: {
    getTrackThumbnail(images) {
      if (images.length) {
        if (images.length >= 2) {
          return images[1].url;
        } else {
          return images[0].url;
        }
      } else return require("../assets/default-album-artwork.png");
    },
    addSongToTracks(songId) {
      axios
        .put(apiUrl + "events/" + this.eventId + "/tracks", {
          trackIds: [songId]
        })
        .then(response => {
          if (response.status == 200)
            console.log("snackbar display & hover lock or smth"); // TODO: snackbar display & hover lock or smth
        })
        .catch(this.setError);
    }
  }
};
</script>

<style lang="scss" scoped>
.v-card--reveal {
  align-items: center;
  bottom: 0;
  justify-content: center;
  opacity: 0.8;
  position: absolute;
  width: 100%;
  height: 100%;
  font-size: 15pt;
}
.small-spacing {
  letter-spacing: 0 !important;
}
</style>
