<template>
  <v-dialog
    v-model="dialog"
    persistent
    max-width="600px"
  >
    <template v-slot:activator="{ on, attrs }">
      <v-list-item
        link
        v-on="on"
        v-bind="attrs"
      >
        <v-list-item-icon>
          <v-icon>mdi-plus</v-icon>
        </v-list-item-icon>

        <v-list-item-content>
          <v-list-item-title>Add Bucket Item</v-list-item-title>
        </v-list-item-content>
      </v-list-item>
    </template>
    <v-card>
      <v-card-title>
        <span class="text-h5">New Bucket List Item</span>
      </v-card-title>
      <v-card-text>
        <v-form v-model="valid">
          <v-container>
            <v-row>
              <v-col cols="12">
                <v-text-field
                  v-model="title"
                  label="Title"
                  :rules="titleRules"
                  :counter="30"
                  required
                ></v-text-field>
              </v-col>
              <v-col cols="12">
                <v-autocomplete
                  v-model="location"
                  :items="towns"
                  label="Location"
                  :rules="locationRules"
                  required
                ></v-autocomplete>
              </v-col>
              <v-col cols="12">
                <v-combobox
                  v-model="tags"
                  :items="['Hiking', 'Food', 'Drink', 'Museum']"
                  label="Tags"
                  :rules="tagRules"
                  chips
                  deletable-chips
                  required
                  multiple
                ></v-combobox>
              </v-col>
              <v-col cols="12">
                <v-file-input
                  v-model="coverImage"
                  :rules="imageRules"
                  accept="image/*"
                  label="Cover Image"
                  required
                ></v-file-input>
              </v-col>
              <v-col cols="12">
                <v-checkbox
                  v-model="addToList"
                  label="Add to your list?"
                  color="primary"
                  hide-details
                ></v-checkbox>
              </v-col>
            </v-row>
          </v-container>
        </v-form>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn
          color="blue darken-1"
          text
          @click="dialog = false"
        >
          Close
        </v-btn>
        <v-btn
          color="blue darken-1"
          text
          @click="submit"
        >
          Save
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
  import { towns } from "../util/locations";
  import {mapGetters} from 'vuex';

  export default {
    data: () => ({
      dialog: false,
      valid: false,
      towns: towns(),
      title: '',
      location: '',
      coverImage: null,
      tags: null,
      addToList: true,
      titleRules: [
        v => !!v || 'Title is required',
        v => v.length <= 30 || 'Title must be less than 20 characters',
      ],
      tagRules: [
        v => !!v || 'At least one tag is required',
      ],
      locationRules: [
        v => !!v || 'Title is required',
      ],
      imageRules: [
        v => !!v || 'You must upload a cover image',
      ],
    }),
    methods: {
      submit() {
        if (this.valid) {
          let formData = new FormData();
          formData.append("image", this.coverImage);
          formData.append("properties", new Blob([JSON.stringify({
            "title": this.title,
            "location": this.location,
            "tags": this.tags,
            "author": this.username,
            "addToList": this.addToList,
          })], {
            type: "application/json"
          }));

          this.$axios.post(`/bucket`, formData, {
            headers: {
              "Content-Type": undefined
            },
          })
            .then(function (response) {
              console.log(response);
            })
            .catch(function (error) {
              console.log(error);
            });
          this.dialog=false;
        }
      },
    },
    computed: {
      ...mapGetters(['isLoggedIn', 'username'])
    },
  }
</script>
