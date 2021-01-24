import { Configuration } from './model';


export const config: Configuration = {
  apiUrl: 'http://localhost:8080',
  authUrl: 'http://localhost:8082',
  clientId: 'test',
  clientSecret: 'test',
  carausel: [
    {
      title: 'Barclays Hackathon',
      text: 'Barclays Hackathon',
      imageUrl: 'https://s3-ap-southeast-1.amazonaws.com/he-public-data/blue-book-hic09def7.png'
    },
    {
      title: 'Aman Kumar Singh',
      text: 'Aman Kumar Singh',
      imageUrl: 'https://s3-ap-southeast-1.amazonaws.com/he-public-data/red-book-hi8d6431a.png'
    },
    {
      title: 'Barclays Books',
      text: 'Barclays Books',
      imageUrl: 'https://s3-ap-southeast-1.amazonaws.com/he-public-data/closed-book-cartoon-vector-symbol-icon-design-beautiful-illustr-illustration-isolated-white-background-975033320bc2a72.jpeg'
    }
  ],
  bannerUrl: ''
};
