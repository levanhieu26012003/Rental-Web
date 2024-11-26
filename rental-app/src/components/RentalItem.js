import React, { useState } from 'react';
import { Card, CardContent, CardMedia, Typography, Button, Box, Grid } from '@mui/material';
import { useNavigate } from 'react-router-dom'; // Thêm hook useNavigate
import { useSelector } from 'react-redux';
import { toast, ToastContainer } from 'react-toastify';
import { authApi, endpoints } from '../api/api'

const RentalItem = ({ rental, isOwner }) => {
  const navigate = useNavigate(); // Khởi tạo useNavigate để điều hướng
  const auth = useSelector((state) => state.auth.auth);
  const [isHidden, setIsHidden] = useState('block');

  const showConfirmationToast = () => {
    console.log(rental.id);
  
    const handleConfirm = async () => {
      try {
        const response = await authApi().post(endpoints['rentedMotel'], { id: rental.id });
        setIsHidden(isHidden === 'block' ? 'none' : 'block');
        toast.success('Đã ẩn bài viết!');
      } catch (error) {
        console.log(error);
        toast.error('Không thể ẩn bài viết!');
      }
    };
  
    toast(
      ({ closeToast }) => (
        <div>
          <p>Bạn có chắc muốn ẩn bài viết này?</p>
          <Button
            variant="contained"
            color="primary"
            onClick={() => {
              handleConfirm(); // Gọi hàm bất đồng bộ
              closeToast(); // Đóng toast khi xác nhận
            }}
            style={{ marginRight: '10px' }}
          >
            Có
          </Button>
          <Button
            variant="outlined"
            color="secondary"
            onClick={() => {
              toast.info('Đã hủy thao tác.');
              closeToast();
            }}
          >
            Không
          </Button>
        </div>
      ),
      {
        position: 'top-center',
        autoClose: false,
        closeOnClick: false,
        draggable: false,
      }
    );
  };

  const formattedPrice = (rental.price / 1000000).toLocaleString('en-US') + ' triệu';

  const handleCardClick = () => {
    navigate(`/motel/${rental.id}`);
  };

  return (
    <Grid item key={rental.id} xs={12} sm={6} md={4} style={{ cursor: 'pointer', display: isHidden }}>
      <ToastContainer/>
      <Card onClick={handleCardClick} >
        <CardMedia
          component="img"
          alt={rental.title}
          height="200"
          image={rental.imageCollection[0].url}
          style={{ objectFit: 'cover' }}
        />
        <CardContent>
          <Typography gutterBottom variant="h5" component="div">
            {rental.title}
          </Typography>
          <Box display="flex" justifyContent="space-between" alignItems="center">
            <Box>
              <Typography variant="body2">Giá: {formattedPrice}</Typography>
              <Typography variant="body2">
                Diện tích: {rental.area} m<sup>2</sup>
              </Typography>
            </Box>
            {isOwner && <Button onClick={(e) => { e.stopPropagation(); showConfirmationToast(); }} variant="contained" color="primary">
              {!isHidden ? 'Hiện' : 'Ẩn'}
            </Button>
            }
          </Box>
        </CardContent>
      </Card>
    </Grid>
  );
};


;

export default RentalItem;
