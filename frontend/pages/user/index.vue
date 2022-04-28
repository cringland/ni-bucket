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

          <v-card-text class="pb-0">
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

            <v-chip-group v-if="item.tags" class="mt-4">
              <v-chip v-for="tag of item.tags" :key="tag" disavked>{{ tag }}</v-chip>
            </v-chip-group>
            <div v-else="item.tags" class="mt-4 text-subtitle-1">
              <span>No tags</span>
            </div>

          </v-card-text>

          <v-card-actions>
            <v-checkbox
              v-model="item.completed"
              label="Completed"
              color="success"
              hide-details
              @click.stop.prevent=""
              @change="markCompleted(item.slug, $event)"
            ></v-checkbox>
            <v-spacer></v-spacer>
            <ReviewForm
              v-if="item.completed"
              :bucket-item="item"
            ></ReviewForm>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
  import {mapGetters} from 'vuex';

  export default {
    layout: "default",
    auth: true,
    data: () => ({
      userInfo: {}
    }),
    async fetch() {
      this.userInfo = await this.$axios.$get('/user/' + this.username)
    },
    computed: {
      ...mapGetters(['username']),
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
    methods: {
      markCompleted(slug, checked) {
        const getDate = () => {
          const today = new Date();
          const date = today.getFullYear() + '-' + (today.getMonth() + 1) + '-' + today.getDate();
          return date;
        };
        this.$axios.put(`/user/${this.username}/bucket`, {
          slug: slug,
          ...(checked && {dateCompleted: getDate()}),
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
      },
    },
    head() {
      return { title: "Your Bucket" }
    }
  };
</script>
