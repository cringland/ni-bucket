<template>
  <v-container fluid fill-width class="pa-0">
    <v-row>
      <v-col cols="12">
        <v-card class="transparent-grey"
                max-height="70vh">
          <v-carousel
            hide-delimiter-background
            rounded="lg"
            cycle
            interval="4000"
            height="auto"
          >
            <v-carousel-item eager v-for="bucketItem of bucketItems" :key="bucketItem.slug">
              <v-img width="100vw" eager
                     :src="'https://ni-bucket-image.s3.eu-west-1.amazonaws.com/' + bucketItem.photoId"
                     height="70vh">
                <h3 class="center-text transparent-grey text-center text-sm-h1">{{ bucketItem.title }}</h3>
              </v-img>
            </v-carousel-item>
          </v-carousel>
        </v-card>

      </v-col>

      <v-col v-if="!isLoggedIn">
        <v-card class="text-center background-cream" flat>
          <h1>FIND ADVENTURE</h1>
          <CreateAccountForm></CreateAccountForm>

          <v-btn
            color="secondary"
            nuxt="true"
            href="/login"
          >Login
          </v-btn>
        </v-card>
      </v-col>
      <v-col v-if="isLoggedIn">
        <v-card class="text-center background-cream" flat>
          <h1>FIND ADVENTURE</h1>
          <v-btn
            color="primary"
            nuxt="true"
            href="/all-items"
          >Find new bucket items
          </v-btn>

          <v-btn
            color="secondary"
            nuxt="true"
            href="/user"
          >Your bucket list
          </v-btn>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
  import {mapGetters} from 'vuex';
  import CreateAccountForm from "../components/CreateAccountForm";

  export default {
    components: {CreateAccountForm},
    layout: "default",
    auth: false,
    data: () => ({
      bucketItems: {},
    }),
    async fetch() {
      const slugs = ['carrick-a-rede-rope-bridge', 'climb-slieve-donard', 'relax-at-the-cathedral-quartar', 'climb-slieve-bearnagh', 'the-dark-hedges'];
      const bucketItems = [];
      for (let slug of slugs) {
        let response = await this.$axios.$get('/bucket/' + slug);
        bucketItems.push(response);
      }
      this.bucketItems = bucketItems;
    },
    computed: {
      ...mapGetters(['isLoggedIn'])
    },
    head() {
      return {title: "Home"}
    },
  };
</script>

<style scoped="scoped">
  .transparent-grey {
    background-color: rgba(238, 238, 238, 0.2);
    font-family: 'Arvo', serif !important;

    /*font-weight: bold;*/

    /*text-shadow:*/
      /*-1px -1px 0 #2b2b2b,*/
      /*1px -1px 0 #2b2b2b,*/
      /*-1px 1px 0 #2b2b2b,*/
      /*1px 1px 0 #2b2b2b;*/
    color: white;
  }

  .center-text {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
  }
</style>
