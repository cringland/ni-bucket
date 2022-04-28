<template>
  <v-container>
    <h1>Your Bucket</h1>
    <v-row>
      <v-col
        v-for="item of userInfo.bucketList"
        :key="item.slug"
      >
        <v-card
          :to="'/bucket/' + item.slug"
          nuxt
          hover
          class="mx-auto my-12"
          max-width="374"
        >
          <v-img
            height="250"
            :src="'https://ni-bucket-image.s3.eu-west-1.amazonaws.com/' + item.photoId"
          ></v-img>

          <v-card-title>{{ item.title }}</v-card-title>

          <v-card-text>
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
              <p class="mb-0" v-else>No Ratings</p>
            </v-row>

            <div v-if="item.tags" class="my-4 text-subtitle-1">
              <span v-for="tag of item.tags" :key="tag">{{ tag }} </span>
            </div>
            <div v-else="item.tags" class="my-4 text-subtitle-1">
              <span>No tags</span>
            </div>

          </v-card-text>

          <v-card-actions>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>

  export default {
    layout: "default",
    auth: true,
    data: () => ({
      userInfo: {}
    }),
    async asyncData({$axios, params}) {
      const slug = params.slug;
      return { userInfo: $axios.$get('/user/' + slug) };
    },
    head() {
      return { title: "Your Bucket" }
    }
  };
</script>
