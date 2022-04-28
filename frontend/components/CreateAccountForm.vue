<template>
  <v-dialog
    v-model="dialog"
    persistent
    max-width="600px"
  >
    <template v-slot:activator="{ on, attrs }">
      <v-btn
        color="primary"
        v-on="on"
        v-bind="attrs"
      >Create Account
      </v-btn>
    </template>
    <v-card>
      <v-card-title>
        <span class="text-h5">Create a new Account</span>
      </v-card-title>
      <v-card-text>
        <v-form v-model="valid">
          <v-container>
            <v-row>
              <v-col cols="12">
                <v-text-field
                  v-model="username"
                  label="Username"
                  :rules="usernameRules"
                  :error-messages="usernameErrors"
                  hint="This will be used when logging in"
                  :counter="20"
                  required
                  @input="changeUsername"
                ></v-text-field>
              </v-col>
              <v-col cols="12">
                <v-autocomplete
                  v-model="tags"
                  :items="['Hiking', 'Food', 'Drink', 'Museum']"
                  label="Your Interests"
                  chips
                  deletable-chips
                  multiple
                ></v-autocomplete>
              </v-col>
              <v-col>
                <v-text-field
                  v-model="email"
                  label="Email"
                  required
                ></v-text-field>
              </v-col>
              <v-col cols="12">
                <v-text-field
                  v-model="password"
                  :append-icon="showPass ? 'mdi-eye' : 'mdi-eye-off'"
                  :type="showPass ? 'text' : 'password'"
                  label="Password"
                  hint="At least 8 characters"
                  :rules="passwordRules"
                  counter
                  @click:append="showPass = !showPass"
                ></v-text-field>
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
  import {Auth} from 'aws-amplify';

  const userExistsError = 'This username is taken';

  export default {
    data: () => ({
      dialog: false,
      valid: false,
      showPass: false,
      username: '',
      password: '',
      email: '',
      tags: null,
      usernameErrors: [],
      usernameRules: [
        v => !!v || 'Username is required',
        v => v.length <= 20 || 'Username must be less than 20 characters',
      ],
      passwordRules: [
        v => !!v || 'Password is required',
        v => v.length >= 8 || 'Password must be at least 8 characters',
      ],
    }),
    methods: {
      changeUsername() {
        if (this.usernameErrors.includes(userExistsError))
          this.usernameErrors = [];
      },
      async submit() {
        if (this.valid) {
          try {
            const awsResponse = await Auth.signUp({
              username: this.username,
              password: this.password,
              attributes: {
                email: this.email,
              },
              autoSignIn: {
                enabled: true,
              }
            });
            console.log(awsResponse);
            const response = await this.$axios.post('/user', {
              username: this.username,
              tags: this.tags
            }, {
              headers: {
                "Content-Type": "application/json"
              },
            });
            this.$auth.loginWith("awsCognito");
          } catch (error) {
            if (error.name === 'UsernameExistsException') {
              this.usernameErrors.push(userExistsError);
            } else {
              console.log(error);
              console.log(error.name);
            }
          }

        }
      },
    },
  }
</script>
