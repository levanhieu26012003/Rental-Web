import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import { Provider } from 'react-redux';
import store from './features/store';
import { ApolloClient, InMemoryCache, ApolloProvider, split, HttpLink } from "@apollo/client";
import { WebSocketLink } from "@apollo/client/link/ws";
import { getMainDefinition } from "@apollo/client/utilities";
import { SubscriptionClient } from "subscriptions-transport-ws";
import cookie from "react-cookies";
import { setContext } from '@apollo/client/link/context';

// Function to get the latest token
const getToken = () => cookie.load('token');

// Tạo SubscriptionClient với các tham số, cập nhật token trong connectionParams
const subscriptionClient = new SubscriptionClient(
  "ws://localhost:8080/subscriptions",
  {
    reconnect: true,
    connectionParams: () => {
      const token = getToken();
      return {
        Authorization: token ? `Bearer ${token}` : "",
      };
    },
  }
);

// Tạo WebSocketLink từ SubscriptionClient
const wsLink = new WebSocketLink(subscriptionClient);

// Tạo link HTTP cho các truy vấn và mutation
const httpLink = new HttpLink({
  uri: 'http://localhost:8080/graphql',
});

// Cập nhật token mỗi lần có truy vấn hoặc mutation thông qua authLink
const authLink = setContext((_, { headers }) => {
  const token = getToken();
  console.log(token)
  return {
    headers: {
      ...headers,
      Authorization: token ? `Bearer ${token}` : "",
    },
  };
});

// Phân chia link giữa HTTP và WebSocket dựa trên loại operation
const splitLink = split(
  ({ query }) => {
    const definition = getMainDefinition(query);
    return (
      definition.kind === 'OperationDefinition' &&
      definition.operation === 'subscription'
    );
  },
  wsLink,                // Dùng WebSocketLink cho subscription
  authLink.concat(httpLink) // Dùng HTTPLink cho truy vấn và mutation
);

// Tạo Apollo Client
const client = new ApolloClient({
  link: splitLink,
  cache: new InMemoryCache(),
});
const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <ApolloProvider client={client}>
    <Provider store={store} >
      <React.StrictMode>
        <App />
      </React.StrictMode>
    </Provider>,
  </ApolloProvider>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
// subscriptionClient.close();