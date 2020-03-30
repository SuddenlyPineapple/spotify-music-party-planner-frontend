<template>
  <div>
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
          mdi-check
        </v-icon>
        <v-icon v-else color="red" small>
          mdi-lock
        </v-icon>
      </template>

      <template v-slot:item.id="{ item }">
        <u>
          <a :href="getLink(item.id)" @click="goToEvent(item.id, $event)"
            >{{ item.id }}
          </a>
        </u>
      </template>

      <template v-slot:item.copy="{ item }">
        <v-icon @click="copyLink(item.id)" small>
          mdi-content-copy
        </v-icon>
      </template>
    </v-data-table>

    <v-snackbar
      v-model="showInfo"
      bottom
      right
      color="accent"
      multi-line
      :timeout="2000"
    >
      Invitation link copied!
      <v-btn dark text @click="showInfo = false" fab>
        <v-icon>mdi-close</v-icon>
      </v-btn>
    </v-snackbar>
  </div>
</template>

<script>
export default {
  name: "EventList",
  props: ["events", "error", "loading"],
  data: () => ({
    showInfo: false,
    headers: [
      { text: "Event Link", value: "id" },
      { text: "Name", value: "name" },
      { text: "Location", value: "location" },
      { text: "Date", value: "date" },
      { text: "Open", value: "open" },
      { text: "Copy Link", value: "copy" }
    ]
  }),
  methods: {
    copyLink(id) {
      const link = this.getLink(id);
      this.$copyText(link).then(() => {
        this.showInfo = true;
      });
    },
    getLink(id) {
      return location.href + "event/" + id;
    },
    goToEvent(id, e) {
      e.preventDefault();
      this.$router.push("/event/" + id);
    }
  }
};
</script>
