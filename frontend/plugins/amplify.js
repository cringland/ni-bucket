import {Amplify} from 'aws-amplify';

Amplify.configure({
  Auth: {
    region: 'eu-west-1',
    userPoolId: 'eu-west-1_A0qIcAYcB',
    userPoolWebClientId: '6o5t66kguk16db28c41kh53bsm',
  }
});
