import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { useQuery, gql } from '@apollo/client';
import { Container, Typography, Box, Avatar, CardContent, Grid, Button, TextField } from '@mui/material';
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";
import Slider from 'react-slick';
import { useNavigate } from 'react-router-dom'; // Thêm hook useNavigate
import User from './User';
import MapComponent from './MapComponent';
import Maps from './Maps';
import CircularIndeterminate from '../pages/Spiner';

// GraphQL query để lấy thông tin chi tiết của motel
const GET_MOTEL_BY_ID = gql`
  query GetMotelById($id: ID!) {
    getMotelById(id: $id) {
      id
      title
      address
      price
      area
      numberTenant
      wards
      district
      province
      status
      lng
      lat
      imageCollection {
        url
      }
      userId {
        id
        username
        fullname
        email
        avatar
      }
      createdDate
      commentCollection {
        content
        createdDate
        userId {
          id
          username
          fullname
          avatar
        }
      }
    }
  }
`;

const MotelDetai = () => {
  const settings = {
    dots: true,
    infinite: true,
    speed: 500,
    slidesToShow: 1,
    slidesToScroll: 1,
  };
  const { id } = useParams(); // Lấy id từ URL
  const { data, loading, error } = useQuery(GET_MOTEL_BY_ID, {
    variables: { id },
  });
  const [comment, setComment] = useState('');

  const handleInputChange = (event) => {
    setComment(event.target.value);
  };


  const handleSubmit = async (event) => {
    event.preventDefault();

    try {
      // Gửi request về server để lưu comment
      // const response = await axios.post('http://localhost:8080/api/comments', { comment });

      // Xử lý khi gửi thành công
      // console.log('Comment sent:', response.data);
      setComment('');  // Clear input after submitting
    } catch (error) {
      console.error('Error sending comment:', error);
    }
  };
  const [maps, setMaps] = useState([]);
  useEffect(() => {
    if (data) {
      const motel = data.getMotelById;
      // Kiểm tra xem lng và lat có tồn tại và có đúng dạng không
      if (motel && motel.lat && motel.lng) {
        setMaps([{ lat: motel.lat, lng: motel.lng }]);
      }
    }
  }, [data])


  if (loading) return <CircularIndeterminate/>;
  if (error) return <p>Error: {error.message}</p>;
  const motel = data.getMotelById;

  // Format giá tiền
  const formattedPrice = (motel.price / 1000000).toLocaleString('en-US') + ' triệu';

  // Format ngày tạo
  const createdDate = new Date(motel.createdDate).toLocaleDateString('vi-VN');
  const createdDateCommnet = new Date(motel.createdDate).toLocaleDateString('vi-VN');

  return (
    <Container>
      <Typography
        variant="h4"
        gutterBottom
        color="primary"
        align="center"
        style={{ fontWeight: 'bold', textTransform: 'uppercase', marginTop: '20px' }}
      >
        {motel.title}
      </Typography>

      <Grid container spacing={2}>
        {/* Phần hình ảnh */}
        <Grid item xs={12} md={6}>
          <Slider {...settings}>
            {motel.imageCollection.map((image, index) => (
              <div key={index}>
                <img
                  src={image.url}
                  alt={`Motel Image ${index + 1}`}
                  style={{ width: '100%', height: '400px', objectFit: 'cover' }}
                />
              </div>
            ))}
          </Slider>
        </Grid>

        {/* Phần thông tin chi tiết */}
        <Grid item xs={12} md={6}>
          <CardContent>
            <Typography variant="body2"  style={{ fontSize: '16px' }}>
              Địa chỉ: {motel.address}, {motel.wards}, {motel.district}, {motel.province}
            </Typography>
            <Typography variant="body2" style={{ fontSize: '20px' }}>
              Giá: {formattedPrice}
            </Typography>
            <Typography variant="body2" style={{ fontSize: '16px' }}>
              Diện tích: {motel.area} m²
            </Typography>

            {/* Thông tin người đăng */}
            <User userId={motel.userId} />
          </CardContent>
        </Grid>
      </Grid>

      {/* Phần bình luận */}
      <Box mt={4}>
        <Typography variant="h6" gutterBottom>
          Bình luận
        </Typography>
        {/* {motel.commentCollection.map((comment, index) => {
          const commentDate = new Date(comment.createdDate).toLocaleDateString('vi-VN');
          return (
            <Box key={comment.id} display="flex" alignItems="flex-start" mb={2} p={2} sx={{ backgroundColor: '#f9f9f9', borderRadius: 2 }}>
      <User userId={comment.userId}  />

      <Box ml={2} flex={1}>
        <Box display="flex" justifyContent="space-between" alignItems="center">
          <Typography variant="body2" color="text.secondary" fontSize="12px">
            Đăng ngày: {commentDate}
          </Typography>
        </Box>

        <Typography variant="body2" color="text.secondary" mt={1} sx={{ wordWrap: 'break-word' }}>
          {comment.content}
        </Typography>
      </Box>
    </Box>
          )
        })}
        a */}
        <Box sx={{  margin: 'auto', padding: 2 }}>
          <form onSubmit={handleSubmit}>
            <TextField
              label="Write your comment"
              placeholder="Enter your comment"
              multiline
              fullWidth
              rows={4}
              value={comment}
              onChange={handleInputChange}
              variant="outlined"
              error={!!error}
              helperText={error}
              sx={{ marginBottom: 2 }}
            />
            <Button variant="contained" color="primary" type="submit">
              Submit
            </Button>
          </form>
        </Box>
      </Box>
      {/* <MapComponent markers1={maps}/> */}
      <Maps markers1={maps}/>
    </Container>

  );
};

export default MotelDetai;
