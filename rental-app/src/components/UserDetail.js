import React, { useEffect, useState } from "react";
import {
  Avatar,
  Card,
  CardContent,
  Typography,
  Grid,
  Container,
  CircularProgress,
  Alert,
  Box,
  Divider,
  Paper,
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
} from "@mui/material";
import { gql, useQuery } from "@apollo/client";
import { Navigate, useNavigate, useParams } from "react-router-dom";
import RentalItem from "./RentalItem";
import APIs, { authApi, endpoints } from "../api/api";
import Follower from "./Follow";
import { useSelector } from "react-redux";
// import {checkOrCreateRoom} from '../components/ChekOrCreateRoom'

import { collection, query, where, getDocs, addDoc, serverTimestamp } from "firebase/firestore";
import { firestore } from '../features/firebase'; // Firebase config

const checkOrCreateRoom = async (currentUserEmail, currentUserName, targetUserEmail, targetUserName) => {
  // Tạo roomId dựa trên email của hai người dùng (đảm bảo id duy nhất)
  const roomId = currentUserEmail > targetUserEmail
    ? `${currentUserEmail}_${targetUserEmail}`
    : `${targetUserEmail}_${currentUserEmail}`;

  const roomsRef = collection(firestore, 'rooms');

  // Kiểm tra xem phòng đã tồn tại hay chưa
  const q = query(roomsRef, where('id', '==', roomId));
  const querySnapshot = await getDocs(q);

  if (querySnapshot.empty) {
    // Tạo phòng mới nếu phòng chưa tồn tại
    const roomData = {
      id: roomId,
      users: [currentUserEmail, targetUserEmail],  // Email của hai người
      fullNames: [currentUserName, targetUserName],  // Tên đầy đủ của hai người
      createdAt: serverTimestamp(),
    };

    // Thêm phòng vào Firestore
    await addDoc(roomsRef, roomData);
    console.log("Room created:", roomData);
  } else {
    console.log("Room already exists.");
  }

  return roomId;
};

const GET_USER_BY_ID = gql`
              query GetUserById($id: ID!){
                getUserById(id: $id) {
                  id
                  fullname
                  email
                  avatar
                  userCollection1 {
                    id
                  }
                  reviewsCollection {
                    rating
                    comment
                    tenantId{
                        fullname
                        avatar
                    }
                  }
                  motelCollection {
                    id
                    active
                    title
                    address
                    wards
                    district
                    province
                    price
                    createdDate
                    imageCollection {
                        url
                    }
                  }
                }
              }
            `;

const UserDetail = () => {
  const { id } = useParams();
  const { data, loading, error } = useQuery(GET_USER_BY_ID, {
    variables: { id },
  });
  const [userData, setUserData] = useState('');
  const [open, setOpen] = useState(false); // Trạng thái mở hộp thoại
  const [rating, setRating] = useState(''); // Giá trị số sao
  const [comment, setComment] = useState(''); // Giá trị bình luận
  const auth = useSelector((state) => state.auth.auth)
  const [isOwner, setIsOwner] = useState(false);
  const navigate = useNavigate();
  useEffect(() => {
    if (data) {
      setUserData(data.getUserById);
    }
    if (auth.user) {
      console.log(auth)
      setIsOwner(id == auth.user.id)
    }
  }, [data, auth]);

  if (loading) return <CircularProgress />;
  if (error) return <Alert severity="error">Lỗi</Alert>;


  const calculateAverageRating = (reviews) => {
    if (!reviews || reviews.length === 0) return 0;
    const totalRating = reviews.reduce((acc, review) => acc + review.rating, 0);
    return (totalRating / reviews.length).toFixed(1);
  };
  const averageRating = userData.reviewsCollection ? calculateAverageRating(userData.reviewsCollection) : 0;
  // Hàm mở hộp thoại
  const handleClickOpen = () => {
    if (!auth.user) {
      navigate('/login')
    } else {
      setOpen(true);
    }
  };

  // Hàm đóng hộp thoại
  const handleClose = () => {
    setOpen(false);
    setRating('');
    setComment('');
  };

  // Hàm gửi đánh giá
  const handleSubmit = async () => {
    try {
      const respone = await authApi().post(endpoints["review"], {
        comment,
        rating,
        hostId: userData.id,
        tenantId: auth.user.id.toString()
      })
      console.log("r: ", respone.data)
    } catch (error) {
      console.log(error)
    }
    handleClose();

  };

  const handleChat = async () => {
    if (!auth.user) {
      navigate('/login')
    } else {
      const roomId = await checkOrCreateRoom(auth.user.email, auth.user.fullname, userData.email, userData.fullname);
      navigate(`/chat`);
    }

  };
  return (
    <Container>
      {/* User Information */}
      <Card sx={{ marginBottom: 3, marginTop: 3 }}>
        <CardContent sx={{ display: "flex", alignItems: "center" }}>
          <Avatar src={userData.avatar} sx={{ width: 100, height: 100, marginRight: 3 }} />
          <Box sx={{ flexGrow: 1 }}>
            <Typography variant="h5">{userData.fullname}</Typography>
            <Typography color="textSecondary">Email: {userData.email}</Typography>
            <Typography>Điểm đánh giá: {averageRating} ★</Typography>
            {userData && userData.id && !isOwner && <Follower id={userData.id} />}

          </Box>
          {!isOwner &&
            <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-end' }}>
              <Paper elevation={3} sx={{ padding: 2, marginBottom: 1, marginTop: 1 }}>
                <Box display="flex" justifyContent="center" alignItems="center">
                  <Button variant="contained" color="primary" onClick={handleChat}>
                    Nhắn tin
                  </Button>
                </Box>
              </Paper>
              <Paper elevation={3} sx={{ padding: 2, marginBottom: 1, marginTop: 1 }}>
                <Box display="flex" justifyContent="center" alignItems="center">
                  <Button variant="contained" color="primary" onClick={handleClickOpen}>
                    Đánh giá
                  </Button>
                </Box>
              </Paper>
            </Box>
          }
        </CardContent>
      </Card>


      <Dialog open={open} onClose={handleClose}>
        <DialogTitle>Nhập đánh giá của bạn</DialogTitle>
        <DialogContent>
          <TextField
            autoFocus
            margin="dense"
            label="Số sao (1-5)"
            type="number"
            fullWidth
            value={rating}
            onChange={(e) => setRating(e.target.value)}
            inputProps={{ min: 1, max: 5 }}
          />
          <TextField
            margin="dense"
            label="Bình luận"
            type="text"
            fullWidth
            value={comment}
            onChange={(e) => setComment(e.target.value)}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose}>Hủy</Button>
          <Button onClick={handleSubmit} variant="contained" color="primary">
            Gửi
          </Button>
        </DialogActions>
      </Dialog>

      {/* Real Estate Listings */}
      <Typography variant="h6" gutterBottom>Bất động sản nổi bật</Typography>
      <Grid container spacing={4}>
        {userData.motelCollection && userData.motelCollection.filter(motel => motel.active === true).map((motel, index) => (
          <RentalItem rental={motel} key={index} isOwner={isOwner} />
        ))}
      </Grid>
    </Container>
  );
};


// const UserProfile = () => {
//     return (<h1>HI</h1>)
// }

export default UserDetail;


