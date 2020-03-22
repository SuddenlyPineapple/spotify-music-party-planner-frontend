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
    :key="events.id"
  />
</template>

<script>
export default {
  name: "EventList",
  props: ["events", "error", "loading"],
  data: () => ({
    headers: [
      { text: "Id", value: "id" },
      { text: "Name", value: "name" },
      { text: "Location", value: "location" },
      { text: "Date", value: "date" },
      { text: "Open", value: "open" },
      { text: "", value: "data-table-expand" }
    ]
  })
};
</script>
