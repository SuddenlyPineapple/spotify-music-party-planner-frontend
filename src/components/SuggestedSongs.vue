<template>
  <v-container fluid>
    <v-row>
        {{ event }}
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
          
        <v-img :src="getTrackThumbnail(n)">
        Witam 
            <v-expand-transition>
            <div
              v-if="hover"
              class="d-flex transition-fast-in-fast-out orange darken-2 v-card--reveal display-3 white--text"
              style="height: 100%;"
            >
              $14.99
            </div>
          </v-expand-transition>
        </v-img>
          </v-card>
        </v-row>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
 props: ["event"],
  data: () => ({
  }),
  computed: {
    tracks() {
      return this.event && this.event.playlist && this.event.playlist.tracks
        ? this.event.playlist.tracks
        : [];
    }
  },
  methods: {
    getTrackThumbnail(n) {
      if (this.tracks.length != 0) {
        if (this.tracks[n].album.img.length >= 2) {
          return this.tracks[n].album.img[1].url;
        } else return this.tracks[n].album.img[0].url;
      } else return require("../assets/default-album-artwork.png");
    },
    notBigEnough() {
      return this.tracks.length < 4;
    }
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
  opacity: .5;
  position: absolute;
  width: 100%;
}
</style>
