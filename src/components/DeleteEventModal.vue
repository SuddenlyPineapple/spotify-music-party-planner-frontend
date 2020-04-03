<template>
  <v-row class="ml-2">
    <v-dialog v-model="dialog" persistent max-width="600px">
      <template v-slot:activator="{ on }">
        <v-btn color="red" dark v-on="on">
          Delete Event
        </v-btn>
      </template>
      <v-card>
        <v-card-title>
          <span class="headline">Delete Event</span>
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-row v-if="error">
              <v-col cols="12">
                <ErrorMessage :message="error" />
              </v-col>
            </v-row>

            <v-row>
              <v-col cols="12">
                <v-text-field
                  v-model="eventNameInput"
                  label="Enter event name to delete it*"
                  prepend-icon="mdi-pen"
                  required
                ></v-text-field>
              </v-col>
            </v-row>
          </v-container>
          <small>*indicates required field</small>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="accent" class="px-2" text @click="dialog = false"
            >Close</v-btn
          >
          <v-btn
            color="red"
            class="px-3"
            :dark="!disabled"
            @click="deleteEvent"
            :disabled="disabled"
          >
            <span v-if="!loading">DELETE</span>
            <v-icon v-else>mdi-spin mdi-loading</v-icon>
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-row>
</template>

<script>
import ErrorMessage from "../components/ErrorMessage.vue";
import axios from "axios";
import { apiUrl } from "../config/backend";

export default {
  name: "DeleteEventModal",
  props: ["eventId", "eventName"],
  data: () => ({
    dialog: false,
    eventNameInput: "",

    //ButtonData
    loading: false,
    disabled: false,

    error: null
  }),
  components: {
    ErrorMessage
  },
  methods: {
    deleteEvent() {
      this.loading = true;
      this.disabled = true;

      const validation = this.eventName == this.eventNameInput && this.eventId;

      if (validation)
        axios
          .delete(apiUrl + "events/" + this.eventId)
          .then(result => {
            if (result.status == 204) {
              this.dialog = false;
              this.eventNameInput = "";
              this.$router.push("/");
            }
          })
          .catch(() => {
            this.error =
              "Couldn't delete event. Please try again or contact administrator!";
          });
      else this.error = "Wrong event name!";

      this.loading = false;
      this.disabled = false;
    }
  }
};
</script>
