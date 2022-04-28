<template>
  <v-container>
    <v-row>
      <v-col
        cols="12"
        sm="2"
      >
        <v-sheet
          class="pa-5 textured-green"
          rounded="lg"
          min-height="90vh"
        >
          <h2>Filter</h2>
          <v-card class="mb-3">
            <v-card-title small>
              Min Rating
            </v-card-title>
            <v-card-text>
              <v-rating
                v-model="minRating"
                background-color="orange lighten-3"
                color="orange"
              ></v-rating>
            </v-card-text>
          </v-card>
          <v-card class="mb-3">
            <v-card-title small class="pb-0"><v-icon>mdi-tag-heart-outline</v-icon>Tags</v-card-title>
            <v-card-text class="pb-0">
              <v-combobox
                v-model="tags"
                :items="tagOptions"
                multiple
                chips
                deletable-chips
              ></v-combobox>
            </v-card-text>
          </v-card>
          <v-card class="mb-3">
            <v-card-title small class="pb-0"><v-icon>mdi-pin-outline</v-icon>County</v-card-title>
            <v-card-text class="pb-0">
              <v-combobox
                v-model="counties"
                :items="countyOptions"
                multiple
                chips
                deletable-chips
              ></v-combobox>
            </v-card-text>
          </v-card>
          <v-btn
            class="float-right"
            color="primary"
            @click="filter"
          >Search
          </v-btn>
        </v-sheet>
      </v-col>
      <v-col
        cols="12"
        sm="10"
      >
        <v-sheet
          class="pa-5 textured-green"
          rounded="lg"
        >
          <v-row dense>
            <v-col
              v-if="filteredItems.length === 0"
            >
              No items found, try changing your search
            </v-col>
            <v-col
              v-for="item of filteredItems"
              :key="item.slug"
            >
              <v-card
                nuxt
                :to="'/bucket/' + item.slug"
                :width="cardWidth"
                class="mx-auto my-6 rounded-l"
                raised
                hover
              >
                <v-img
                  height="250"
                  :src="'https://ni-bucket-image.s3.eu-west-1.amazonaws.com/' + item.photoId"
                ></v-img>

                <v-card-title>{{ item.title }}</v-card-title>

                <v-card-text class="mb-0">
                  <v-row
                    align="center"
                    class="mx-0"
                  >
                    <template v-if="item.rating">
                      <v-rating
                        :value="item.rating"
                        color="amber"
                        dense
                        half-increments
                        readonly
                        size="14"
                      ></v-rating>

                      <div class="grey--text ms-4">
                        {{ Math.round(item.rating *2)/2 }}
                      </div>
                    </template>
                    <p class="mb-0" v-else="item.rating">No Ratings</p>
                  </v-row>

                  <v-chip-group v-if="item.tags" class="mt-4">
                    <v-chip v-for="tag of item.tags" :key="tag" disavked>{{ tag }}</v-chip>
                  </v-chip-group>
                  <div v-else="item.tags" class="mt-4 text-subtitle-1">
                    <span>No tags</span>
                  </div>
                </v-card-text>

                <v-card-actions>
                   <v-container>
                            <v-row justify="center" align="center">
                              <v-col align="center">
                                <v-btn
                    v-if="isLoggedIn && !addedSlugs.includes(item.slug)"
                    color="blue-grey"
                    class="white--text rounded-xl"
                    @click.prevent="addToList(item.slug)"
                  >
                    Add to List
                  </v-btn>
                  <v-btn v-else text disabled>
                  Item is in list
                  </v-btn>
                  </v-col>
                  </v-row>
                  </v-container>
                </v-card-actions>
              </v-card>
            </v-col>
          </v-row>
        </v-sheet>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
  import {mapGetters} from 'vuex';
  import { counties, county } from "../util/locations";

  export default {
    layout: "default",
    auth: false,
    data: () => ({
      bucketItems: [],
      filteredItems: [],
      userInfo: null,
      minRating: 1,
      tags: [],
      tagOptions: [],
      counties: [],
      countyOptions: counties(),
      addedSlugs: [],
      completedSlugs: [],
    }),
    computed: {
      ...mapGetters(['isLoggedIn', 'username']),
      cardWidth() {
        switch (this.$vuetify.breakpoint.name) {
          case 'xs':
            return "70vw"
          case 'sm':
            return "70vw"
          case 'md':
            return "30vw"
          case 'lg':
            return "20vw"
          case 'xl':
            return "20vw"
        }
      },
    },
    async fetch() {
      this.bucketItems = await this.$axios.$get('/bucket/all');
      this.filteredItems = this.bucketItems;
      this.tagOptions = this.bucketItems.flatMap(i => i.tags)
        this.tagOptions = [...new Set(this.tagOptions)];
      if (this.isLoggedIn) {
        let username = this.username;
        this.userInfo = await this.$axios.$get('/user/' + username);
        this.addedSlugs = this.userInfo.bucketList.map(i => i.slug);
        this.completedSlugs = this.userInfo.bucketList.filter(i => i.completed).map(i => i.slug);
      }
    },
    methods: {
      addToList(slug) {
        this.$axios.put(`/user/${this.username}/bucket`, {
          slug: slug,
        }, {
          headers: {
            "Content-Type": "application/json"
          },
        })
          .then(function (response) {
            console.log(response);
          })
          .catch(function (error) {
            console.log(error);
          });
          this.addedSlugs.push(slug);
      },
      filter() {
        this.filteredItems = this.bucketItems.filter(i => {
          const rating = i.rating || 1;
          let countyMatch = true;
          if (this.counties && this.counties.length > 0) {
            countyMatch = this.counties.includes(county(i.location));
          }
          let tagMatch = true;
          if (this.tags && this.tags.length > 0) {
            let intersect = new Set([...i.tags].filter(y => new Set(this.tags).has(y)));
            console.log(intersect);
            tagMatch = intersect.size > 0;
          }
          return rating >= this.minRating && countyMatch && tagMatch;
        })
      },
    },
    head() {
      return {title: "Item Search"};
    },
  };
</script>

<style>
.textured-green {
  background-color: #e8f0e7 !important;
  background-image: url("https://www.transparenttextures.com/patterns/60-lines.png");
  background-repeat: repeat;
}
</style>
