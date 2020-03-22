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
        {{ tracks }}
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
  name: "SongList",
  props: ["tracks", "loading"]
};
</script>
