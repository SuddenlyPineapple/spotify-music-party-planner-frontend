<template>
  <v-data-table
    :v-if="events"
    :headers="headers"
    :items="events"
    class="elevation-1"
    dense
    multi-sort
    :loading="loading"
    loading-text="Fetching data..."
  >
    <template v-slot:item.open="{ item }">
      <v-icon v-if="item.open" color="accent" small>
        mdi-clock
      </v-icon>
      <v-icon v-else color="red" small>
        mdi-lock
      </v-icon>
    </template>

    <template v-slot:item.id="{ item }">
      <u>
        <a @click="$router.push('/event/' + item.id)">{{ item.id }}</a>
      </u>
    </template>
  </v-data-table>
</template>

<script>
export default {
  name: "EventList",
  props: ["events", "error", "loading"],
  data: () => ({
    headers: [
      { text: "Event Link", value: "id" },
      { text: "Name", value: "name" },
      { text: "Location", value: "location" },
      { text: "Date", value: "date" },
      { text: "Open", value: "open" },
      { text: "", value: "data-table-expand" }
    ]
  })
};
</script>
