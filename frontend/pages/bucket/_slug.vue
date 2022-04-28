<template>
  <v-container>
    <v-row>
      <v-col
        cols="12"
        sm="8"
      >
        <v-sheet
          class="pa-5 textured-green"
          rounded="lg"
          min-height="70vh"
        >
          <span class="text-h4">{{ bucketItem.title }}</span><span
          class="grey--text ms-4"> - {{ bucketItem.location }}</span>
          <v-text nuxt :to="'/user/' + bucketItem.author" class="grey--text ms-4">
            Added by {{ bucketItem.author }}
          </v-text>
          <v-container>
            <v-row>
              <v-col>
                <v-img :src="'https://ni-bucket-image.s3.eu-west-1.amazonaws.com/' + bucketItem.photoId"
                       aspect-ratio="1.77"></v-img>
              </v-col>
            </v-row>
          </v-container>
        </v-sheet>
      </v-col>

      <v-col
        cols="12"
        sm="4"
      >
        <v-sheet
          class="pa-5 textured-green"
          min-height="70vh"
          rounded="lg"
        >
          <h5 class="text-h5">Reviews</h5>
          <v-pagination
            v-model="page"
            :length="1"
          ></v-pagination>

          <v-card
            v-for="review of reviews"
            :key="review.id"
            elevation="2"
            class="mb-3"
          >
            <v-card-title>{{ review.title }}</v-card-title>
            <v-card-text>
              <v-row
                align="center"
                class="mx-0"
              >
                <v-rating
                  :value="review.rating"
                  color="amber"
                  dense
                  half-increments
                  readonly
                  size="14"
                ></v-rating>
                <div class="grey--text ms-4">
                  {{ review.rating }}
                </div>
              </v-row>
              <v-row class="mx-0">
                <div class="text-lg-body1">{{ review.text }}</div>
              </v-row>
            </v-card-text>
            <v-card-actions>
              <v-dialog
                v-if="review.imageIds && review.imageIds.length > 0"
                v-model="review.dialog"
                width="75vw"
              >
                <template v-slot:activator="{ on, attrs }">
                  <v-btn
                    v-bind="attrs"
                    v-on="on"
                    small
                  >
                    See images
                  </v-btn>
                </template>

                <v-card>
                  <v-card-title class="text-h5 grey lighten-2">
                    {{review.title}}
                  </v-card-title>

                  <v-card-text>
                    <v-carousel
                      hide-delimiter-background
                      rounded="lg"
                      height="auto"
                    >
                      <v-carousel-item eager v-for="id of review.imageIds" :key="id">
                        <v-img eager :src="'https://ni-bucket-image.s3.eu-west-1.amazonaws.com/' + id"
                               max-height="70vh" contain>
                        </v-img>
                      </v-carousel-item>
                    </v-carousel>
                  </v-card-text>

                  <v-divider></v-divider>

                  <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn
                      color="primary"
                      text
                      @click="review.dialog = false"
                    >
                      Close
                    </v-btn>
                  </v-card-actions>
                </v-card>
              </v-dialog>
            </v-card-actions>
          </v-card>
        </v-sheet>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
  export default {
    layout: "default",
    auth: false,
    data: () => ({
      slug: "",
      reviews: [],
      bucketItem: {},
    }),
    async asyncData({$axios, params}) {
      const slug = params.slug;
      const reviews = await $axios.$get('/bucket/' + slug + '/reviews');
      const bucketItem = await $axios.$get('/bucket/' + slug);
      return {slug, reviews, bucketItem}
    },
    head() {
      return {title: this.bucketItem.title}
    }
  };
</script>

<style>
.textured-green {
  background-color: #e8f0e7 !important;
  background-image: url("https://www.transparenttextures.com/patterns/60-lines.png");
  background-repeat: repeat;
}
</style>
