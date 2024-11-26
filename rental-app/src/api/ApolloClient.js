import { ApolloClient, InMemoryCache } from '@apollo/client';

const client = new ApolloClient({
  uri: 'http://localhost:8080/api/graphql', // Địa chỉ của GraphQL endpoint
  cache: new InMemoryCache(),
});

export default client;
