import React, { useEffect, useState } from 'react';
import { useQuery, gql, useLazyQuery } from '@apollo/client';
import RentalItem from './RentalItem';
import ReactPaginate from 'react-paginate';
import { Container, Grid, Typography, TextField, Button, Box, Autocomplete } from '@mui/material';
import '../static/css/pagination.css';
import RentalMapWithRadius from './RentalMapWithRadius';
import _ from 'lodash';
import API, { endpoints } from '../api/api'
import { ToastContainer } from 'react-toastify';
// GraphQL query to fetch all motels with search and pagination
const GET_ALL_MOTELS = gql`
  query GetAllMotels($limit: Int, $offset: Int, $priceMin: Int, $priceMax: Int, $address: String) {
    getAllMotels(limit: $limit, offset: $offset, priceMin: $priceMin, priceMax: $priceMax, address: $address) {
      motels {
        id
        title
        address
        price
        area
        createdDate
        userId {
          id
          username
          email
          avatar
        }
        imageCollection {
          url
        }
      }
      totalCount
    }
  }
`;

const RentalList = () => {
  const [currentPage, setCurrentPage] = useState(1);
  const limit = 6;
  const offset = (currentPage - 1) * limit;

  // State cho giá trị tìm kiếm thực sự được sử dụng khi fetch
  const [searchParams, setSearchParams] = useState({
    priceMin: null,
    priceMax: null,
    address: '',
  });

  // State cho giá trị tạm thời của form
  const [tempPriceMin, setTempPriceMin] = useState('');
  const [tempPriceMax, setTempPriceMax] = useState('');
  const [tempAddress, setTempAddress] = useState('');

  // Fetch data với các biến trong `searchParams`
  const { data, loading, error, refetch } = useQuery(GET_ALL_MOTELS, {
    variables: {
      limit,
      offset,
      priceMin: searchParams.priceMin,
      priceMax: searchParams.priceMax,
      address: searchParams.address,
    },
  });

  const [rentals, setRentals] = useState([]);
  const [pageCount, setPageCount] = useState(0);
  const [query, setQuery] = useState("");
  const [suggestions, setSuggestions] = useState([]);

  const fetchSuggestions = _.debounce(async (keyword) => {
    if (keyword) {
      try {
        const respone = await API.get(endpoints['suggest_keyword'], {
          params: { keyword },
        });
        setSuggestions(respone.data);
        console.log("respone " ,respone.data)
      } catch (error) {
        console.log("Error fetching suggestions", error);
      }
    } else {
      setSuggestions([]);
    }

  }, 300)
  const handleInputChange = (e, value) => {
    console.log(value);
    setQuery(value);
    if (value.trim().length > 0) { // Chỉ fetch nếu có giá trị
      fetchSuggestions(value);
    } else {
      setSuggestions([]); // Xóa gợi ý khi input trống
    }
  };
  const handleOptionSelect = (event, value) => {
    if (value) {
      setQuery(value); // Cập nhật giá trị khi chọn từ gợi ý
    }
  };

  useEffect(() => {
    if (data) {
      setRentals(data.getAllMotels.motels);
      setPageCount(Math.ceil(data.getAllMotels.totalCount / limit));
    }
    console.log("sugest ",suggestions)
  }, [data, limit, suggestions]);

  const handlePageClick = (event) => {
    setCurrentPage(event.selected + 1);
  };

  const handleSearch = (e) => {
    e.preventDefault();
    // Cập nhật giá trị tìm kiếm vào state chính và refetch dữ liệu
    setSearchParams({
      priceMin: tempPriceMin ? parseInt(tempPriceMin) : null,
      priceMax: tempPriceMax ? parseInt(tempPriceMax) : null,
      address: query || '',
    });

    setCurrentPage(1); // Reset pagination về trang đầu tiên
    refetch(); // Thực hiện refetch với các giá trị tìm kiếm mới
  };

  if (loading) return <p>Loading...</p>;
  if (error) return <p>Error: {error.message}</p>;

  return (
    <Container>
      <Typography
        variant="h4"
        gutterBottom
        color="primary"
        align="center"
        style={{ fontWeight: 'bold', textTransform: 'uppercase', marginTop: '20px' }}
      >
        Danh sách nhà trọ
      </Typography>

      {/* Thanh tìm kiếm */}
      <Box component="form" onSubmit={handleSearch} sx={{ marginBottom: 3 }}>
        <Grid container spacing={2}>
          <Grid item xs={12} sm={4}>
            <TextField
              label="Min Price"
              fullWidth
              value={tempPriceMin}
              onChange={(e) => setTempPriceMin(e.target.value)}
              type="number"
            />
          </Grid>
          <Grid item xs={12} sm={4}>
            <TextField
              label="Max Price"
              fullWidth
              value={tempPriceMax}
              onChange={(e) => setTempPriceMax(e.target.value)}
              type="number"

            />
          </Grid>
          <Grid item xs={12} sm={4}>
            {/* <TextField
              label="Address"
              fullWidth
              value={query}
              onChange={handleInputChange}
            /> */}
            <Autocomplete
              freeSolo
              options={suggestions}
              inputValue={query}
              onInputChange={handleInputChange}
              onChange={handleOptionSelect} // Xử lý khi chọn gợi ý
              renderInput={(params) => (
                <TextField
                  {...params}
                  label="Nhập từ khóa tìm kiếm"
                  variant="outlined"
                  fullWidth
                />
              )}
            />
          </Grid>
        </Grid>
        <Box textAlign="center" sx={{ marginTop: 2 }}>
          <Button type="submit" variant="contained" color="primary">
            Tìm kiếm
          </Button>
        </Box>
      </Box>

      {/* Danh sách kết quả */}
      <Grid container spacing={4}>
        {rentals.map((rental, index) => (
          <RentalItem key={index} rental={rental} />
        ))}
      </Grid>

      {/* Pagination */}
      <ReactPaginate
        previousLabel={"previous"}
        nextLabel={"next"}
        breakLabel={"..."}
        pageCount={pageCount}
        marginPagesDisplayed={1}
        pageRangeDisplayed={2}
        onPageChange={handlePageClick}
        containerClassName={"pagination"}
        activeClassName={"active"}
        forcePage={currentPage - 1}
      />
      <ToastContainer />
      <RentalMapWithRadius />
    </Container>
  );
};

export default RentalList;
