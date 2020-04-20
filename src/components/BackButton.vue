<template>
  <div>
    <v-btn
      color="accent"
      fab
      class="hidden-sm-and-down"
      fixed
      right
      large
      mr-10
      :to="path"
    >
      <!-- absolute right bottom -->
      <v-icon dark>mdi-chevron-double-left</v-icon>
    </v-btn>
    <v-btn
      color="accent"
      fab
      class="hidden-md-and-up"
      fixed
      right
      bottom
      large
      mr-5
      :to="path"
    >
      <v-icon dark>mdi-chevron-double-left</v-icon>
    </v-btn>
  </div>
</template>

<script>
export default {
  name: "BackButton",
  props: ["path"]
};
</script>
