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
      <v-col cols="12">
        <v-data-table
          v-model="selected"
          :items="tracks"
          :headers="headers"
          :items-per-page="5"
          show-select
          class="elevation-1"
        >
          <template v-slot:item.album.img="{ item }">
            <v-img
              v-if="item.length == 3"
              :src="item[2]"
              height="64"
              width="64"
            ></v-img>
            <td v-else>
              <img
                src="../assets/default-album-artwork.png"
                height="64"
                width="64"
              />
            </td>
          </template>
        </v-data-table>
        {{ tracks }}
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
      { text: "Cover", sortable: false, value: "album.img[2].url" },
      { text: "Title", value: "name" },
      { text: "Artist", value: "artist" },
      { text: "Duration", value: "duration" }
    ],
    selected: []
  })
};
</script>
