<template>

  <v-container fluid>
    <v-row>

      <v-col cols="12">
        <v-row
          :align="alignment"
          :justify="justify"
          class="grey lighten-5"
          style="height: 300px;"
        >
          <v-card
            v-for="n in 12"
            :key="n"
          >
        <v-hover v-slot:default="{ hover }">
        <v-img :src="getTrackThumbnail(n)">
            <v-expand-transition>
            <div
              v-if="hover"
              class="d-flex transition-fast-in-fast-out accent darken-2 v-card--reveal white--text"
             
            >
            <v-row align="center">
                <v-col class="text-center" >
                {{getData(n)}}
                Title: {{suggested[n].name}} <br/>
                Artist: {{suggested[n].artist}} <br/>

            </v-col></v-row>
            <v-btn icon color="black"  @click="addSong(suggested[n].id)">
              <v-icon v-if="inPlaylist(suggested[n].id)">mdi-heart</v-icon>
              <v-icon v-else>mdi-heart-outline</v-icon>
            </v-btn>
            </div>
          </v-expand-transition>
        </v-img>
        </v-hover>
          </v-card>
        </v-row>
      </v-col>
    </v-row>
  </v-container>

</template>

<script>
import axios from "axios";
import { apiUrl } from "../config/backend";
export default {
 props: ["event"],
  data: () => ({
      suggested: [],
      selected: [],
  }),
  computed: {
    tracks() {
      return this.event && this.event.playlist && this.event.playlist.suggestions.fromGuests.tracks
        ? this.event.playlist.suggestions.fromGuests.tracks
        : [];
    }
  },
  methods: {
    getTrackThumbnail(n) {
      if (this.tracks.length != 0) {
        if (this.tracks[n].element.album.img.length >= 2) {
          return this.tracks[n].element.album.img[1].url;
        } else {
            return this.tracks[n].element.album.img[0].url;}

      } else return require("../assets/default-album-artwork.png");
    },
    addSong(songId) {
      const endpoint = apiUrl + "events/" + this.eventId + "/suggestions";

      if (this.inPlaylist(songId)) {
        axios
          .delete(endpoint, {
            data: {
              tracks: [songId]
            }
          })
          .then(response => {
            if (response.status == 200)
              this.selected = this.selected.filter(song => song != songId);
          })
          .catch(this.setError);
      } else {
        axios
          .put(endpoint, {
            tracks: [songId]
          })
          .then(response => {
            if (response.status == 200) this.selected.push(songId);
          })
          .catch(this.setError);
      }
    },
    inPlaylist(songId) {
      return !!this.selected.filter(song => song == songId).length;
    },
    getData(n){
        this.suggested[n] = this.event.playlist.suggestions.fromGuests.tracks[n].element;
    },
  }
};
</script>

<style lang="scss" scoped>
.dark-container {
  background-color: #2e2e2e;
  color: #fff;
}
.ls-2 {
  letter-spacing: 2px !important;
}
.ls-3 {
  letter-spacing: 2px !important;
}
.event-headline {
  font-size: 9px;
  letter-spacing: 10px;
}
.v-card--reveal {
  align-items: center;
  bottom: 0;
  justify-content: center;
  opacity: .8;
  position: absolute;
  width: 100%;
  height: 100%; 
  font-size: 15pt;
}
</style>
