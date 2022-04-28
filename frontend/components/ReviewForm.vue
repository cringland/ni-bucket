<template>
  <div>
    <v-chip
      @click.stop.prevent="dialog = !dialog"
      color="secondary"
    >
      Write a Review
    </v-chip>
    <v-dialog
      v-model="dialog"
      persistent
      max-width="600px"
    >
      <v-card>
        <v-card-title>
          <span class="text-h5">New Review for {{ bucketItem.title }}</span>
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
                    counter="30"
                    required
                  ></v-text-field>
                </v-col>
                <v-col cols="12">
                </v-col>
                <v-col cols="12">
                  <v-rating
                    v-model="rating"
                    background-color="orange lighten-3"
                    color="orange"
                    large
                  ></v-rating>
                </v-col>
                <v-col cols="12">
                  <v-textarea
                    counter="400"
                    v-model="text"
                    label="Text"
                    outlined
                    :rules="textRules"
                  ></v-textarea>
                </v-col>
                <v-col cols="12">
                  <v-file-input
                    v-model="images"
                    multiple
                    small-chips
                    :rules="imageRules"
                    accept="image/*"
                    label="Images"
                  ></v-file-input>
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
            @click="submit(bucketItem.slug)"
          >
            Save
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
  export default {
    props: ['bucketItem'],
    data: () => ({
      dialog: false,
      valid: false,
      title: '',
      rating: '',
      text: '',
      images: null,
      titleRules: [
        v => !!v || 'Title is required',
        v => v.length <= 30 || 'Title must be less than 20 characters',
      ],
      textRules: [
        v => v.length <= 300 || 'Review must be less than 300 characters',
      ],
      imageRules: [
        v => v && v.length <= 5 || 'You cannot upload more than 5 images',
      ],
    }),
    methods: {
      async submit(slug) {
        if (this.valid) {
          const username = this.$auth.user.username;
          const response = await this.$axios.post(`/review`, {
            bucketItemSlug: slug,
            author: username,
            title: this.title,
            text: this.text,
            rating: this.rating
          }, {
            headers: {
              "Content-Type": "application/json"
            },
          });

          console.log(response);
          var reviewId = response.data.id;
          for (let image of this.images) {
            let formData = new FormData();
            formData.append("image", image);
            await this.$axios.post(`/review/${slug}/${reviewId}`, formData, {
              headers: {
                "Content-Type": "multipart/form-data"
              },
            });
          }
        }
      },
    },
  }
</script>
