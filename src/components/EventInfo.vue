<template>
  <v-container cols="12" class="dark-container">
    <v-row>
      <v-col v-if="tracks" class="ma-0 pa-0 pl-4">
        <v-img
          v-if="notBigEnough()"
          contain
          :src="getTrackThumbnail(0)"
          cols="4"
          width="250"
        />
        <v-row v-else v-for="m in 2" :key="m" class="ma-0 pa-0">
          <v-col v-for="n in 2" :key="n" class="ma-0 pa-0" md="auto">
            <v-img
              v-if="m == 1"
              :src="getTrackThumbnail(m + n - 2)"
              width="125"
            >
            </v-img>
            <v-img v-else :src="getTrackThumbnail(m + n - 1)" width="125">
            </v-img>
          </v-col>
        </v-row>
      </v-col>
      <v-divider vertical></v-divider>
      <v-col v-if="event && tracks" cols="7">
        <div style="font-size:9px; letter-spacing: 10px;">
          <strong>EVENT</strong>
        </div>
        <h1 class="ls-2 mb-0">
          <strong>{{ event.name }}</strong>
        </h1>
        <div class="subtitle-2 mb-1 mt-0 pt-0 ls-3">
          Tracks: {{ tracks.length }}
        </div>
        <div class="subtitle-1 ls-3">
          Location:
        </div>
        <div class="mb-1 body-1 ls-2">
          <strong>{{ event.location }}</strong>
        </div>
        <div class="subtitle-1 ls-3">
          Date:
        </div>
        <div class="mb-1 body-1 ls-2">
          <strong>{{ event.date }}</strong>
        </div>
        <div class="subtitle-1 ls-3">
          Host:
        </div>
        <div class="mb-1 body-1 ls-2">
          <strong>{{ event.hostId }}</strong>
        </div>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
  props: ["event", "tracks"],
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
</style>
