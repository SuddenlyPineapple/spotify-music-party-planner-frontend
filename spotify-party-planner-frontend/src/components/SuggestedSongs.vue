<template>
  <v-row v-if="recomendations">
    <v-col cols="12" class="pb-0">
      <span class="theme--light v-label v-label--active caption small-spacing">
        Recomended Songs - based on playlist and genres list (after adding new
        genre click
        <a @click="refreshEvent($event)"><u>REFRESH DATA</u></a> for new
        recomendations!)
      </span>
    </v-col>
    <v-col cols="12" class="text-center" v-if="loading">
      <v-progress-circular
        :size="70"
        :width="7"
        color="accent"
        indeterminate
      ></v-progress-circular>
    </v-col>
    <v-col
      cols="12"
      class="text-center caption"
      v-else-if="recomendations.length == 0"
    >
      Add more genres & tracks to see recomendations...
    </v-col>
    <v-col cols="12" class="pt-0" v-else>
      <v-row class="grey lighten-5">
        <v-col
          cols="6"
          sm="3"
          md="2"
          v-for="(track, index) in recomendations.slice(0, 24)"
          :key="index"
        >
          <v-card v-if="(index + 1) % 8 === 0">
            <InFeedAdsense
              data-ad-layout-key="+2a+rx+1+2-3"
              data-ad-client="ca-pub-1122450262680065"
              data-ad-slot="5112822776"
            >
            </InFeedAdsense>
          </v-card>
          <v-card v-else>
            <v-hover v-slot:default="{ hover }">
              <v-img
                :src="getTrackThumbnail(track.element.album.img)"
                lazy-src="../assets/default-album-artwork.png"
              >
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
                            v-if="!inPlaylist(track.element.id)"
                            @click="addSongToTracks(track.element.id)"
                            dark
                            icon
                            :ripple="false"
                          >
                            <v-icon dark>mdi-heart-outline</v-icon>
                          </v-btn>
                          <v-btn
                            v-else
                            @click="deleteSongFromTracks(track.element.id)"
                            dark
                            icon
                            :ripple="false"
                          >
                            <v-icon dark>mdi-heart</v-icon>
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
  props: ["eventId", "recomendations", "pristineSelected", "loading"],
  data: () => ({
    selected: []
  }),
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
          if (response.status == 200) {
            this.$emit("eventUpdated", response.data);
            this.selected = response.data.playlist.tracks.map(
              track => track.id
            );
          }
        })
        .catch(this.setError);
    },
    deleteSongFromTracks(songId) {
      axios
        .delete(apiUrl + "events/" + this.eventId + "/tracks", {
          data: {
            trackIds: [songId]
          }
        })
        .then(response => {
          if (response.status == 200) {
            this.$emit("eventUpdated", response.data);
            this.selected = response.data.playlist.tracks.map(
              track => track.id
            );
          }
        })
        .catch(this.setError);
    },
    inPlaylist(songId) {
      return !!this.selected.filter(song => song == songId).length;
    },
    refreshEvent(e) {
      e.preventDefault();
      this.$emit("refreshData");
    }
  },
  mounted() {
    this.selected = [...this.pristineSelected];
  },
  updated() {
    this.selected = [...this.pristineSelected];
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
