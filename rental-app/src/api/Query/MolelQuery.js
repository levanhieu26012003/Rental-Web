import { gql } from '@apollo/client';
const GET_ALL_MOTELS = gql`
  query {
    getAllMotels {
      id
      title
      address
      price
      area
      wards
      district
      province
    }
  }
`;


export default  GET_ALL_MOTELS;
