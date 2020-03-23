<template>
  <v-container>
    <v-row>
      <v-col cols="12" class="text-center" v-if="loading">
        <v-progress-circular
          :size="70"
          :width="7"
          color="accent"
          indeterminate
        ></v-progress-circular>
      </v-col>
      <v-col cols="12" v-else>
        <v-data-table
          v-model="selected"
          :items="tracks"
          :headers="headers"
          :items-per-page="5"
          class="elevation-1"
        >
          <template v-slot:item.album="{ item }">
            <img
              v-if="item.album.img.length == 3"
              :src="item.album.img[2].url"
              height="64"
              width="64"
            />
            <img
              v-else
              src="../assets/default-album-artwork.png"
              height="64"
              width="64"
            />
          </template>
        </v-data-table>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
  name: "SongList",
  props: ["tracks", "loading"],
  data: () => ({
    headers: [
      { text: "Cover", sortable: false, value: "album" },
      { text: "Title", value: "name" },
      { text: "Artist", value: "artist" },
      { text: "Duration", value: "duration" }
    ],
    selected: []
  })
};
</script>
