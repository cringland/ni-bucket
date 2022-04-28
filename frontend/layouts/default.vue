<template>
  <v-app class="background-cream" id="inspire">
    <v-app-bar
      app
      dense
      class="greener"
    >
      <v-app-bar-nav-icon @click.stop="drawer = !drawer"></v-app-bar-nav-icon>
      <v-img max-height="40"
             max-width="40"
             contain src="/logo.png"></v-img>
      <v-toolbar-title underline class="text-decoration-underline font-weight-bold navbar-header">NI Bucket List</v-toolbar-title>
    </v-app-bar>

    <v-navigation-drawer
      v-model="drawer"
      app
      disable-resize-watcher
      temporary
    >
      <v-list>
        <BucketItemForm v-if="isLoggedIn"></BucketItemForm>
        <v-list-item
          v-for="item in items"
          :key="item.title"
          link
          :to="item.link"
        >
          <v-list-item-icon>
            <v-icon>{{ item.icon }}</v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title>{{ item.title }}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>

    <v-main fill-width>
      <v-container fluid fill-width class="pa-0">
        <nuxt/>
      </v-container>
    </v-main>
  </v-app>
</template>

<script>
  import {mapGetters} from 'vuex';

  export default {
    data() {
      return {
        drawer: false,
      };
    },
    computed: {
      ...mapGetters(['isLoggedIn']),
      items() {
        return [
          {title: 'Home', icon: 'mdi-home', link: "/"},
          {title: 'Your Bucket', icon: 'mdi-bucket', link: "/user", authRequired: true},
          {title: 'Find new items', icon: 'mdi-list-box', link: "/all-items"},
        ].filter(item => this.isLoggedIn || !item.authRequired)
      },
    },
    methods: {
      logOut() {
        this.$auth.logout();
      }
    },
  };
</script>

<style>
  @import url('https://fonts.googleapis.com/css2?family=Josefin+Sans&display=swap');
  @import url('https://fonts.googleapis.com/css2?family=Arvo&display=swap');

  * {
    color: #475468;
    font-family: 'Josefin Sans', sans-serif;
  }

  html {
    overflow-y: auto !important;
  }

  .background-cream {
    background-color: #F6F5F0 !important;
  }

  .greener {
    background-color: #a3b18a !important;
    background-image: url("https://www.transparenttextures.com/patterns/45-degree-fabric-light.png");
    background-repeat: repeat;
  }

  .navbar-header {
    font-family: 'Arvo', serif !important;
    color: white;

    /*font-weight: bold;*/

    text-shadow: -1px -1px 0 #2b2b2b,
    1px -1px 0 #2b2b2b,
    -1px 1px 0 #2b2b2b,
    1px 1px 0 #2b2b2b;
  }
</style>
