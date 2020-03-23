<template>
  <v-row class="ml-2">
    <v-dialog v-model="dialog" persistent max-width="600px">
      <template v-slot:activator="{ on }">
        <v-btn color="accent" dark v-on="on">
          Add Event
        </v-btn>
      </template>
      <v-card>
        <v-card-title>
          <span class="headline">Add Event</span>
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-row v-if="error">
              <v-col cols="12">
                <ErrorMessage :message="error" />
              </v-col>
            </v-row>
            <v-row>
              <v-col cols="12" sm="6">
                <v-text-field
                  v-model="event.name"
                  label="Event name*"
                  prepend-icon="mdi-pen"
                  required
                ></v-text-field>
              </v-col>

              <v-col cols="12" sm="6">
                <v-text-field
                  v-model="event.location"
                  label="Location*"
                  prepend-icon="mdi-pen"
                  required
                ></v-text-field>
              </v-col>

              <v-col cols="12" sm="6">
                <v-menu
                  ref="timePicker"
                  v-model="timePickerMenu"
                  :close-on-content-click="false"
                  :nudge-right="40"
                  :return-value.sync="time"
                  transition="scale-transition"
                  offset-y
                  max-width="290px"
                  min-width="290px"
                >
                  <template v-slot:activator="{ on }">
                    <v-text-field
                      v-model="time"
                      label="Time*"
                      prepend-icon="mdi-timer"
                      readonly
                      v-on="on"
                    ></v-text-field>
                  </template>
                  <v-time-picker
                    v-if="timePickerMenu"
                    v-model="time"
                    full-width
                    @click:minute="$refs.timePicker.save(time)"
                  ></v-time-picker>
                </v-menu>
              </v-col>

              <v-col cols="12" sm="6">
                <v-menu
                  ref="datePicker"
                  v-model="datePickerMenu"
                  :close-on-content-click="false"
                  :return-value.sync="date"
                  transition="scale-transition"
                  offset-y
                  min-width="290px"
                >
                  <template v-slot:activator="{ on }">
                    <v-text-field
                      v-model="date"
                      label="Date*"
                      prepend-icon="mdi-calendar"
                      readonly
                      v-on="on"
                    ></v-text-field>
                  </template>
                  <v-date-picker v-model="date" no-title scrollable>
                    <v-spacer></v-spacer>
                    <v-btn text color="primary" @click="datePickerMenu = false"
                      >Cancel</v-btn
                    >
                    <v-btn
                      text
                      color="primary"
                      @click="$refs.datePicker.save(date)"
                    >
                      OK
                    </v-btn>
                  </v-date-picker>
                </v-menu>
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
            color="accent"
            class="px-3"
            :dark="!disabled"
            @click="saveEvent"
            :disabled="disabled"
          >
            <span v-if="!loading">Save</span>
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
  name: "AddEventModal",
  props: ["userId"],
  data: () => ({
    dialog: false,
    timePickerMenu: false,
    datePickerMenu: false,
    time: null,
    date: new Date().toISOString().substr(0, 10),
    event: {
      name: "",
      location: ""
    },

    //ButtonData
    loading: false,
    disabled: false,

    error: null
  }),
  components: {
    ErrorMessage
  },
  methods: {
    saveEvent() {
      this.loading = true;
      this.disabled = true;

      const validation =
        this.time && this.date && this.event.name && this.event.location;

      if (validation)
        axios
          .post(apiUrl + "events", {
            ...this.event,
            date: this.date + "T" + this.time + ":00Z",
            hostId: this.userId
          })
          .then(result => {
            this.dialog = false;
            this.event.name = "";
            this.event.location = "";

            this.$emit("saved", result.data.id);
          })
          .catch(() => {
            this.error =
              "Couldn't create Event. Please try again or contact administrator!";
          });
      else this.error = "You must fill all required fields!";

      this.loading = false;
      this.disabled = false;
    }
  }
};
</script>
